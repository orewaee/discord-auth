package dev.orewaee.key;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import com.velocitypowered.api.proxy.Player;
import dev.orewaee.account.Account;
import dev.orewaee.config.TomlConfig;
import dev.orewaee.utils.AuthManager;
import dev.orewaee.utils.ServerManager;
import dev.orewaee.utils.Utils;

public class KeyManager {
    private static final Map<Account, Key> keys = new HashMap<>();

    public static void addKey(Account account) {
        Key key = new Key();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                KeyManager.removeKeyByAccount(account);
                System.out.println("key removed");
            }
        };

        key.timer().schedule(task, TomlConfig.getKeyExpirationTime());

        keys.put(account, key);
    }

    public static void addKey(Account account, Key key) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                KeyManager.removeKeyByAccount(account);
                System.out.println("key removed");

                String name = account.name();

                if (AuthManager.isLogged(name)) return;

                Player player = ServerManager.getProxy().getPlayer(name).orElse(null);

                if (player == null) return;

                Utils.sendAuthInstructions(player);
            }
        };

        key.timer().schedule(task, 1000 * TomlConfig.getKeyExpirationTime());

        keys.put(account, key);
    }

    public static void removeKeyByAccount(Account account) {
        Key key = keys.get(account);

        if (key == null) return;

        key.timer().cancel();

        keys.remove(account);
    }

    public static boolean containsKey(Key key) {
        return keys.containsValue(key);
    }

    public static boolean containsKeyByCode(String code) {
        for (Account account : keys.keySet())
            if (code.equals(keys.get(account).code()))
                return true;

        return false;
    }

    public static Key getKeyByAccount(Account account) {
        return keys.get(account);
    }

    public static Map<Account, Key> getKeys() {
        return keys;
    }
}
