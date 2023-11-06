package dev.orewaee.events;

import com.velocitypowered.api.event.ResultedEvent.ComponentResult;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.*;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent.ServerResult;
import com.velocitypowered.api.proxy.Player;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.config.TomlConfig;
import dev.orewaee.session.Session;
import dev.orewaee.session.SessionManager;
import dev.orewaee.utils.AuthManager;
import dev.orewaee.utils.ServerManager;
import dev.orewaee.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class EventsListener {
    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        System.out.println("ServerPreConnectEvent");

        Player player = event.getPlayer();
        String name = player.getUsername();

        boolean accountExists = AccountManager.containsAccountByName(name);

        if (!accountExists) event.setResult(ServerResult.denied());

        RegisteredServer originalServer = event.getOriginalServer();

        if (!AuthManager.isLogged(name) && !originalServer.equals(ServerManager.getLobby())) {
            event.setResult(ServerResult.denied());

            Component message = MiniMessage.miniMessage().deserialize(TomlConfig.getAuthFirstMessage());

            player.sendMessage(message);
        }
    }

    @Subscribe
    public void onServerConnected(ServerConnectedEvent event) {
        System.out.println("ServerConnectedEvent");
    }

    @Subscribe
    public void onServerPostConnect(ServerPostConnectEvent event) {
        System.out.println("onServerPostConnect");
    }

    @Subscribe
    public void onConnectionHandshake(ConnectionHandshakeEvent event) {
        System.out.println("ConnectionHandshakeEvent");
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        System.out.println("PreLoginEvent");

        String name = event.getUsername();
        String ip = event.getConnection().getRemoteAddress().getHostString();

        if (!AccountManager.containsAccountByName(name)) return;

        Account account = AccountManager.getAccountByName(name);

        System.out.printf("ip = %s\n", ip);

        boolean sessionExists = SessionManager.containsSession(account);

        if (sessionExists) {

        }
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        System.out.println("LoginEvent");

        Player player = event.getPlayer();
        String name = player.getUsername();
        String ip = player.getRemoteAddress().getHostString();

        Account account = AccountManager.getAccountByName(name);

        if (account == null) {
            Component component = MiniMessage.miniMessage().deserialize(TomlConfig.getMissingAccountMessage());
            ComponentResult result = ComponentResult.denied(component);

            event.setResult(result);

            return;
        }

        Session session = SessionManager.getSessionByAccount(account);

        if (session != null) {


            SessionManager.removeSession(account);

            if (ip.equals(session.ip())) {
                AuthManager.addLogged(name);

                Component message = MiniMessage.miniMessage().deserialize(TomlConfig.getSessionRestoredMessage());

                player.sendMessage(message);

                return;
            }

            Utils.sendAuthInstructions(player);

            return;
        }

        Utils.sendAuthInstructions(player);
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        System.out.println("PostLoginEvent");
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        System.out.println("DisconnectEvent");

        Player player = event.getPlayer();
        String name = player.getUsername();
        String ip = player.getRemoteAddress().getHostString();

        if (!AccountManager.containsAccountByName(name)) return;

        Account account = AccountManager.getAccountByName(name);

        if (AuthManager.isLogged(name)) {
            Session session = SessionManager.getSessionByAccount(account);

            if (session != null) SessionManager.removeSession(account);

            AuthManager.removeLogged(name);
            SessionManager.addSession(account, new Session(ip));
        }

        AuthManager.removeLogged(name);
    }

    @Subscribe
    public void onPlayerChooseInitialServer(PlayerChooseInitialServerEvent event) {
        System.out.println("PlayerChooseInitialServerEvent");
    }
}
