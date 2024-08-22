package dev.orewaee.discordauth.api.pool;

import org.jetbrains.annotations.NotNull;

import com.velocitypowered.api.proxy.Player;

/**
 * An entity containing the player and his auth status.
 */
public class Pool {
    private final Player player;
    private boolean status;

    public Pool(@NotNull Player player, boolean status) {
        this.player = player;
        this.status = status;
    }

    /**
     * @return pool player
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * @return pool status
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Sets the status of the pool.
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Pool[player=%s status=%s]", player.getUsername(), status);
    }
}
