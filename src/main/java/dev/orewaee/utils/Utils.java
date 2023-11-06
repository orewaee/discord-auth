package dev.orewaee.utils;

import com.velocitypowered.api.proxy.Player;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.config.TomlConfig;
import dev.orewaee.key.Key;
import dev.orewaee.key.KeyManager;

public class Utils {
    public static void sendAuthInstructions(Player player) {
        String name = player.getUsername();

        if (!AccountManager.containsAccountByName(name)) return;

        Account account = AccountManager.getAccountByName(name);

        Key key = new Key();

        String text = TomlConfig.getSendKeyMessage().replace("%key%", key.code());
        Component message = MiniMessage.miniMessage().deserialize(text);

        player.sendMessage(message);

        KeyManager.addKey(account, key);
    }
}
