package dev.orewaee.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;

import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;

public class PreLoginEventListener {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        String name = event.getUsername();

        Account account = accountManager.getAccountByName(name);

        if (account != null) return;

        event.setResult(
            PreLoginEvent.PreLoginComponentResult.denied(
                MiniMessage.miniMessage().deserialize("<#16D886>Account missing!")
            )
        );
    }
}
