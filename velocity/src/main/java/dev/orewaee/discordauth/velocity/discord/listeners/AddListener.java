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

public class AddListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;
    private final PermissionUtils permissionUtils;

    private final static String NO_PERMISSION = "discord-components.no-permission";
    private final static String NAME_EXISTS = "discord-components.name-exists";
    private final static String DISCORDID_EXISTS = "discord-components.discordid-exists";
    private final static String ADD_TITLE = "discord-components.add-title";
    private final static String ADD_DESCRIPTION = "discord-components.add-description";

    public AddListener(Config config, PermissionUtils permissionUtils) {
        this.config = config;
        this.permissionUtils = permissionUtils;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Member eventMember = event.getMember();
        if (eventMember == null) return;

        if (permissionUtils.hasPermission(eventMember)) {
            String content = config
                .getString(NO_PERMISSION, "You don't have permission to use it");

            event.reply(content).setEphemeral(true).queue();
            return;
        }

        if (!event.getName().equals("add")) return;

        OptionMapping nameMapping = event.getOption("name");
        if (nameMapping == null) return;
        String name = nameMapping.getAsString();

        OptionMapping discordIdMapping = event.getOption("discordid");
        if (discordIdMapping == null) return;
        String discordId = discordIdMapping.getAsString();

        if (accountManager.containsByName(name)) {
            String content = config
                .getString(NAME_EXISTS, "An account with this name already exists")
                .replace("%name%", name);

            event.reply(content).queue();
            return;
        }

        if (accountManager.containsByDiscordId(discordId)) {
            String content = config
                .getString(DISCORDID_EXISTS, "An account with this discordid already exists")
                .replace("%discordid%", discordId);

            event.reply(content).queue();
            return;
        }

        Account newAccount = new Account(name, discordId);
        accountManager.add(newAccount);

        String title = config
            .getString(ADD_TITLE, ":green_square: Account added")
            .replace("%name%", name)
            .replace("%discordid%", discordId);

        String description = config
            .getString(ADD_DESCRIPTION, "Account %name% ||%discordid%|| has been successfully added")
            .replace("%name%", name)
            .replace("%discordid%", discordId);

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0x78b159)
            .setAuthor(name, null, "https://mc-heads.net/avatar/" + name)
            .setTitle(title)
            .setDescription(description)
            .build();

        event.replyEmbeds(embed).queue();
    }
}
