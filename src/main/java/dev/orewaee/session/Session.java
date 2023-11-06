package dev.orewaee.session;

import java.util.Timer;

public record Session(String ip, Timer timer) {
    public Session(String ip) {
        this(ip, new Timer());
    }
}
