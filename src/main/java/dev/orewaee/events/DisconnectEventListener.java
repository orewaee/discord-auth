package dev.orewaee.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.session.InMemorySessionManager;
import dev.orewaee.session.Session;
import dev.orewaee.session.SessionManager;
import dev.orewaee.managers.AuthManager;

public class DisconnectEventListener {
    private final AccountManager accountManager = JsonAccountManager.getInstance();
    private final SessionManager sessionManager = InMemorySessionManager.getInstance();

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        String name = player.getUsername();
        String ip = player.getRemoteAddress().getHostString();

        Account account = accountManager.getAccountByName(name);

        if (AuthManager.isLogged(account)) {
            Session session = sessionManager.getSessionByAccount(account);

            if (session != null) sessionManager.removeSession(account);

            AuthManager.removeLogged(account);
            sessionManager.addSession(account, new Session(ip));
        }

        AuthManager.removeLogged(account);
    }
}
