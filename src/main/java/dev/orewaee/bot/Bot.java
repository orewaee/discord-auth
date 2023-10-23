package dev.orewaee.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
    public Bot(String token) {
        JDABuilder jdaBuilder = JDABuilder
            .createDefault(token)
            .enableIntents(GatewayIntent.DIRECT_MESSAGES)
            .addEventListeners(new EventsListener());

        JDA jda = jdaBuilder.build();
    }
}
