package dev.orewaee.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {
    public Bot(String token) {
        JDABuilder jdaBuilder = JDABuilder
            .createDefault(token)
            .setChunkingFilter(ChunkingFilter.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .addEventListeners(new EventsListener())
            .addEventListeners(new AddCommand())
            .addEventListeners(new RemoveCommand())
            .addEventListeners(new AccountsCommand())
            .addEventListeners(new SyncCommand());

        JDA jda = jdaBuilder.build();

        jda.updateCommands().addCommands(
            Commands.slash("add", "Add account")
                .addOption(
                    OptionType.STRING,
                    "name",
                    "Account name",
                    true
                )
                .addOption(
                    OptionType.STRING,
                    "discord_id",
                    "Account discord id",
                    true
                ),
            Commands.slash("remove", "Remove account")
                .addOption(
                    OptionType.STRING,
                    "name",
                    "Account name",
                    true
                )
                .addOption(
                    OptionType.STRING,
                    "discord_id",
                    "Account discord id",
                    true
                ),
            Commands.slash("accounts", "Displays a list of all accounts"),
            Commands.slash("sync", "Sync names in all guilds")
        ).queue();
    }
}
