package dev.orewaee.discordauth.velocity.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {
    private final JDABuilder builder;
    private final JDA jda;

    public Bot(String token) {
        this.builder = JDABuilder
            .createDefault(token)
            .setChunkingFilter(ChunkingFilter.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .addEventListeners(new DMListener());

        this.jda = builder.build();
    }
}
