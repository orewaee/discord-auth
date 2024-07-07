package dev.orewaee.discordauth.velocity.discord;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.common.config.Config;
import dev.orewaee.discordauth.velocity.DiscordAuth;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public class RemoveCommandListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;

    public RemoveCommandListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("remove")) return;

        OptionMapping discordIdMapping = event.getOption("discord_id");
        if (discordIdMapping == null) return;
        String discordId = discordIdMapping.getAsString();

        Account account = accountManager.getByDiscordId(discordId);

        if (account == null) {
            event.reply("There is no account with this discordId").queue();
            return;
        }

        accountManager.removeByDiscordId(discordId);

        MessageEmbed embed = new EmbedBuilder()
            .setColor(0xdd2e44)
            .setAuthor(account.getName(), null, "https://mc-heads.net/avatar/" + account.getName())
            .setTitle(":red_square: Account removed")
            .setDescription(
                String.format(
                    "Account removed successfully. To add it, use the command:\n```/add %s %s```",
                    account.getName(), discordId
                )
            ).build();

        event.replyEmbeds(embed).queue();
    }
}
