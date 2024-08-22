package dev.orewaee.discordauth.api;

import org.jetbrains.annotations.NotNull;

import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.SessionManager;

/**
 * The DiscordAuth API.
 */
public interface DiscordAuthAPI {
    /**
     * Returns the account manager.
     *
     * @return account manager
     */
    @NotNull AccountManager getAccountManager();

    /**
     * Returns the key manager.
     *
     * @return key manager
     */
    @NotNull KeyManager getKeyManager();

    /**
     * Returns the pool manager.
     *
     * @return pool manager
     */
    @NotNull PoolManager getPoolManager();

    /**
     * Returns the session manager.
     *
     * @return session manager
     */
    @NotNull SessionManager getSessionManager();
}
