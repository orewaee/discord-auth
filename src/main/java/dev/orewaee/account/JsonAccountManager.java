package dev.orewaee.account;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import dev.orewaee.utils.FileHelper;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dev.orewaee.config.TomlConfig;

public class JsonAccountManager implements AccountManager {
    private static final AccountManager instance = new JsonAccountManager();

    private Set<Account> accounts = new HashSet<>();

    public JsonAccountManager() {
        Path path = Path.of(TomlConfig.getAccountsFileName());
        Path dirs = path.getParent();

        try {
            if (!Files.exists(dirs))
                Files.createDirectories(dirs);

            if (!Files.exists(path))
                Files.createFile(path);

            Reader reader = Files.newBufferedReader(path);

            Gson gson = new Gson();
            Type type = new TypeToken<Set<Account>>() {}.getType();

            accounts = gson.fromJson(reader, type);

            reader.close();
        } catch (IOException exception) {
            // todo use logger
        }
    }

    @Override
    public void addAccount(String name, String discordId) {
        accounts.add(new Account(name, discordId));

        FileHelper.update();
    }

    @Override
    public void removeAccount(String name, String discordId) {
        accounts.remove(new Account(name, discordId));

        FileHelper.update();
    }

    @Override
    @Nullable
    public Account getAccountByName(String name) {
        for (Account account : accounts)
            if (account.name().equals(name))
                return account;

        return null;
    }

    @Override
    @Nullable
    public Account getAccountByDiscordId(String discordId) {
        for (Account account : accounts)
            if (account.discordId().equals(discordId))
                return account;

        return null;
    }

    @Override
    public boolean containsAccount(Account account) {
        return accounts.contains(account);
    }

    @Override
    public boolean containsAccount(String name, String discordId) {
        return accounts.contains(new Account(name, discordId));
    }

    @Override
    public boolean containsAccountByName(String name) {
        for (Account account : accounts)
            if (account.name().equals(name))
                return true;

        return false;
    }

    @Override
    public boolean containsAccountByDiscordId(String discordId) {
        for (Account account : accounts)
            if (account.discordId().equals(discordId))
                return true;

        return false;
    }

    @Override
    public Set<Account> getAccounts() {
        return accounts;
    }

    public static AccountManager getInstance() {
        return instance;
    }
}
