package dev.orewaee.discordauth.api.key;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;

/**
 * Key manager interface.
 */
public interface KeyManager {
    /**
     * Adds a new key.
     *
     * @param account the account to which the key will be linked
     * @param key the key itself
     * @param runnable actions that will be performed when the key lifetime expires
     */
    void add(@NotNull Account account, @NotNull Key key, @Nullable Runnable runnable);

    /**
     * Removes key by account.
     *
     * @param account the account the key is linked to
     */
    void removeByAccount(@NotNull Account account);

    /**
     * Gets a key by account.
     *
     * @param account the account the key is linked to
     * @return key linked to the specified account, or {@code null} if it does not exist
     */
    @Nullable Key getByAccount(@NotNull Account account);

    /**
     * Gets a key by value.
     *
     * @param value key value
     * @return key with the specified value, or {@code null} if it does not exist
     */
    @Nullable Key getByValue(@NotNull String value);
}
