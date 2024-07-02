package dev.orewaee.discordauth.common.session;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.session.Session;
import dev.orewaee.discordauth.api.session.SessionManager;

public class InMemorySessionManager implements SessionManager {
    private final long sessionLifetime;
    private final Map<Account, Session> sessions;

    public InMemorySessionManager(long sessionLifetime) {
        this.sessionLifetime = sessionLifetime;
        this.sessions = new HashMap<>();
    }

    @Override
    public void add(@NotNull Account account, @NotNull Session session, @Nullable Runnable runnable) {
        sessions.put(account, session);

        if (runnable == null) return;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };

        session.getTimer().schedule(task, sessionLifetime);
    }

    @Override
    public void removeByAccount(@NotNull Account account) {
        Session session = sessions.get(account);
        if (session != null) session.getTimer().cancel();
        sessions.remove(account);
    }

    @Override
    public @Nullable Session getByAccount(@NotNull Account account) {
        return sessions.getOrDefault(account, null);
    }

    @Override
    public String toString() {
        return sessions.toString();
    }
}
