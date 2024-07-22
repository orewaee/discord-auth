package dev.orewaee.discordauth.velocity.discord.listeners;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;
import dev.orewaee.discordauth.velocity.discord.utils.PermissionUtils;

public class RemoveByNameListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;
    private final PermissionUtils permissionUtils;

    private final static String NO_PERMISSION = "discord-components.no-permission";
    private final static String NO_NAME = "discord-components.no-name";
    private final static String REMOVE_TITLE = "discord-components.remove-title";
    private final static String REMOVE_DESCRIPTION = "discord-components.remove-description";

    public RemoveByNameListener(Config config, PermissionUtils permissionUtils) {
        this.config = config;
        this.permissionUtils = permissionUtils;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!permissionUtils.hasPermission(event)) {
            String content = config
                .getString(NO_PERMISSION, "You don't have permission to use it");

            event.reply(content).setEphemeral(true).queue();
            return;
        }

        if (!event.getFullCommandName().equals("remove byname")) return;

        OptionMapping nameMapping = event.getOption("name");
        if (nameMapping == null) return;
        String name = nameMapping.getAsString();

        Account account = accountManager.getByName(name);

        if (account == null) {
            String content = config
                .getString(NO_NAME, "There is no account with this name")
                .replace("%name%", name);

            event.reply(content).queue();
            return;
        }

        accountManager.removeByName(name);

        String title = config
            .getString(REMOVE_TITLE, ":red_square: Account removed")
            .replace("%name%", name)
            .replace("%discordid%", account.getDiscordId());

        String description = config
            .getString(REMOVE_DESCRIPTION, "The account %name% ||%discordid%|| was successfully removed")
            .replace("%name%", name)
            .replace("%discordid%", account.getDiscordId());

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0xdd2e44)
            .setAuthor(name, null, "https://mc-heads.net/avatar/" + name)
            .setTitle(title)
            .setDescription(description)
            .build();

        event.replyEmbeds(embed).queue();
    }
}
