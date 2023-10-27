package dev.orewaee.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
    public Bot(String token) {
        JDABuilder jdaBuilder = JDABuilder
            .createDefault(token)
            .enableIntents(GatewayIntent.DIRECT_MESSAGES)
            .addEventListeners(new EventsListener())
            .addEventListeners(new AddCommand())
            .addEventListeners(new RemoveCommand());

        JDA jda = jdaBuilder.build();

        jda.updateCommands().addCommands(
            Commands.slash("add", "Add account")
                .addOption(
                    OptionType.MENTIONABLE,
                    "discord",
                    "Account discord",
                    true
                )
                .addOption(
                    OptionType.STRING,
                    "name",
                    "Account name",
                    true
                ),
            Commands.slash("remove", "Remove account")
                .addOption(
                    OptionType.MENTIONABLE,
                    "discord",
                    "Account discord",
                    true
                )
                .addOption(
                    OptionType.STRING,
                    "name",
                    "Account name",
                    true
                )
        ).queue();
    }
}
