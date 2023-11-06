package dev.orewaee.utils;

import java.util.Random;

import dev.orewaee.key.KeyManager;

public class Generator {
    private static int generateRandomInt(int minimum, int maximum) {
        Random random = new Random();

        return random.nextInt(maximum + 1 - minimum) + minimum;
    }

    public static String generateCode() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        String first = generateRandomInt(0, 9) + "";
        char second = alphabet[generateRandomInt(0, alphabet.length - 1)];
        String third = generateRandomInt(10, 99) + "";

        String code = first + second + third;

        if (KeyManager.containsKeyByCode(code)) generateCode();

        return code;
    }
}