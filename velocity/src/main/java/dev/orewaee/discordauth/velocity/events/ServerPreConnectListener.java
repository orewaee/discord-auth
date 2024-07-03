package dev.orewaee.discordauth.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent.ServerResult;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.velocity.DiscordAuth;

public class ServerPreConnectListener {
    private final AccountManager accountManager;
    private final PoolManager poolManager;

    public ServerPreConnectListener(AccountManager accountManager, PoolManager poolManager) {
        this.accountManager = accountManager;
        this.poolManager = poolManager;
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
            .getServer("limbo")
            .orElse(null);

        Pool pool = poolManager.getByAccount(account);

        boolean validPool = pool != null && pool.getStatus();

        if (validPool || original.equals(limbo)) return;

        event.setResult(ServerPreConnectEvent.ServerResult.denied());
        player.sendMessage(Component.text("auth first"));
    }
}
