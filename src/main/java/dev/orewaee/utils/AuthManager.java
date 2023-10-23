package dev.orewaee.utils;

import java.util.HashSet;
import java.util.Set;

public class AuthManager {
    private static final Set<String> loggedPlayers = new HashSet<>();

    public static boolean isLogged(String name) {
        return loggedPlayers.contains(name);
    }

    public static void addLogged(String name) {
        loggedPlayers.add(name);
    }

    public static void removeLogged(String name) {
        loggedPlayers.remove(name);
    }

    public static Set<String> getLoggedPlayers() {
        return loggedPlayers;
    }
}
