package dev.orewaee.bot;

import com.velocitypowered.api.proxy.Player;
import dev.orewaee.account.Account;
import dev.orewaee.account.AccountManager;
import dev.orewaee.key.Key;
import dev.orewaee.key.KeyManager;
import dev.orewaee.utils.AuthManager;
import dev.orewaee.utils.ServerManager;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventsListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.PRIVATE)) return;

        if (event.getAuthor().isBot()) return;

        String discord = event.getAuthor().getId();
        String messageContent = event.getMessage().getContentRaw();

        Account account = AccountManager.getAccountByDiscord(discord);

        if (account == null) return;

        String name = account.getName();

        if (AuthManager.isLogged(name)) return;

        Key key = KeyManager.getKeyByName(name);

        if (key == null) {
            event.getMessage().reply("Ключ не найден").queue();
            return;
        }

        if (!key.getKey().equals(messageContent)) {
            event.getMessage().reply("Неверный ключ").queue();
            return;
        }

        AuthManager.addLogged(name);
        KeyManager.removeKeyByName(name);

        event.getMessage().reply("Вы успешно авторизованы :white_check_mark:").queue();

        Player player = ServerManager.getProxy().getPlayer(name).orElse(null);

        if (player == null) return;

        player.sendMessage(Component.text("Вы успешно авторизовались!").color(TextColor.color(0x16D886)));
    }
}
