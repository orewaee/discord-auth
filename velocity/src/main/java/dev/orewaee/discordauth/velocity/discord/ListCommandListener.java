package dev.orewaee.discordauth.velocity.discord;

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

public class ListCommandListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;

    public ListCommandListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("list")) return;

        List<Account> accounts = accountManager.getAll();
        int quantity = accounts.size();

        if (quantity == 0) {
            MessageEmbed embed = new EmbedBuilder()
                .setColor(0x5865f2)
                .setTitle(":page_with_curl: List of accounts")
                .setDescription("No accounts found at this time.")
                .build();

            event.replyEmbeds(embed).queue();
            return;
        }

        int chunk = 32;
        int messages = (int) Math.ceil(quantity / (double) chunk);

        List<StringJoiner> results = new ArrayList<>();
        for (int i = 0; i < messages; i++) results.add(new StringJoiner("\n"));

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            String item = String.format("- `%s` ||%s||", account.getName(), account.getDiscordId());

            int position = i / chunk;
            results.get(position).add(item);
        }

        List<MessageEmbed> embeds = new ArrayList<>();

        MessageEmbed header = new EmbedBuilder()
            .setColor(0x5865f2)
            .setTitle(":page_with_curl: List of accounts")
            .setDescription(
                String.format("At the moment, %d accounts have been found.", quantity)
            ).build();

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
