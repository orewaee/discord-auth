package dev.orewaee.discordauth.velocity.discord;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class SyncCommandListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;

    public SyncCommandListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("sync")) return;

        int total = 0;
        int successful = 0;

        List<Guild> guilds = event.getJDA().getGuilds();
        for (Guild guild : guilds) {
            for (Member member : guild.getMembers()) {
                Account account = accountManager.getByDiscordId(member.getId());
                if (account == null) continue;

                String name = member.getNickname() != null ?
                    member.getNickname() : member.getUser().getName();

                if (account.getName().equals(name)) continue;

                try {
                    member.modifyNickname(account.getName()).queue();
                    successful++;
                } catch (Exception exception) {
                    System.out.printf(
                        "Failed to sync account %s#%s (%s)\n",
                        account.getName(), account.getDiscordId(), exception.getMessage()
                    );
                } finally {
                    total++;
                }
            }
        }

        String message = String.format("Done. Guilds: %d. Total: %s. Successful: %s.", guilds.size(), total, successful);
        event.reply(message).queue();
    }
}
