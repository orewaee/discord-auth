package dev.orewaee.managers;

import java.util.HashSet;
import java.util.Set;

import dev.orewaee.account.Account;

public class AuthManager {
    private static final Set<Account> loggedAccounts = new HashSet<>();

    public static boolean isLogged(Account account) {
        return loggedAccounts.contains(account);
    }

    public static void addLogged(Account account) {
        loggedAccounts.add(account);
    }

    public static void removeLogged(Account account) {
        loggedAccounts.remove(account);
    }

    public static Set<Account> getLoggedAccounts() {
        return loggedAccounts;
    }
}
