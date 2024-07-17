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
     * Gets an account manager.
     *
     * @return account manager
     */
    @NotNull AccountManager getAccountManager();

    /**
     * Gets a key manager.
     *
     * @return key manager
     */
    @NotNull KeyManager getKeyManager();

    /**
     * Gets a pool manager.
     *
     * @return pool manager
     */
    @NotNull PoolManager getPoolManager();

    /**
     * Gets a session manager.
     *
     * @return session manager
     */
    @NotNull SessionManager getSessionManager();
}
