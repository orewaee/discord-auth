package dev.orewaee.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;

import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.MinecraftMessages;
import dev.orewaee.config.TomlMinecraftMessages;

public class PreLoginEventListener {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    private final MinecraftMessages minecraftMessages = TomlMinecraftMessages.getInstance();

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        String name = event.getUsername();

        Account account = accountManager.getAccountByName(name);

        if (account != null) return;

        event.setResult(
            PreLoginEvent.PreLoginComponentResult.denied(
                MiniMessage.miniMessage().deserialize(
                    minecraftMessages.missingAccount()
                )
            )
        );
    }
}
