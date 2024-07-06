package dev.orewaee.discordauth.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent.PreLoginComponentResult;

import net.kyori.adventure.text.Component;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class PreLoginListener {
    private final AccountManager accountManager;

    public PreLoginListener() {
        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
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
