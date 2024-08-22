package dev.orewaee.discordauth.api.key;

import java.util.Timer;

import org.jetbrains.annotations.NotNull;

/**
 * An auth key required to confirm that the account belongs to the player.
 */
public class Key {
    private final String value;
    private final Timer timer;

    public Key(@NotNull String value) {
        this.value = value;
        this.timer = new Timer();
    }

    /**
     * @return key value
     */
    @NotNull
    public String getValue() {
        return value;
    }

    /**
     * @return key timer
     */
    @NotNull
    public Timer getTimer() {
        return timer;
    }

    @Override
    public String toString() {
        return String.format("Key[value=%s]", value);
    }
}
