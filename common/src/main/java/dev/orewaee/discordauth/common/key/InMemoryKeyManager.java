package dev.orewaee.discordauth.common.key;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.key.Key;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.common.config.Config;

public class InMemoryKeyManager implements KeyManager {
    private final Config config;
    private final Map<Account, Key> keys;

    private static final String KEYS_LIFETIME = "keys.lifetime";

    public InMemoryKeyManager(Config config) {
        this.config = config;
        this.keys = new HashMap<>();
    }

    @Override
    public void add(@NotNull Account account, @NotNull Key key, @Nullable Runnable runnable) {
        keys.put(account, key);

        if (runnable == null) return;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };

        long delay = 1000 * config.getLong(KEYS_LIFETIME, 10);
        key.getTimer().schedule(task, delay);
    }

    @Override
    public void removeByAccount(@NotNull Account account) {
        keys.remove(account);
    }

    @Override
    public @Nullable Key getByAccount(@NotNull Account account) {
        return keys.getOrDefault(account, null);
    }

    @Override
    public @Nullable Key getByValue(@NotNull String value) {
        for (Key key : keys.values())
            if (key.getValue().equals(value))
                return key;

        return null;
    }

    @Override
    public String toString() {
        return keys.toString();
    }
}
