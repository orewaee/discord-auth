package dev.orewaee.session;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import dev.orewaee.account.Account;
import dev.orewaee.config.TomlConfig;
import org.jetbrains.annotations.Nullable;

public class SessionManager {
    private static final Map<Account, Session> sessions = new HashMap<>();

    public static void addSession(Account account, Session session) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SessionManager.removeSession(account);
                System.out.println("session removed");
            }
        };

        session.timer().schedule(task, 1000 * TomlConfig.getSessionExpirationTime());

        sessions.put(account, session);
    }

    public static void removeSession(Account account) {
        Session session = sessions.get(account);

        if (session == null) return;

        session.timer().cancel();

        sessions.remove(account);
    }

    public static boolean containsSession(Account account) {
        return sessions.containsKey(account);
    }

    @Nullable
    public static Session getSessionByAccount(Account account) {
        if (!containsSession(account)) return null;

        return sessions.get(account);
    }

    public static boolean containsSessionByIp(String ip) {
        for (Account account : sessions.keySet())
            if (ip.equals(sessions.get(account).ip()))
                return true;

        return false;
    }

    public static Map<Account, Session> getSessions() {
        return sessions;
    }
}
