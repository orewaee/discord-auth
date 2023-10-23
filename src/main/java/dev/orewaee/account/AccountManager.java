package dev.orewaee.account;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dev.orewaee.utils.FileHelper;
import dev.orewaee.config.TomlConfig;
import org.jetbrains.annotations.Nullable;

public class AccountManager {
    private static Set<Account> accounts = new HashSet<>();

    public static void addAccount(String name, String discord) {
        if (accountExists(name, discord)) return;

        accounts.add(new Account(name, discord));

        FileHelper.update();
    }

    public static void removeAccount(String name, String discord) {
        if (!accountExists(name, discord)) return;

        for (Account account : accounts) {
            boolean namesEqual = name.equals(account.getName());
            boolean discordsEqual = discord.equals(account.getDiscord());

            if (namesEqual && discordsEqual) {
                accounts.remove(account);

                FileHelper.update();

                break;
            }
        }
    }

    public static boolean accountExists(String name, String discord) {
        boolean accountExistsByName = accountExistsByName(name);
        boolean accountExistsByDiscord = accountExistsByDiscord(discord);

        return accountExistsByName || accountExistsByDiscord;
    }

    public static boolean accountExistsByName(String name) {
        for (Account account : accounts)
            if (name.equals(account.getName()))
                return true;

        return false;
    }

    public static boolean accountExistsByDiscord(String discord) {
        for (Account account : accounts)
            if (discord.equals(account.getDiscord()))
                return true;

        return false;
    }

    private static void setAccounts(Set<Account> newAccounts) {
        accounts = newAccounts;
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

    public static @Nullable Account getAccountByName(String name) {
        for (Account account : accounts)
            if (name.equals(account.getName()))
                return account;

        return null;
    }

    public static @Nullable Account getAccountByDiscord(String discord) {
        for (Account account : accounts)
            if (discord.equals(account.getDiscord()))
                return account;

        return null;
    }

    public static Set<Account> getAccounts() {
        return accounts;
    }
}
