package dev.orewaee.discordauth.common.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

import com.moandjiezana.toml.Toml;

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


    public void reload() {
        load();
    }

    public String getString(String key, String def) {
        return toml.getString(key, def);
    }

    public List<String> getList(String key, List<String> def) {
        return toml.getList(key, def);
    }

    public long getLong(String key, long def) {
        return toml.getLong(key, def);
    }
}
