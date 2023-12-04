package dev.orewaee.key;

import java.util.Map;
import java.util.HashMap;
import java.util.TimerTask;

import org.jetbrains.annotations.Nullable;

import com.velocitypowered.api.proxy.Player;

import dev.orewaee.account.Account;
import dev.orewaee.config.TomlConfig;
import dev.orewaee.managers.AuthManager;
import dev.orewaee.managers.ServerManager;
import dev.orewaee.utils.Utils;

public class InMemoryKeyManager implements KeyManager {
    private static KeyManager instance;

    private final Map<Account, Key> keys = new HashMap<>();

    @Override
    public void addKey(Account account, Key key) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                InMemoryKeyManager
                    .getInstance()
                    .removeKey(account);

                if (AuthManager.isLogged(account)) return;

                Player player = ServerManager.getProxy().getPlayer(account.name()).orElse(null);

                if (player == null) return;

                Utils.sendAuthInstructions(player);
            }
        };

        long time = TomlConfig.getInstance().keyExpirationTime() * 1000;

        key.timer().schedule(task, time);

        keys.put(account, key);
    }

    @Override
    public void removeKey(Account account) {
        Key key = keys.get(account);

        if (key == null) return;

        key.timer().cancel();

        keys.remove(account);
    }

    @Override
    public boolean containsKeyByCode(String code) {
        for (Account account : keys.keySet())
            if (keys.get(account).code().equals(code))
                return true;

        return false;
    }

    @Override
    @Nullable
    public Key getKeyByAccount(Account account) {
        return keys.get(account);
    }

    @Override
    public Map<Account, Key> getKeys() {
        return keys;
    }

    public static KeyManager getInstance() {
        if (instance == null) instance = new InMemoryKeyManager();

        return instance;
    }
}
