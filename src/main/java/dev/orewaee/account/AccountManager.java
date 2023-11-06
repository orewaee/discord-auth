package dev.orewaee.account;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;

import dev.orewaee.utils.FileHelper;
import dev.orewaee.config.TomlConfig;

public class AccountManager {
    private static Set<Account> accounts = new HashSet<>();

    public static void addAccount(String name, String discord) {
        Account account = new Account(name, discord);

        if (containsAccount(account)) return;

        accounts.add(account);

        FileHelper.update();
    }

    public static void removeAccount(String name, String discord) {
        Account account = new Account(name, discord);

        if (!containsAccount(account)) return;

        accounts.remove(account);

        FileHelper.update();
    }

    public static boolean containsAccount(Account account) {
        return accounts.contains(account);
    }

    public static boolean containsAccount(String name, String discord) {
        Account account = new Account(name, discord);

        return accounts.contains(account);
    }

    public static boolean containsAccountByName(String name) {
        for (Account account : accounts)
            if (name.equals(account.name()))
                return true;

        return false;
    }

    public static boolean containsAccountByDiscord(String discord) {
        for (Account account : accounts)
            if (discord.equals(account.discord()))
                return true;

        return false;
    }

    public static void loadAccounts() {
        Path path = Path.of(TomlConfig.getAccountsFileName());
        Path directories = path.getParent();

        try {
            if (!Files.exists(directories))
                Files.createDirectories(directories);

            if (!Files.exists(path))
                Files.createFile(path);

            Reader reader = Files.newBufferedReader(path);

            Gson gson = new Gson();
            Type type = new TypeToken<Set<Account>>() {}.getType();

            accounts = gson.fromJson(reader, type);
        } catch (Exception exception) {
            System.out.println("Error 1");
            exception.printStackTrace();
        }
    }

    @Nullable
    public static Account getAccountByName(String name) {
        for (Account account : accounts)
            if (name.equals(account.name()))
                return account;

        return null;
    }

    @Nullable
    public static Account getAccountByDiscord(String discord) {
        for (Account account : accounts)
            if (discord.equals(account.discord()))
                return account;

        return null;
    }

    public static Set<Account> getAccounts() {
        return accounts;
    }
}
