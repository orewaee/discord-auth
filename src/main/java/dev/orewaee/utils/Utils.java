package dev.orewaee.utils;

import com.velocitypowered.api.proxy.Player;
import dev.orewaee.config.TomlConfig;
import dev.orewaee.key.Key;
import dev.orewaee.key.KeyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Random;

public class Utils {
    private static int generateRandomInt(int minimum, int maximum) {
        Random random = new Random();

        return random.nextInt(maximum + 1 - minimum) + minimum;
    }

    public static String generateKey() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        String first = generateRandomInt(0, 9) + "";
        char second = alphabet[generateRandomInt(0, alphabet.length - 1)];
        String third = generateRandomInt(10, 99) + "";

        String key = first + second + third;

        if (KeyManager.keyExistsByKey(key)) generateKey();

        return key;
    }

    public static void sendAuthInstructions(Player player) {
        String name = player.getUsername();

        Key key = new Key(name);

        String text = TomlConfig.getSendKeyMessage().replace("%key%", key.getKey());
        Component message = MiniMessage.miniMessage().deserialize(text);

        player.sendMessage(message);

        KeyManager.addKey(key);
    }
}
