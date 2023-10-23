package dev.orewaee.session;

import dev.orewaee.config.TomlConfig;

import java.util.Timer;
import java.util.TimerTask;

public class Session {
    private final String name, ip;
    private final Timer timer = new Timer();

    public Session(String name, String ip) {
        this.name = name;
        this.ip = ip;

        startTimer();
    }

    public void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                SessionManager.removeSession(name, ip);
            }
        };

        long time = TomlConfig.getSessionExpirationTime();

        timer.schedule(task, time * 1000);
    }

    public void stopTimer() {
        timer.cancel();
    }

    public void restartTimer() {
        stopTimer();
        startTimer();
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public Timer getTimer() {
        return timer;
    }
}
