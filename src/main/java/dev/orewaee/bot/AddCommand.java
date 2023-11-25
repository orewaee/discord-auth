package dev.orewaee.bot;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.TomlConfig;

public class AddCommand extends ListenerAdapter {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getUser().getId().equals(TomlConfig.getDiscordAdmin())) return;

        if (!event.getName().equals("add")) return;

        OptionMapping nameMapping = event.getOption("name");
        if (nameMapping == null) return;

        String name = nameMapping.getAsString();

        OptionMapping discordIdMapping = event.getOption("discord_id");
        if (discordIdMapping == null) return;

        String discordId = discordIdMapping.getAsString();

        if (accountManager.containsAccountByName(name)) {
            event.reply("An account with the same name already exists").queue();
            return;
        }

        if (accountManager.containsAccountByDiscordId(discordId)) {
            event.reply("An account with the same discord id already exists").queue();
            return;
        }

        accountManager.addAccount(name, discordId);

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0x78b159)
            .setAuthor(name, null, "https://mc-heads.net/avatar/" + name)
            .setTitle(":green_square: Account added")
            .setDescription(
                String.format(
                    "Account added successfully. To remove it, use the command:\n```/remove %s %s```",
                    name, discordId
                )
            )
            .build();

        event.replyEmbeds(embed).queue();
    }
}
