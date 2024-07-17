package dev.orewaee.discordauth.velocity.discord.listeners;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class RemoveByDiscordIdListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;

    private final static String DISCORD_IDS = "discord.ids";
    private final static String NO_PERMISSION = "discord-components.no-permission";
    private final static String NO_DISCORDID = "discord-components.no-discordid";
    private final static String REMOVE_TITLE = "discord-components.remove-title";
    private final static String REMOVE_DESCRIPTION = "discord-components.remove-description";

    public RemoveByDiscordIdListener(Config config) {
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

        if (!event.getFullCommandName().equals("remove bydiscordid")) return;
        
        OptionMapping discordIdMapping = event.getOption("discordid");
        if (discordIdMapping == null) return;
        String discordId = discordIdMapping.getAsString();

        Account account = accountManager.getByDiscordId(discordId);

        if (account == null) {
            String content = config
                .getString(NO_DISCORDID, "There is no account with this discordid")
                .replace("%discordid%", discordId);

            event.reply(content).queue();
            return;
        }

        accountManager.removeByDiscordId(discordId);

        String title = config
            .getString(REMOVE_TITLE, ":red_square: Account removed")
            .replace("%name%", account.getName())
            .replace("%discordid%", discordId);

        String description = config
            .getString(REMOVE_DESCRIPTION, "The account %name% ||%discordid%|| was successfully removed")
            .replace("%name%", account.getName())
            .replace("%discordid%", discordId);

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0xdd2e44)
            .setAuthor(account.getName(), null, "https://mc-heads.net/avatar/" + account.getName())
            .setTitle(title)
            .setDescription(description)
            .build();

        event.replyEmbeds(embed).queue();
    }
}
