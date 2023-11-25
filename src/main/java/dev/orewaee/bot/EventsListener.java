package dev.orewaee.bot;

import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.key.InMemoryKeyManager;
import org.jetbrains.annotations.NotNull;

import com.velocitypowered.api.proxy.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import dev.orewaee.account.Account;
import dev.orewaee.config.TomlConfig;
import dev.orewaee.key.Key;
import dev.orewaee.key.KeyManager;
import dev.orewaee.managers.AuthManager;
import dev.orewaee.managers.ServerManager;

public class EventsListener extends ListenerAdapter {
    private final AccountManager accountManager = JsonAccountManager.getInstance();
    private final KeyManager keyManager = InMemoryKeyManager.getInstance();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.PRIVATE)) return;

        if (event.getAuthor().isBot()) return;

        String discordId = event.getAuthor().getId();
        String messageContent = event.getMessage().getContentRaw();

        Account account = accountManager.getAccountByDiscordId(discordId);

        if (account == null) return;

        String name = account.name();

        if (AuthManager.isLogged(account)) return;

        Key key = keyManager.getKeyByAccount(account);

        if (key == null) {
            event.getMessage().reply(TomlConfig.getKeyNotFoundMessage()).queue();
            return;
        }

        if (!messageContent.equals(key.code())) {
            event.getMessage().reply(TomlConfig.getInvalidKeyMessage()).queue();
            return;
        }

        AuthManager.addLogged(account);
        keyManager.removeKey(account);

        event.getMessage().reply(TomlConfig.getSuccessfulAuthDiscordMessage()).queue();

        Player player = ServerManager.getProxy().getPlayer(name).orElse(null);

        if (player == null) return;

        Component message = MiniMessage.miniMessage().deserialize(TomlConfig.getSuccessfulAuthMessage());
        player.sendMessage(message);
    }
}
