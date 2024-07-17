package dev.orewaee.discordauth.velocity.discord.listeners;

import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class ListListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;

    private final static String DISCORD_IDS = "discord.ids";
    private final static String NO_PERMISSION = "discord-components.no-permission";
    private final static String LIST_TITLE = "discord-components.list-title";
    private final static String LIST_NO_ACCOUNTS = "discord-components.list-no-accounts";
    private final static String LIST_DESCRIPTION = "discord-components.list-description";
    private final static String LIST_ITEM = "discord-components.list-item";

    public ListListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        if (!config.getList(DISCORD_IDS, List.of()).contains(userId)) {
            String content = config
                .getString(NO_PERMISSION, "You don't have permission to use it");

            event.reply(content).setEphemeral(true).queue();
            return;
        }

        if (!event.getName().equals("list")) return;

        List<Account> accounts = accountManager.getAll();
        int quantity = accounts.size();

        String title = config
            .getString(LIST_TITLE, ":page_with_curl: List of accounts")
            .replace("%quantity%", quantity + "");

        if (quantity == 0) {
            String description = config
                .getString(LIST_NO_ACCOUNTS, "No accounts found at this time")
                .replace("%quantity%", quantity + "");

            MessageEmbed embed = new EmbedBuilder()
                .setColor(0x5865f2)
                .setTitle(title)
                .setDescription(description)
                .build();

            event.replyEmbeds(embed).queue();
            return;
        }

        int chunk = 32;
        int messages = (int) Math.ceil(quantity / (double) chunk);

        List<StringJoiner> results = new ArrayList<>();
        for (int i = 0; i < messages; i++) results.add(new StringJoiner("\n"));

        String item = config.getString(LIST_ITEM, "- `%name%` ||%discordid%||");
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);

            int position = i / chunk;
            results.get(position).add(
                item
                    .replace("%name%", account.getName())
                    .replace("%discordid%", account.getDiscordId())
            );
        }

        List<MessageEmbed> embeds = new ArrayList<>();

        String description = config
            .getString(LIST_DESCRIPTION, "At the moment, %quantity% accounts have been found")
            .replace("%quantity%", quantity + "");

        MessageEmbed header = new EmbedBuilder()
            .setColor(0x5865f2)
            .setTitle(title)
            .setDescription(description).build();

        embeds.add(header);

        for (StringJoiner result : results) {
            MessageEmbed embed = new EmbedBuilder()
                .setColor(0x5865f2)
                .setDescription(result + "")
                .build();

            embeds.add(embed);
        }

        event.replyEmbeds(embeds).queue();
    }
}
