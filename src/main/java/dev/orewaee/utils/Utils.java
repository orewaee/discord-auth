package dev.orewaee.utils;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.TomlMinecraftMessages;
import dev.orewaee.key.InMemoryKeyManager;
import dev.orewaee.key.Key;
import dev.orewaee.key.KeyManager;

public class Utils {
    public static void sendAuthInstructions(Player player) {
        AccountManager accountManager = JsonAccountManager.getInstance();
        KeyManager keyManager = InMemoryKeyManager.getInstance();

        String name = player.getUsername();

        Account account = accountManager.getAccountByName(name);

        Key key = new Key();

        String text = TomlMinecraftMessages.getInstance().sendKey().replace("%key%", key.code());
        Component message = MiniMessage.miniMessage().deserialize(text);

        player.sendMessage(message);

        keyManager.addKey(account, key);
    }
}
