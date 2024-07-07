package dev.orewaee.discordauth.velocity.discord;

import java.io.IOException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import dev.orewaee.discordauth.common.config.Config;

public class Bot {
    private final Config config;
    private final JDABuilder builder;
    private final JDA jda;

    private final static String DISCORD_ACTIVITY = "discord.activity";
    private final static String DISCORD_NAME = "discord.name";

    public Bot(Config config) throws IOException {
        this.config = config;

        String token = config.getString("discord.token", "");
        if (token.isEmpty()) throw new IOException("discord token missing");

        this.builder = JDABuilder.createDefault(token);

        applySettings();
        addEventListeners();
        applyActivity();

        this.jda = builder.build();

        addCommands();
    }

    private void applySettings() {
        builder
            .setChunkingFilter(ChunkingFilter.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES)
            .setMemberCachePolicy(MemberCachePolicy.ALL);
    }

    private void addEventListeners() {
        builder.addEventListeners(
            new DMListener(config),
            new AddCommandListener(config),
            new RemoveCommandListener(config)
        );
    }

    private void applyActivity() {
        String name = config.getString(DISCORD_NAME, "");
        if (name.isEmpty()) return;

        Activity activity = switch (config.getString(DISCORD_ACTIVITY, "")) {
            case "custom" -> Activity.customStatus(name);
            case "competing" -> Activity.competing(name);
            case "listening" -> Activity.listening(name);
            case "playing" -> Activity.playing(name);
            case "watching" -> Activity.watching(name);
            default -> null;
        };

        if (activity != null) builder.setActivity(activity);
    }

    private void addCommands() {
        jda.updateCommands().addCommands(
            Commands.slash("add", "Add new account")
                .addOption(OptionType.STRING, "name", "Account name", true)
                .addOption(OptionType.USER, "discord_id", "Account discordId", true),
            Commands.slash("remove", "Remove existing account")
                .addOption(OptionType.USER, "discord_id", "Account discordId", true)
        ).queue();
    }
}
