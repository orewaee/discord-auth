package dev.orewaee.discordauth.common.utils;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.IOException;

public class Utils {
    public static void createIfNotExists(Path path, Path file) throws IOException {
        if (!Files.exists(path)) Files.createDirectories(path);

        Path full = path.resolve(file);

        if (Files.exists(full)) return;

        InputStream resource = Utils.class.getResourceAsStream("/" + file);
        if (resource == null) throw new IOException("Resource " + file + " not found");

        Files.copy(resource, full);
        resource.close();
    }
}
