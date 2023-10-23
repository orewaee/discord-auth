package dev.orewaee.key;

import java.util.Timer;
import java.util.TimerTask;

import com.velocitypowered.api.proxy.Player;

import dev.orewaee.config.TomlConfig;
import dev.orewaee.utils.*;

public class Key {
    private final String name, key;
    private final Timer timer = new Timer();

    public Key(String name) {
        this.name = name;
        this.key = Utils.generateKey();

        startTimer();
    }

    public void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                KeyManager.removeKeyByName(name);

                if (AuthManager.isLogged(name)) return;

                Player player = ServerManager.getProxy().getPlayer(name).orElse(null);

                if (player == null) return;

                Utils.sendAuthInstructions(player);
            }
        };

        long time = TomlConfig.getKeyExpirationTime();

        timer.schedule(task, time * 1000);
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public Timer getTimer() {
        return timer;
    }
}
