package dev.orewaee.discordauth.velocity.discord;

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

public class AddCommandListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;

    private final static String DISCORD_IDS = "discord.ids";

    public AddCommandListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String userId = event.getUser().getId();
        if (!config.getList(DISCORD_IDS, List.of()).contains(userId)) {
            event.reply("You do not have permission").setEphemeral(true).queue();
            return;
        }

        if (!event.getName().equals("add")) return;

        OptionMapping nameMapping = event.getOption("name");
        if (nameMapping == null) return;
        String name = nameMapping.getAsString();

        OptionMapping discordIdMapping = event.getOption("discord_id");
        if (discordIdMapping == null) return;
        String discordId = discordIdMapping.getAsString();

        if (accountManager.containsByName(name)) {
            event.reply("An account with this name already exists").queue();
            return;
        }

        if (accountManager.containsByDiscordId(discordId)) {
            event.reply("An account with this discordId already exists").queue();
            return;
        }

        Account newAccount = new Account(name, discordId);
        accountManager.add(newAccount);

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0x78b159)
            .setAuthor(name, null, "https://mc-heads.net/avatar/" + name)
            .setTitle(":green_square: Account added")
            .setDescription(
                String.format(
                    "Account added successfully. To remove it, use one of the commands:\n```/remove byname %s\n/remove bydiscordid %s```",
                    name, discordId
                )
            ).build();

        event.replyEmbeds(embed).queue();
    }
}
