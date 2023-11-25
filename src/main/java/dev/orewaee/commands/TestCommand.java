package dev.orewaee.commands;

import java.util.List;
import java.util.Map;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import net.kyori.adventure.text.Component;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.key.InMemoryKeyManager;
import dev.orewaee.key.Key;
import dev.orewaee.key.KeyManager;
import dev.orewaee.session.Session;
import dev.orewaee.session.InMemorySessionManager;
import dev.orewaee.session.SessionManager;
import dev.orewaee.managers.AuthManager;
import dev.orewaee.managers.ServerManager;

public class TestCommand implements RawCommand {
    private final AccountManager accountManager = JsonAccountManager.getInstance();
    private final KeyManager keyManager = InMemoryKeyManager.getInstance();
    private final SessionManager sessionManager = InMemorySessionManager.getInstance();

    @Override
    public List<String> suggest(Invocation invocation) {
        return RawCommand.super.suggest(invocation);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("ACCOUNTS:"));

        for (Account account : accountManager.getAccounts()) {
            String content = account.name() + " / " + account.discordId();

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

        Map<Account, Key> keys = keyManager.getKeys();

        for (Account account : keys.keySet()) {
            Key key = keys.get(account);

            Component content = Component.text(account.name() + "#" + account.discordId() + " / " + key.code());

            source.sendMessage(content);
        }

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("LOGGED PLAYERS:"));

        for (Account loggedAccount : AuthManager.getLoggedAccounts()) {
            Component content = Component.text(loggedAccount.toString());

            source.sendMessage(content);
        }

        source.sendMessage(Component.empty());
        source.sendMessage(Component.text("SESSIONS:"));

        Map<Account, Session> sessions = sessionManager.getSessions();

        for (Account account : sessions.keySet()) {
            Session session = sessions.get(account);

            Component content = Component.text(account.name() + "#" + account.discordId() + " / " + session.ip());

            source.sendMessage(content);
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("discordauth.test");
    }
}
