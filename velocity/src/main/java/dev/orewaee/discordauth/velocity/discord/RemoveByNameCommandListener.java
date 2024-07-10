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

public class RemoveByNameCommandListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;

    private final static String DISCORD_IDS = "discord.ids";

    public RemoveByNameCommandListener(Config config) {
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

        if (!event.getFullCommandName().equals("remove byname")) return;

        OptionMapping nameMapping = event.getOption("name");
        if (nameMapping == null) return;
        String name = nameMapping.getAsString();

        Account account = accountManager.getByName(name);

        if (account == null) {
            event.reply("There is no account with this name").queue();
            return;
        }

        accountManager.removeByName(name);

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0xdd2e44)
            .setAuthor(account.getName(), null, "https://mc-heads.net/avatar/" + account.getName())
            .setTitle(":red_square: Account removed")
            .setDescription(
                String.format(
                    "Account removed successfully. To add it, use the command:\n```/add %s %s```",
                    name, account.getDiscordId()
                )
            ).build();

        event.replyEmbeds(embed).queue();
    }
}
