package dev.orewaee.discordauth.velocity.discord;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import net.kyori.adventure.text.Component;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.Account;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.Key;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.Pool;
import dev.orewaee.discordauth.api.pool.PoolManager;

import dev.orewaee.discordauth.common.config.Config;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class DMListener extends ListenerAdapter {
    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final Config config;

    public DMListener(Config config) {
        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
        this.keyManager = api.getKeyManager();
        this.poolManager = api.getPoolManager();
        this.config = config;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.PRIVATE)) return;
        if (event.getAuthor().isBot()) return;

        String discordId = event.getAuthor().getId();
        String messageContent = event.getMessage().getContentRaw();

        Account account = accountManager.getByDiscordId(discordId);
        if (account == null) return;

        Pool pool = poolManager.getByAccount(account);
        if (pool == null || pool.getStatus()) return;

        Key key = keyManager.getByAccount(account);
        if (key == null) return;

        if (!key.getValue().equals(messageContent)) return;

        pool.setStatus(true);
        pool.getPlayer().sendMessage(Component.text("successful auth"));
        keyManager.removeByAccount(account);

        // redirecting
        String serverName = config.getString("servers.redirect", "");
        System.out.println("servers.redirect " + serverName);
        if (!serverName.isEmpty()) DiscordAuth.getInstance()
            .getProxy()
            .getServer(serverName)
            .ifPresent(server -> pool
                .getPlayer()
                .createConnectionRequest(server)
                .connect());

        event.getMessage().reply("successful auth").queue();
    }
}
