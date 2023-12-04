package dev.orewaee.commands;

import java.util.List;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import net.kyori.adventure.text.Component;

import dev.orewaee.account.AccountManager;
import dev.orewaee.account.JsonAccountManager;
import dev.orewaee.config.MinecraftMessages;
import dev.orewaee.config.TomlMinecraftMessages;

public class AccountCommand implements SimpleCommand {
    private final AccountManager accountManager = JsonAccountManager.getInstance();

    private final MinecraftMessages minecraftMessages = TomlMinecraftMessages.getInstance();

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] arguments = invocation.arguments();

        if (arguments.length != 3) {
            source.sendMessage(Component.text(
                minecraftMessages.invalidCommandSyntax()
            ));

            return;
        }

        String action = arguments[0];

        switch (action) {
            case "add" -> {
                String name = arguments[1];
                String discordId = arguments[2];

                if (accountManager.containsAccountByName(name)) {
                    source.sendMessage(Component.text(
                        minecraftMessages.accountWithThatNameAlreadyExists()
                    ));

                    return;
                }

                if (accountManager.containsAccountByDiscordId(discordId)) {
                    source.sendMessage(Component.text(
                        minecraftMessages.accountWithThatDiscordIdAlreadyExists()
                    ));

                    return;
                }

                accountManager.addAccount(name, discordId);

                source.sendMessage(Component.text(
                    minecraftMessages.accountAdded()
                ));
            }

            case "remove" -> {
                String name = arguments[1];
                String discordId = arguments[2];

                accountManager.removeAccount(name, discordId);

                source.sendMessage(Component.text(
                    minecraftMessages.accountRemoved()
                ));
            }

            default -> source.sendMessage(Component.text(
                minecraftMessages.invalidCommandSyntax()
            ));
        }
    }
}
