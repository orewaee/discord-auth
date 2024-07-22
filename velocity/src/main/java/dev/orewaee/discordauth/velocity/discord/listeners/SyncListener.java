package dev.orewaee.discordauth.velocity.discord.listeners;

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
import dev.orewaee.discordauth.velocity.discord.utils.PermissionUtils;

public class SyncListener extends ListenerAdapter {
    private final Config config;
    private final AccountManager accountManager;
    private final PermissionUtils permissionUtils;

    private final static String NO_PERMISSION = "discord-components.no-permission";
    private final static String SYNC_MESSAGE = "discord-components.sync-message";

    public SyncListener(Config config, PermissionUtils permissionUtils) {
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

        String message = config
            .getString(SYNC_MESSAGE, "Done. Guilds: %guilds%. Total: %total%. Successful: %successful%.")
            .replace("%guilds%", guilds.size() + "")
            .replace("%total%", total + "")
            .replace("%successful%", successful + "");

        event.reply(message).queue();
    }
}
