package dev.orewaee.discordauth.api.account;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Account manager interface.
 */
public interface AccountManager {
    /**
     * Adds a new account.
     *
     * @param account account to be added
     */
    void add(@NotNull Account account);

    /**
     * Removes account by name.
     *
     * @param name name of the account to be removed
     */
    void removeByName(@NotNull String name);

    /**
     * Removes account by discordId.
     *
     * @param discordId discordId of the account to be removed
     */
    void removeByDiscordId(@NotNull String discordId);

    /**
     * Gets an account by name.
     *
     * @param name name of the account
     *
     * @return an account with the specified name, or {@code null} if it does not exist
     */
    @Nullable Account getByName(@NotNull String name);

    /**
     * Gets an account by discordId.
     *
     * @param discordId discordId of the account
     *
     * @return an account with the specified discordId, or {@code null} if it does not exist
     */
    @Nullable Account getByDiscordId(@NotNull String discordId);

    /**
     * Checks for the existence of an account by name.
     *
     * @param name name of the account
     *
     * @return boolean value of existence of an account with the specified name
     */
    boolean containsByName(@NotNull String name);

    /**
     * Checks for the existence of an account by discordId.
     *
     * @param discordId discordId of the account
     *
     * @return boolean value of existence of an account with the specified discordId
     */
    boolean containsByDiscordId(@NotNull String discordId);
}
