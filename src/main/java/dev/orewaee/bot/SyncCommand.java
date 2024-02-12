package dev.orewaee.bot;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.Config;
import dev.orewaee.config.TomlConfig;

public class SyncCommand extends ListenerAdapter {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    private final Config config = TomlConfig.getInstance();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!config.adminDiscordId().contains(event.getUser().getId())) return;

        if (!event.getName().equals("sync")) return;

        int successful = 0;
        int unsuccessful = 0;

        List<Guild> guilds = event.getJDA().getGuilds();
        for (Guild guild : guilds) {
            for (Member member : guild.getMembers()) {

                Account account = accountManager.getAccountByDiscordId(member.getId());

                if (account == null) continue;

                String name = member.getNickname() != null ?
                    member.getNickname() : member.getUser().getName();

                if (account.name().equals(name)) continue;

                try {
                    member.modifyNickname(account.name()).queue();

                    successful++;
                } catch (Exception exception) {
                    System.out.printf(
                        "Failed to sync account %s#%s (%s)\n",
                        account.name(), account.discordId(), exception.getMessage()
                    );

                    unsuccessful++;
                }
            }
        }

        event.reply(
            String.format(
                "Done. Guilds: %d. Successful: %s. Unsuccessful: %s.",
                guilds.size(), successful, unsuccessful
            )
        ).queue();
    }
}
