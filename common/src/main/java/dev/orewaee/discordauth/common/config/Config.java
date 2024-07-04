package dev.orewaee.discordauth.common.config;

import com.moandjiezana.toml.Toml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private final Path file;
    private final Path dirs;

    private Toml toml;

    public Config(Path file) {
        this.file = file;
        this.dirs = file.getParent();

        load();
    }

    private void load() {
        try {
            if (!Files.exists(dirs)) Files.createDirectories(dirs);

            if (!Files.exists(file)) {
                InputStream in = Config.class.getResourceAsStream("/" + file.getFileName());

                if (in == null) return;

                Files.copy(in, file);

                in.close();
            }

            BufferedReader reader = Files.newBufferedReader(file);

            toml = new Toml().read(reader);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getString(String key, String def) {
        return toml.getString(key, def);
    }

    public long getLong(String key, long def) {
        return toml.getLong(key, def);
    }
}
