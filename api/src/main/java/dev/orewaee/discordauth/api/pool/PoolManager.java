package dev.orewaee.discordauth.api.pool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;

/**
 * Pool manager interface.
 */
public interface PoolManager {
    /**
     * Adds a new pool.
     *
     * @param account the account to which the pool will be linked
     * @param pool the pool itself
     */
    void add(@NotNull Account account, @NotNull Pool pool);

    /**
     * Removes pool by account.
     *
     * @param account the account the pool is linked to
     */
    void removeByAccount(@NotNull Account account);

    /**
     * Returns the pool bound to the specified account, or null if it does not exist.
     *
     * @param account the account the pool is linked to
     * @return pool linked to the specified account, or {@code null} if it does not exist
     */
    @Nullable Pool getByAccount(@NotNull Account account);
}
