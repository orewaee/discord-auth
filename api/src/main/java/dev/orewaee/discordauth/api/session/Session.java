package dev.orewaee.discordauth.api.session;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;

/**
 * An entity that stores the ip address of a disconnected player to restore access.
 */
public class Session {
    private final String ip;
    private final Timer timer;

    public Session(@NotNull String ip) {
        this.ip = ip;
        this.timer = new Timer();
    }

    /**
     * @return session ip
     */
    @NotNull
    public String getIp() {
        return ip;
    }

    /**
     * @return session timer
     */
    @NotNull
    public Timer getTimer() {
        return timer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Session session)) return false;

        return session.getIp().equals(ip);
    }

    @Override
    public String toString() {
        return String.format("Session[ip=%s]", ip);
    }
}
