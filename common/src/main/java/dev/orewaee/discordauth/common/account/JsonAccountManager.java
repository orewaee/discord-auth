package dev.orewaee.discordauth.common.account;

import java.util.List;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.common.utils.Utils;

public class JsonAccountManager implements AccountManager {
    private final List<Account> accounts;

    private final Gson gson = new Gson();

    private final Path dirs = Path.of("./plugins/discordauth/");
    private final Path file = Path.of("accounts.json");

    public JsonAccountManager() throws IOException {
        Utils.createIfNotExists(dirs, file);

        Path path = dirs.resolve(file);
        BufferedReader reader = Files.newBufferedReader(path);

        Type type = new TypeToken<List<Account>>() {}.getType();

        accounts = gson.fromJson(reader, type);

        reader.close();
    }

    private void save() {
        try {
            Utils.createIfNotExists(dirs, file);
            BufferedWriter writer = Files.newBufferedWriter(dirs.resolve(file));

            gson.toJson(accounts, writer);

            writer.close();
        } catch (IOException exception) {
            System.out.println("Error accounts saving: " + exception.getMessage());
        }
    }

    @Override
    public void add(@NotNull Account account) {
        accounts.add(account);
        save();
    }

    @Override
    public void removeByName(@NotNull String name) {
        accounts.removeIf(account -> account.getName().equals(name));
    }

    @Override
    public void removeByDiscordId(@NotNull String discordId) {
        accounts.removeIf(account -> account.getDiscordId().equals(discordId));
    }

    @Override
    public @Nullable Account getByName(@NotNull String name) {
        for (Account account : accounts)
            if (account.getName().equals(name))
                return account;

        return null;
    }

    @Override
    public @Nullable Account getByDiscordId(@NotNull String discordId) {
        for (Account account : accounts)
            if (account.getDiscordId().equals(discordId))
                return account;

        return null;
    }

    @Override
    public boolean containsByName(@NotNull String name) {
        for (Account account : accounts)
            if (account.getName().equals(name))
                return true;

        return false;
    }

    @Override
    public boolean containsByDiscordId(@NotNull String discordId) {
        for (Account account : accounts)
            if (account.getDiscordId().equals(discordId))
                return true;

        return false;
    }

    @Override
    public String toString() {
        return accounts.toString();
    }
}
