package dev.orewaee.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.key.Key;
import dev.orewaee.key.KeyManager;
import dev.orewaee.session.Session;
import dev.orewaee.session.SessionManager;
import dev.orewaee.utils.AuthManager;
import dev.orewaee.utils.ServerManager;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public class TestCommand implements RawCommand {
    @Override
    public List<String> suggest(Invocation invocation) {
        return RawCommand.super.suggest(invocation);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("ACCOUNTS:"));

        for (Account account : AccountManager.getAccounts()) {
            String content = account.name() + " / " + account.discord();

            source.sendMessage(Component.text(content));
        }

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("SERVERS:"));

        for (RegisteredServer server : ServerManager.getServers()) {
            ServerInfo info = server.getServerInfo();

            Component content = Component.text(info.getName() + " / " + info.getAddress());

            source.sendMessage(content);
        }

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("KEYS:"));

        Map<Account, Key> keys = KeyManager.getKeys();

        for (Account account : keys.keySet()) {
            Key key = keys.get(account);

            Component content = Component.text(account.name() + "#" + account.discord() + " / " + key.code());

            source.sendMessage(content);
        }

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("LOGGED PLAYERS:"));

        for (String loggedPlayer : AuthManager.getLoggedPlayers()) {
            Component content = Component.text(loggedPlayer);

            source.sendMessage(content);
        }

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("SESSIONS:"));

        Map<Account, Session> sessions = SessionManager.getSessions();

        for (Account account : sessions.keySet()) {
            Session session = sessions.get(account);

            Component content = Component.text(account.name() + "#" + account.discord() + " / " + session.ip());

            source.sendMessage(content);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("discordauth.test");
    }
}
