package dev.orewaee.key;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KeyManager {
    private static final List<Key> keys = new ArrayList<>();

    public static void addKey(String name) {
        if (keyExistsByName(name)) return;

        keys.add(new Key(name));
    }

    public static void addKey(Key key) {
        if (keyExistsByName(key.getName())) return;

        keys.add(key);
    }

    public static void removeKeyByName(String targetName) {
        keys.removeIf(key -> targetName.equals(key.getName()));
    }

    public static boolean keyExistsByName(String targetName) {
        for (Key key : keys)
            if (targetName.equals(key.getName()))
                return true;

        return false;
    }

    public static boolean keyExistsByKey(String targetKey) {
        for (Key key : keys)
            if (targetKey.equals(key.getKey()))
                return true;

        return false;
    }

    public static List<Key> getKeys() {
        return keys;
    }

    @Nullable
    public static Key getKeyByName(String targetName) {
        for (Key key : keys)
            if (targetName.equals(key.getName()))
                return key;

        return null;
    }

    @Nullable
    public static Key getKeyByKey(String targetKey) {
        for (Key key : keys)
            if (targetKey.equals(key.getKey()))
                return key;

        return null;
    }
}
