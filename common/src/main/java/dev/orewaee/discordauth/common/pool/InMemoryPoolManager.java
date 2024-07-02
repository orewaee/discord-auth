package dev.orewaee.discordauth.common.pool;

import java.util.Map;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;

public class InMemoryPoolManager implements PoolManager {
    private final Map<Account, Pool> pools;

    public InMemoryPoolManager() {
        this.pools = new HashMap<>();
    }

    @Override
    public void add(@NotNull Account account, @NotNull Pool pool) {
        pools.put(account, pool);
    }

    @Override
    public void removeByAccount(@NotNull Account account) {
        pools.remove(account);
    }

    @Override
    public @Nullable Pool getByAccount(@NotNull Account account) {
        return pools.getOrDefault(account, null);
    }

    @Override
    public String toString() {
        return pools.toString();
    }
}
