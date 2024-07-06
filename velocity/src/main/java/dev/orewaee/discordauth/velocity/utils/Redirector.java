package dev.orewaee.discordauth.velocity.utils;

import java.util.Optional;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class Redirector {
    public static void redirect(Player player, String target) {
        ProxyServer proxy = DiscordAuth.getInstance().getProxy();
        Optional<RegisteredServer> optional = proxy.getServer(target);

        optional.ifPresent(server -> player
            .createConnectionRequest(server)
            .connect());
    }
}
