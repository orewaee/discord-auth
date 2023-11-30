package dev.orewaee.config;

import dev.orewaee.Main;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

// todo Minecraft and Discord messages

public class TomlMessages implements Messages {
    private static final Messages instance = new TomlMessages();

    private static final Path path = Path.of("plugins/DiscordAuth/messages.toml");
    private static TomlParseResult toml;

    public TomlMessages() {
        Path directories = path.getParent();

        try {
            if (!Files.exists(directories))
                Files.createDirectories(directories);

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
    public String getAuthFirstMessage() {
        return toml.getString("auth_first");
    }

    @Override
    public String getMissingAccountMessage() {
        return toml.getString("missing_account");
    }

    @Override
    public String getSendKeyMessage() {
        return toml.getString("send_key");
    }

    @Override
    public String getSessionRestoredMessage() {
        return toml.getString("session_restored");
    }

    @Override
    public String getSuccessfulAuthMessage() {
        return toml.getString("successful_auth_minecraft");
    }

    @Override
    public String getSuccessfulAuthDiscordMessage() {
        return toml.getString("successful_auth_discord");
    }

    @Override
    public String getInvalidKeyMessage() {
        return toml.getString("invalid_key");
    }

    @Override
    public String getKeyNotFoundMessage() {
        return toml.getString("key_not_found");
    }

    public static Messages getInstance() {
        return instance;
    }
}
