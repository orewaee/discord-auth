package dev.orewaee.discordauth.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent.PreLoginComponentResult;

import net.kyori.adventure.text.Component;

import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

public class PreLoginListener {
    private final AccountManager accountManager;

    public PreLoginListener(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        String name = event.getUsername();

        Account account = accountManager.getByName(name);

        if (account != null) return;

        Component reason = Component.text("You don't have an account");
        PreLoginComponentResult result = PreLoginComponentResult.denied(reason);

        event.setResult(result);
    }
}
