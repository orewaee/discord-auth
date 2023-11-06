package dev.orewaee.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.orewaee.account.AccountManager;
import net.kyori.adventure.text.Component;

import java.util.List;

public class AccountCommand implements SimpleCommand {
    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] arguments = invocation.arguments();

        if (arguments.length != 3) {
            source.sendMessage(Component.text(":("));
            return;
        }

        String action = arguments[0];

        switch (action) {
            case "add" -> {
                String name = arguments[1];
                String discord = arguments[2];

                if (AccountManager.containsAccountByName(name)) {
                    source.sendMessage(Component.text("An account with the same name already exists"));
                    return;
                }

                if (AccountManager.containsAccountByDiscord(discord)) {
                    source.sendMessage(Component.text("An account with the same discord already exists"));
                    return;
                }

                AccountManager.addAccount(name, discord);

                source.sendMessage(Component.text("Account added successfully"));
            }

            case "remove" -> {
                String name = arguments[1];
                String discord = arguments[2];

                AccountManager.removeAccount(name, discord);

                source.sendMessage(Component.text("Account removed successfully"));
            }

            default -> source.sendMessage(Component.text("Invalid command syntax"));
        }
    }
}
