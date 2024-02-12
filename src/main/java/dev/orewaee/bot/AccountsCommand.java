package dev.orewaee.bot;

import dev.orewaee.account.Account;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.Config;
import dev.orewaee.config.TomlConfig;

import java.util.*;

public class AccountsCommand extends ListenerAdapter {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    private final Config config = TomlConfig.getInstance();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!config.adminDiscordId().contains(event.getUser().getId())) return;

        if (!event.getName().equals("accounts")) return;

        Set<Account> accounts = accountManager.getAccounts();
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

        int pool = 32;

        int messages = (int) Math.ceil(quantity / (double) pool);

        List<StringJoiner> results = new ArrayList<>();
        for (int i = 0; i < messages; i++) results.add(new StringJoiner("\n"));

        int i = 0;
        for (Account account : accounts) {
            int pos = i / pool;

            results.get(pos).add(
                String.format(
                    "- `%s` ||%s||",
                    account.name(),
                    account.discordId()
                )
            );

            i++;
        }

        List<MessageEmbed> embeds = new ArrayList<>();

        embeds.add(
            new EmbedBuilder()
                .setColor(0x5865f2)
                .setTitle(":page_with_curl: List of accounts")
                .setDescription(
                    String.format("At the moment, %d accounts have been found.", quantity)
                )
                .build()
        );

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
