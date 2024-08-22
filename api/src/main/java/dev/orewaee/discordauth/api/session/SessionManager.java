package dev.orewaee.discordauth.api.session;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;

/**
 * Session manager interface.
 */
public interface SessionManager {
    /**
     * Adds a new session.
     *
     * @param account the account to which the session will be linked
     * @param session the pool itself
     * @param runnable actions that will be performed when the session lifetime expires
     */
    void add(@NotNull Account account, @NotNull Session session, @Nullable Runnable runnable);

    /**
     * Removes session by account.
     *
     * @param account the account the session is linked to
     */
    void removeByAccount(@NotNull Account account);

    /**
     * Gets session by account
     *
     * @param account the account the session is linked to
     * @return session linked to the specified account, or {@code null} if it does not exist
     */
    @Nullable Session getByAccount(@NotNull Account account);
}
