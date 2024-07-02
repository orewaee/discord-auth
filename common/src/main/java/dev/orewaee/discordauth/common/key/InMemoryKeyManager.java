package dev.orewaee.discordauth.common.key;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.key.Key;
import dev.orewaee.discordauth.api.key.KeyManager;

public class InMemoryKeyManager implements KeyManager {
    private final long keyLifetime;
    private final Map<Account, Key> keys;

    public InMemoryKeyManager(long keyLifetime) {
        this.keyLifetime = keyLifetime;
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

        key.getTimer().schedule(task, keyLifetime);
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
