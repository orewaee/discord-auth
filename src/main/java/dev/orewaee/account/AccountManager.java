package dev.orewaee.account;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

public interface AccountManager {
    void addAccount(String name, String discordId);
    void removeAccount(String name, String discordId);

    @Nullable
    Account getAccountByName(String name);

    @Nullable
    Account getAccountByDiscordId(String discordId);

    boolean containsAccount(Account account);
    boolean containsAccount(String name, String discordId);

    boolean containsAccountByName(String name);
    boolean containsAccountByDiscordId(String discordId);

    Set<Account> getAccounts();
}
