package dev.orewaee.discordauth.api.session;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;

public class Session {
    private final String ip;
    private final Timer timer;

    public Session(@NotNull String ip) {
        this.ip = ip;
        this.timer = new Timer();
    }

    @NotNull
    public String getIp() {
        return ip;
    }

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
