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

import java.util.Set;
import java.util.StringJoiner;

public class AccountsCommand extends ListenerAdapter {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    private final Config config = TomlConfig.getInstance();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getUser().getId().equals(config.adminDiscordId())) return;

        if (!event.getName().equals("accounts")) return;

        Set<Account> accounts = accountManager.getAccounts();
        int quantity = accounts.size();
        StringJoiner result = new StringJoiner("\n");
        for (Account account : accounts)
            result.add(
                String.format("- `%s` ||%s||", account.name(), account.discordId())
            );

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0x5865f2)
            .setTitle(":page_with_curl: List of accounts")
            .setDescription(
                quantity == 0 ? "No accounts found at this time." :
                    String.format("At the moment, %d accounts have been found.\n\n%s", quantity, result)
            )
            .build();

        event.replyEmbeds(embed).queue();
    }
}
