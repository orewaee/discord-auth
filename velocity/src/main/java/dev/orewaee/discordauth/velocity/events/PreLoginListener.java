package dev.orewaee.discordauth.velocity.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent.PreLoginComponentResult;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class PreLoginListener {
    private final Config config;
    private final AccountManager accountManager;

    private final static String NO_ACCOUNT = "minecraft-components.no-account";

    public PreLoginListener(Config config) {
        this.config = config;

        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        String name = event.getUsername();

        Account account = accountManager.getByName(name);

        if (account != null) return;

        String message = config
            .getString(NO_ACCOUNT, "You don't have an account")
            .replace("%name%", name);

        Component component = MiniMessage.miniMessage().deserialize(message);

        PreLoginComponentResult result = PreLoginComponentResult.denied(component);

        event.setResult(result);
    }
}
