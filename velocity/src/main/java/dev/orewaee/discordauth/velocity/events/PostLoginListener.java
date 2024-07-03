package dev.orewaee.discordauth.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.Key;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.Session;
import dev.orewaee.discordauth.api.session.SessionManager;
import dev.orewaee.discordauth.common.utils.Utils;

public class PostLoginListener {
    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    public PostLoginListener(AccountManager accountManager, KeyManager keyManager, PoolManager poolManager, SessionManager sessionManager) {
        this.accountManager = accountManager;
        this.keyManager = keyManager;
        this.poolManager = poolManager;
        this.sessionManager = sessionManager;
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        String name = player.getUsername();
        String ip = player.getRemoteAddress().getHostString();

        Account account = accountManager.getByName(name);

        if (account == null) {
            Component reason = Component.text("You don't have an account");
            player.disconnect(reason);
            return;
        }

        Pool pool = new Pool(player, false);
        poolManager.add(account, pool);

        Session session = sessionManager.getByAccount(account);

        if (session != null) {
            sessionManager.removeByAccount(account);

            if (session.getIp().equals(ip)) {
                pool.setStatus(true);
                player.sendMessage(Component.text("Session restored"));
                return;
            }
        }

        Key key = new Key(Utils.genValue("abcdefghijklmnopqrstuvwxyz"));
        pool.getPlayer().sendMessage(Component.text(key.getValue()));

        class Actions implements Runnable {
            @Override
            public void run() {
                keyManager.removeByAccount(account);

                Pool p = poolManager.getByAccount(account);
                if (p == null || p.getStatus()) return;

                Key k = new Key(Utils.genValue("abcdefghijklmnopqrstuvwxyz"));
                p.getPlayer().sendMessage(Component.text(k.getValue()));
                keyManager.add(account, k, new Actions());
            }
        }

        keyManager.add(account, key, new Actions());
    }
}
