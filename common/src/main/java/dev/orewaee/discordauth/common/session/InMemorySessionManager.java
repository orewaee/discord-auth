package dev.orewaee.discordauth.common.session;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.session.Session;
import dev.orewaee.discordauth.api.session.SessionManager;
import dev.orewaee.discordauth.common.config.Config;

public class InMemorySessionManager implements SessionManager {
    private final Config config;
    private final Map<Account, Session> sessions;

    private final static String SESSIONS_LIFETIME = "sessions.lifetime";

    public InMemorySessionManager(Config config) {
        this.config = config;
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

        long delay = 1000 * config.getLong(SESSIONS_LIFETIME, 60);
        session.getTimer().schedule(task, delay);
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
