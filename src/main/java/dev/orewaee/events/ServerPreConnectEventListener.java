package dev.orewaee.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent.ServerResult;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.managers.AuthManager;
import dev.orewaee.managers.ServerManager;
import dev.orewaee.config.MinecraftMessages;
import dev.orewaee.config.TomlMinecraftMessages;

public class ServerPreConnectEventListener {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    private final MinecraftMessages minecraftMessages = TomlMinecraftMessages.getInstance();

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        String name = player.getUsername();

        Account account = accountManager.getAccountByName(name);

        RegisteredServer originalServer = event.getOriginalServer();

        if (account == null && !originalServer.equals(ServerManager.getLobby())) {
            event.setResult(ServerResult.denied());

            Component message = MiniMessage.miniMessage().deserialize(
                minecraftMessages.missingAccount()
            );

            player.sendMessage(message);

            return;
        }

        // todo replace ServerManager to new singleton manager
        if (!AuthManager.isLogged(account) && !originalServer.equals(ServerManager.getLobby())) {
            event.setResult(ServerResult.denied());

            Component message = MiniMessage.miniMessage().deserialize(
                minecraftMessages.authFirst()
            );

            player.sendMessage(message);
        }
    }
}
