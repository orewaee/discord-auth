package dev.orewaee.config;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.orewaee.Main;

public class TomlDiscordMessages implements DiscordMessages {
    private static final DiscordMessages instance = new TomlDiscordMessages();

    private TomlParseResult toml;

    public TomlDiscordMessages() {
        Path path = Path.of("plugins/DiscordAuth/messages.toml");
        Path dirs = path.getParent();

        try {
            if (!Files.exists(dirs))
                Files.createDirectories(dirs);

            if (!Files.exists(path)) {
                InputStream inputStream = Main.class.getResourceAsStream("/messages.toml");

                if (inputStream == null) return;

                Files.copy(inputStream, path);

                inputStream.close();
            }

            toml = Toml.parse(path);
        } catch (Exception exception) {
            System.out.println("Error while loading messages");
            exception.printStackTrace();
        }
    }

    @Override
    public String accountWithThatNameAlreadyExists() {
        return toml.getString("discord.account_with_that_name_already_exists");
    }

    @Override
    public String accountWithThatDiscordIdAlreadyExists() {
        return toml.getString("discord.account_with_that_discord_id_already_exists");
    }

    @Override
    public String missingAccount() {
        return toml.getString("discord.missing_account");
    }

    @Override
    public String invalidKey() {
        return toml.getString("discord.invalid_key");
    }

    @Override
    public String keyNotFound() {
        return toml.getString("discord.key_not_found");
    }

    @Override
    public String successfulAuth() {
        return toml.getString("discord.successful_auth");
    }

    public static DiscordMessages getInstance() {
        return instance;
    }
}
