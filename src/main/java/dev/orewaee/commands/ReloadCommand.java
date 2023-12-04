package dev.orewaee.commands;

import java.util.List;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import net.kyori.adventure.text.Component;

import dev.orewaee.config.MinecraftMessages;
import dev.orewaee.config.TomlMinecraftMessages;

public class ReloadCommand implements SimpleCommand {
    MinecraftMessages minecraftMessages = TomlMinecraftMessages.getInstance();

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] arguments = invocation.arguments();

        if (arguments.length == 0) {
            Component message = Component.text(
                minecraftMessages.invalidCommandSyntax()
            );

            source.sendMessage(message);

            return;
        }

        switch (arguments[0]) {
            case "reload" -> {
                // todo reload config and messages

                Component message = Component.text(
                    minecraftMessages.configReloaded()
                );

                source.sendMessage(message);
            }

            default -> {
                Component message = Component.text(
                    minecraftMessages.invalidCommandSyntax()
                );

                source.sendMessage(message);
            }
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return List.of("reload");
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("discordauth.reload");
    }
}
