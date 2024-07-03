package dev.orewaee.discordauth.velocity;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.google.inject.Inject;

import org.slf4j.Logger;

import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.proxy.ProxyServer;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.common.account.JsonAccountManager;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.common.key.InMemoryKeyManager;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.common.pool.InMemoryPoolManager;
import dev.orewaee.discordauth.api.session.SessionManager;
import dev.orewaee.discordauth.common.session.InMemorySessionManager;
import dev.orewaee.discordauth.velocity.events.DisconnectListener;
import dev.orewaee.discordauth.velocity.events.PostLoginListener;
import dev.orewaee.discordauth.velocity.events.PreLoginListener;
import dev.orewaee.discordauth.velocity.events.ServerPreConnectListener;

@Plugin(id = "discordauth", name = "discord-auth", version = "0.4.0", authors = {"orewaee"})
public class DiscordAuth implements DiscordAuthAPI {
    private static DiscordAuth instance;

    private final ProxyServer proxy;
    private final Logger logger;

    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    @Inject
    public DiscordAuth(ProxyServer proxy, Logger logger) throws IOException {
        instance = this;

        this.proxy = proxy;
        this.logger = logger;

        this.accountManager = new JsonAccountManager();
        this.keyManager = new InMemoryKeyManager(1000 * 10);
        this.poolManager = new InMemoryPoolManager();
        this.sessionManager = new InMemorySessionManager(1000 * 60);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        registerEvents(proxy.getEventManager());
    }

    private void registerEvents(EventManager manager) {
        manager.register(this, new DisconnectListener(accountManager, keyManager, poolManager, sessionManager));
        manager.register(this, new PostLoginListener(accountManager, keyManager, poolManager, sessionManager));
        manager.register(this, new PreLoginListener(accountManager));
        manager.register(this, new ServerPreConnectListener(accountManager, poolManager));
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

    public static DiscordAuth getInstance() {
        return instance;
    }
}
