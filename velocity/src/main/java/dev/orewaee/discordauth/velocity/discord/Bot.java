package dev.orewaee.discordauth.velocity.discord;

import java.io.IOException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.discord.listeners.*;
import dev.orewaee.discordauth.velocity.discord.utils.PermissionUtils;

public class Bot {
    private final Config config;

    private final JDABuilder builder;
    private final JDA jda;

    private final PermissionUtils permissionUtils;

    private final static String DISCORD_ACTIVITY = "discord.activity";
    private final static String DISCORD_NAME = "discord.name";

    public Bot(Config config) throws IOException {
        this.config = config;
        this.permissionUtils = new PermissionUtils(config);

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
            new AddListener(config, permissionUtils),
            new RemoveByNameListener(config, permissionUtils),
            new RemoveByDiscordIdListener(config, permissionUtils),
            new ListListener(config, permissionUtils),
            new SyncListener(config, permissionUtils)
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
        CommandData add = Commands.slash("add", "Add new account")
            .addOption(OptionType.STRING, "name", "Account name", true)
            .addOption(OptionType.USER, "discordid", "Account discordid", true);

        CommandData remove = Commands.slash("remove", "Remove existing account")
            .addSubcommands(
                new SubcommandData("byname", "Remove account by name")
                    .addOption(OptionType.STRING, "name", "Account name", true),
                new SubcommandData("bydiscordid", "Remove account by discordid")
                    .addOption(OptionType.USER, "discordid", "Account discordid", true)
            );

        CommandData list = Commands.slash("list", "List of all accounts");

        CommandData sync = Commands.slash("sync", "Sync guild member names with accounts");

        jda.updateCommands().addCommands(add, remove, list, sync).queue();
    }

    public JDA getJda() {
        return jda;
    }
}
