package dev.orewaee.discordauth.velocity.discord;

import java.io.IOException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import dev.orewaee.discordauth.common.config.Config;

public class Bot {
    private final JDABuilder builder;
    private final JDA jda;

    public Bot(Config config) throws IOException {
        String token = config.getString("discord.token", "");

        if (token.isEmpty()) throw new IOException("discord token missing");

        this.builder = JDABuilder
            .createDefault(token)
            .setChunkingFilter(ChunkingFilter.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .addEventListeners(
                new DMListener(config),
                new AddCommandListener(config)
            );

        this.jda = builder.build();

        jda.updateCommands().addCommands(
            Commands.slash("add", "Add new account")
                .addOption(OptionType.STRING, "name", "Account name", true)
                .addOption(OptionType.USER, "discord_id", "Account discordId", true)
        ).queue();
    }
}
