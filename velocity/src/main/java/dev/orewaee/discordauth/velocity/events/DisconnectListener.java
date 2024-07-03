package dev.orewaee.discordauth.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.Session;
import dev.orewaee.discordauth.api.session.SessionManager;

public class DisconnectListener {
    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    public DisconnectListener(AccountManager accountManager, KeyManager keyManager, PoolManager poolManager, SessionManager sessionManager) {
        this.accountManager = accountManager;
        this.keyManager = keyManager;
        this.poolManager = poolManager;
        this.sessionManager = sessionManager;
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

        if (pool.getStatus()) {
            Session session = sessionManager.getByAccount(account);
            if (session != null) sessionManager.removeByAccount(account);

            sessionManager.add(account, new Session(ip), () -> sessionManager.removeByAccount(account));
        }

        poolManager.removeByAccount(account);
    }
}
