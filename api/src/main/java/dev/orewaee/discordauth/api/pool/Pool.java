package dev.orewaee.discordauth.api.pool;

import org.jetbrains.annotations.NotNull;

import com.velocitypowered.api.proxy.Player;

public class Pool {
    private final Player player;
    private boolean status;

    public Pool(@NotNull Player player, boolean status) {
        this.player = player;
        this.status = status;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Pool[player=%s status=%s]", player.getUsername(), status);
    }
}
