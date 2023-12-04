package dev.orewaee.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.MinecraftMessages;
import dev.orewaee.config.TomlMinecraftMessages;
import dev.orewaee.session.InMemorySessionManager;
import dev.orewaee.session.Session;
import dev.orewaee.session.SessionManager;
import dev.orewaee.managers.AuthManager;
import dev.orewaee.utils.Utils;

public class PostLoginEventListener {
    private final AccountManager accountManager = JsonAccountManager.getInstance();
    private final SessionManager sessionManager = InMemorySessionManager.getInstance();

    private final MinecraftMessages minecraftMessages = TomlMinecraftMessages.getInstance();

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        String name = player.getUsername();
        String ip = player.getRemoteAddress().getHostString();

        Account account = accountManager.getAccountByName(name);

        if (account == null) return;

        Session session = sessionManager.getSessionByAccount(account);

        if (session != null) {
            sessionManager.removeSession(account);

            if (session.ip().equals(ip)) {
                // todo replace AuthManager to new singleton manager
                AuthManager.addLogged(account);

                Component message = MiniMessage.miniMessage().deserialize(
                    minecraftMessages.sessionRestored()
                );

                player.sendMessage(message);

                return;
            }

            Utils.sendAuthInstructions(player);

            return;
        }

        Utils.sendAuthInstructions(player);
    }
}
