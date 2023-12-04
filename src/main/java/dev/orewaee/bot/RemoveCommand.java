package dev.orewaee.bot;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.Config;
import dev.orewaee.config.DiscordMessages;
import dev.orewaee.config.TomlConfig;
import dev.orewaee.config.TomlDiscordMessages;

public class RemoveCommand extends ListenerAdapter {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    private final Config config = TomlConfig.getInstance();
    private final DiscordMessages discordMessages = TomlDiscordMessages.getInstance();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getUser().getId().equals(config.adminDiscordId())) return;

        if (!event.getName().equals("remove")) return;

        OptionMapping nameMapping = event.getOption("name");
        if (nameMapping == null) return;

        String name = nameMapping.getAsString();

        OptionMapping discordIdMapping = event.getOption("discord_id");
        if (discordIdMapping == null) return;

        String discordId = discordIdMapping.getAsString();

        if (!accountManager.containsAccount(name, discordId)) {
            event.reply(
                discordMessages.missingAccount()
            ).queue();

            return;
        }

        accountManager.removeAccount(name, discordId);

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0xdd2e44)
            .setAuthor(name, null, "https://mc-heads.net/avatar/" + name)
            .setTitle(":red_square: Account removed")
            .setDescription(
                String.format(
                    "Account removed successfully. To add it, use the command:\n```/add %s %s```",
                    name, discordId
                )
            )
            .build();

        event.replyEmbeds(embed).queue();
    }
}
