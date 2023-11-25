package dev.orewaee.session;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import org.jetbrains.annotations.Nullable;

import dev.orewaee.account.Account;
import dev.orewaee.config.TomlConfig;

public class InMemorySessionManager implements SessionManager {
    private static SessionManager instance;

    private final Map<Account, Session> sessions = new HashMap<>();

    @Override
    public void addSession(Account account, Session session) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                InMemorySessionManager
                    .getInstance()
                    .removeSession(account);
            }
        };

        long time = TomlConfig.getSessionExpirationTime() * 1000;

        session.timer().schedule(task, time);

        sessions.put(account, session);
    }

    @Override
    public void removeSession(Account account) {
        Session session = sessions.get(account);

        if (session == null) return;

        session.timer().cancel();

        sessions.remove(account);
    }

    @Override
    public boolean containsSessionByAccount(Account account) {
        return sessions.containsKey(account);
    }

    @Override
    public boolean containsSessionByIp(String ip) {
        for (Account account : sessions.keySet())
            if (sessions.get(account).ip().equals(ip))
                return true;

        return false;
    }

    @Override
    @Nullable
    public Session getSessionByAccount(Account account) {
        return sessions.get(account);
    }

    @Override
    public Map<Account, Session> getSessions() {
        return sessions;
    }

    public static SessionManager getInstance() {
        if (instance == null) instance = new InMemorySessionManager();

        return instance;
    }
}
