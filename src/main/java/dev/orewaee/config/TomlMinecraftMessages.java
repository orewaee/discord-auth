package dev.orewaee.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import dev.orewaee.Main;

public class TomlMinecraftMessages implements MinecraftMessages {
    private static final MinecraftMessages instance = new TomlMinecraftMessages();

    private TomlParseResult toml;

    private void load() {
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
    public void reload() {
        load();
    }

    public TomlMinecraftMessages() {
        load();
    }

    @Override
    public String invalidCommandSyntax() {
        return toml.getString("minecraft.invalid_command_syntax");
    }

    @Override
    public String accountAlreadyExists() {
        return toml.getString("minecraft.account_already_exists");
    }

    @Override
    public String accountWithThatNameAlreadyExists() {
        return toml.getString("minecraft.account_with_that_name_already_exists");
    }

    @Override
    public String accountWithThatDiscordIdAlreadyExists() {
        return toml.getString("minecraft.account_with_that_discord_id_already_exists");
    }

    @Override
    public String accountAdded() {
        return toml.getString("minecraft.account_added");
    }

    @Override
    public String accountRemoved() {
        return toml.getString("minecraft.account_removed");
    }

    @Override
    public String configReloaded() {
        return toml.getString("minecraft.config_reloaded");
    }

    @Override
    public String messagesReloaded() {
        return toml.getString("minecraft.messages_reloaded");
    }

    @Override
    public String sessionRestored() {
        return toml.getString("minecraft.session_restored");
    }

    @Override
    public String missingAccount() {
        return toml.getString("minecraft.missing_account");
    }

    @Override
    public String successfulAuth() {
        return toml.getString("minecraft.successful_auth");
    }

    @Override
    public String sendKey() {
        return toml.getString("minecraft.send_key");
    }

    @Override
    public String authFirst() {
        return toml.getString("minecraft.auth_first");
    }

    public static MinecraftMessages getInstance() {
        return instance;
    }
}
