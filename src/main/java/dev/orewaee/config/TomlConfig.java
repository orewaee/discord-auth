package dev.orewaee.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import dev.orewaee.Main;

public class TomlConfig implements Config {
    private static final Config instance = new TomlConfig();

    private TomlParseResult toml;

    private void load() {
        Path path = Path.of("plugins/DiscordAuth/config.toml");
        Path dirs = path.getParent();

        try {
            if (!Files.exists(dirs))
                Files.createDirectories(dirs);

            if (!Files.exists(path)) {
                InputStream inputStream = Main.class.getResourceAsStream("/config.toml");

                if (inputStream == null) return;

                Files.copy(inputStream, path);

                inputStream.close();
            }

            toml = Toml.parse(path);
        } catch (Exception exception) {
            System.out.println("Error while loading config");
            exception.printStackTrace();
        }
    }

    @Override
    public void reload() {
        load();
    }

    public TomlConfig() {
        load();
    }

    @Override
    public String accountsFileName() {
        return toml.getString("accounts_file_name");
    }

    @Override
    public Long keyExpirationTime() {
        return toml.getLong("key_expiration_time");
    }

    @Override
    public Long sessionExpirationTime() {
        return toml.getLong("session_expiration_time");
    }

    @Override
    public String lobbyServerName() {
        return toml.getString("lobby_server_name");
    }

    @Override
    public String botToken() {
        return toml.getString("bot_token");
    }

    @Override
    public List<String> adminDiscordId() {
        String[] array = toml.getString("admin_discord_id").split(";");
        return Arrays.asList(array);
    }

    public static Config getInstance() {
        return instance;
    }
}
