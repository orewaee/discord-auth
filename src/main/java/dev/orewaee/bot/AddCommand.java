package dev.orewaee.bot;

import dev.orewaee.config.TomlConfig;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import dev.orewaee.account.AccountManager;

public class AddCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (!event.getUser().getId().equals(TomlConfig.getDiscordAdmin())) return;
        if (!command.equals("add")) return;

        OptionMapping discordMapping = event.getOption("discord");
        if (discordMapping == null) return;

        IMentionable mentionable = discordMapping.getAsMentionable();
        String discord = mentionable.getId();

        OptionMapping nameMapping = event.getOption("name");
        if (nameMapping == null) return;

        String name = nameMapping.getAsString();

        boolean accountExists = AccountManager.containsAccount(name, discord);

        if (accountExists) {
            event.reply("Account already exists").queue();
            return;
        }

        AccountManager.addAccount(name, discord);

        event.reply("Account added").queue();
    }
}
