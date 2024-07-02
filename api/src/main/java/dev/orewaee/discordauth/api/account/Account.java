package dev.orewaee.discordauth.api.account;

import org.jetbrains.annotations.NotNull;

/**
 * An entity containing the name and discordId of the player.
 */
public class Account {
    private final String name;
    private final String discordId;

    public Account(@NotNull String name, @NotNull String discordId) {
        this.name = name;
        this.discordId = discordId;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getDiscordId() {
        return discordId;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + discordId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Account account)) return false;

        return account.getName().equals(name) && account.getDiscordId().equals(discordId);
    }

    @Override
    public String toString() {
        return String.format("Account[name=%s, discordId=%s]", name, discordId);
    }
}
