package dev.orewaee.discordauth.common.utils;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.IOException;
import java.util.Random;

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

    public static int randInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }

    public static String genValue(String alphabet) {
        char[] chars = alphabet.toCharArray();

        String x = randInt(0, 9) + "";
        char y = chars[randInt(0, chars.length - 1)];
        String z = randInt(10, 99) + "";

        return x + y + z;
    }
}
