package dev.orewaee.discordauth.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.Session;
import dev.orewaee.discordauth.api.session.SessionManager;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class DisconnectListener {
    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    public DisconnectListener() {
        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
        this.keyManager = api.getKeyManager();
        this.poolManager = api.getPoolManager();
        this.sessionManager = api.getSessionManager();
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        String name = player.getUsername();
        String ip = player.getRemoteAddress().getHostString();

        Account account = accountManager.getByName(name);
        if (account == null) return;

        Pool pool = poolManager.getByAccount(account);
        if (pool == null) return;

        keyManager.removeByAccount(account);
        poolManager.removeByAccount(account);

        if (!pool.getStatus()) return;

        Session session = sessionManager.getByAccount(account);
        if (session != null) sessionManager.removeByAccount(account);

        sessionManager.add(account, new Session(ip), () -> sessionManager.removeByAccount(account));
    }
}
