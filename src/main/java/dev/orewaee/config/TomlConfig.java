package dev.orewaee.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import dev.orewaee.Main;

public class TomlConfig {
    private static final Path path = Path.of("plugins/DiscordAuth/config.toml");
    private static TomlParseResult toml;

    public static void loadConfig() {
        Path directories = path.getParent();

        try {
            if (!Files.exists(directories))
                Files.createDirectories(directories);

            if (!Files.exists(path)) {
                InputStream inputStream = Main.class.getResourceAsStream("/config.toml");

                if (inputStream == null) return;

                Files.copy(inputStream, path);

                inputStream.close();
            }

            toml = Toml.parse(path);
        } catch (Exception exception) {
            System.out.println("Error 3");
            exception.printStackTrace();
        }
    }

    // config
    public static String getAccountsFileName() {
        return toml.getString("accounts_file_name");
    }

    // config
    public static Long getKeyExpirationTime() {
        return toml.getLong("key_expiration_time");
    }

    // config
    public static Long getSessionExpirationTime() {
        return toml.getLong("session_expiration_time");
    }

    // config
    public static String getLobbyServer() {
        return toml.getString("lobby_server");
    }

    // config
    public static String getBotToken() {
        return toml.getString("bot_token");
    }

    // messages
    public static String getAuthFirstMessage() {
        return toml.getString("auth_first_message");
    }

    public static String getMissingAccountMessage() {
        return toml.getString("missing_account_message");
    }

    public static String getSendKeyMessage() {
        return toml.getString("send_key_message");
    }

    public static String getSessionRestoredMessage() {
        return toml.getString("session_restored_message");
    }

    public static String getSuccessfulAuthMessage() {
        return toml.getString("successful_auth_message");
    }

    public static String getSuccessfulAuthDiscordMessage() {
        return toml.getString("successful_auth_discord_message");
    }

    public static String getInvalidKeyMessage() {
        return toml.getString("invalid_key_message");
    }

    public static String getKeyNotFoundMessage() {
        return toml.getString("key_not_found");
    }

    // config
    public static String getDiscordAdmin() {
        return toml.getString("discord_admin");
    }
}
