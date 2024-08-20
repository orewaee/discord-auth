package dev.orewaee.discordauth.velocity;

import java.nio.file.Path;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.google.inject.Inject;

import org.slf4j.Logger;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.SessionManager;

import dev.orewaee.discordauth.common.config.Config;
import dev.orewaee.discordauth.common.account.JsonAccountManager;
import dev.orewaee.discordauth.common.key.InMemoryKeyManager;
import dev.orewaee.discordauth.common.pool.InMemoryPoolManager;
import dev.orewaee.discordauth.common.session.InMemorySessionManager;

import dev.orewaee.discordauth.velocity.listeners.*;
import dev.orewaee.discordauth.velocity.commands.DebugCommand;
import dev.orewaee.discordauth.velocity.commands.ReloadCommand;
import dev.orewaee.discordauth.velocity.discord.Bot;

@Plugin(
    id = "discordauth",
    name = "discord-auth",
    version = "@version",
    authors = {"orewaee"}
)
public class DiscordAuth implements DiscordAuthAPI {
    private static DiscordAuth instance;

    private final ProxyServer proxy;
    private final Logger logger;
    private final Path directory;

    private final Config config;

    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    private final Bot bot;

    @Inject
    public DiscordAuth(ProxyServer proxy, Logger logger, @DataDirectory Path directory) throws IOException {
        instance = this;

        this.proxy = proxy;
        this.logger = logger;
        this.directory = directory;

        this.config = new Config(directory.resolve("config.toml"));

        this.accountManager = new JsonAccountManager();
        this.keyManager = new InMemoryKeyManager(config);
        this.poolManager = new InMemoryPoolManager();
        this.sessionManager = new InMemorySessionManager(config);

        this.bot = new Bot(config);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        registerEvents(proxy.getEventManager());
        registerCommands(proxy.getCommandManager());
    }

    private void registerEvents(EventManager manager) {
        manager.register(this, new DisconnectListener());
        manager.register(this, new PostLoginListener(config));
        manager.register(this, new PreLoginListener(config));
        manager.register(this, new ServerPreConnectListener(config));
    }

    private void registerCommands(CommandManager manager) {
        CommandMeta debugMeta = manager.metaBuilder("debug")
            .plugin(this)
            .build();

        manager.register(debugMeta, new DebugCommand());

        CommandMeta meta = manager.metaBuilder("discordauth")
                .aliases("da")
                .plugin(this)
                .build();

        manager.register(meta, new ReloadCommand(config));
    }

    @Override
    public @NotNull AccountManager getAccountManager() {
        return accountManager;
    }

    @Override
    public @NotNull KeyManager getKeyManager() {
        return keyManager;
    }

    @Override
    public @NotNull PoolManager getPoolManager() {
        return poolManager;
    }

    @Override
    public @NotNull SessionManager getSessionManager() {
        return sessionManager;
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDirectory() {
        return directory;
    }

    public Bot getBot() {
        return bot;
    }

    public static DiscordAuth getInstance() {
        return instance;
    }
}
