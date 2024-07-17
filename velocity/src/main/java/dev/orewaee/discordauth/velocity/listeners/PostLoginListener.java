package dev.orewaee.discordauth.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.Key;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.Session;
import dev.orewaee.discordauth.api.session.SessionManager;

import dev.orewaee.discordauth.common.utils.Utils;
import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;
import dev.orewaee.discordauth.velocity.utils.Redirector;

public class PostLoginListener {
    private final Config config;
    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    private final static String SERVERS_REDIRECT = "servers.redirect";
    private final static String NO_ACCOUNT = "minecraft-components.no-account";
    private final static String KEY_MESSAGE = "minecraft-components.key-message";
    private final static String SESSION_RESTORED = "minecraft-components.session-restored";

    public PostLoginListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
        this.keyManager = api.getKeyManager();
        this.poolManager = api.getPoolManager();
        this.sessionManager = api.getSessionManager();
    }

    class KeyRefresher implements Runnable {
        private final Account account;

        public KeyRefresher(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            keyManager.removeByAccount(account);

            Pool pool = poolManager.getByAccount(account);
            if (pool == null || pool.getStatus()) return;

            String value = Utils.genValue();
            Key key = new Key(value);

            String message = config
                .getString(KEY_MESSAGE, "Send key <#5865f2><click:copy_to_clipboard:%key%><hover:show_text:\"Copy\">%key%</hover></click><reset> to bot")
                .replace("%name%", pool.getPlayer().getUsername())
                .replace("%key%", value);

            Component component = MiniMessage.miniMessage().deserialize(message);

            pool.getPlayer().sendMessage(component);

            keyManager.add(account, key, new KeyRefresher(account));
        }
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        String name = player.getUsername();
        String ip = player.getRemoteAddress().getHostString();

        Account account = accountManager.getByName(name);

        if (account == null) {
            String message = config
                .getString(NO_ACCOUNT, "You don't have an account")
                .replace("%name%", name);

            Component component = MiniMessage.miniMessage().deserialize(message);

            player.disconnect(component);
            return;
        }

        Pool newPool = new Pool(player, false);
        poolManager.add(account, newPool);

        Session session = sessionManager.getByAccount(account);

        if (session != null) sessionManager.removeByAccount(account);

        if (session != null && session.getIp().equals(ip)) {
            newPool.setStatus(true);

            String target = config.getString(SERVERS_REDIRECT, "");
            if (!target.isEmpty()) Redirector.redirect(player, target);

            String message = config
                .getString(SESSION_RESTORED, "<#78b159>Session restored")
                .replace("%name%", name)
                .replace("%discordid%", account.getDiscordId());

            Component component = MiniMessage.miniMessage().deserialize(message);

            player.sendMessage(component);
            return;
        }

        String value = Utils.genValue();
        Key key = new Key(value);

        String message = config
            .getString(KEY_MESSAGE, "Send key <#5865f2><click:copy_to_clipboard:%key%><hover:show_text:\"Copy\">%key%</hover></click><reset> to bot")
            .replace("%name%", name)
            .replace("%key%", value);

        Component component = MiniMessage.miniMessage().deserialize(message);

        player.sendMessage(component);

        keyManager.add(account, key, new KeyRefresher(account));
    }
}
