package dev.orewaee.discordauth.velocity.listeners;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent.ServerResult;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class ServerPreConnectListener {
    private final Config config;
    private final AccountManager accountManager;
    private final PoolManager poolManager;

    private final static String SERVERS_LIMBO = "servers.limbo";
    private final static String AUTH_FIRST = "minecraft-components.auth-first";

    public ServerPreConnectListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
        this.poolManager = api.getPoolManager();
    }

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        String name = player.getUsername();

        Account account = accountManager.getByName(name);

        if (account == null) {
            event.setResult(ServerResult.denied());
            return;
        }

        RegisteredServer original = event.getOriginalServer();
        RegisteredServer limbo = DiscordAuth.getInstance()
            .getProxy()
            .getServer(config.getString(SERVERS_LIMBO, "limbo"))
            .orElse(null);

        Pool pool = poolManager.getByAccount(account);

        boolean validPool = pool != null && pool.getStatus();
        if (validPool || original.equals(limbo)) return;

        ServerResult result = ServerPreConnectEvent.ServerResult.denied();
        event.setResult(result);

        String message = config
            .getString(AUTH_FIRST, "<#dd2e44>Auth first")
            .replace("%name%", name);

        Component component = MiniMessage.miniMessage().deserialize(message);

        player.sendMessage(component);
    }
}
