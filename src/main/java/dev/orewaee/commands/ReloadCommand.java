package dev.orewaee.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import dev.orewaee.config.TomlConfig;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ReloadCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] arguments = invocation.arguments();

        if (arguments.length == 0) {
            Component message = Component.text("Invalid command syntax");

            source.sendMessage(message);

            return;
        }

        switch (arguments[0]) {
            case "reload" -> {
                TomlConfig.loadConfig();

                Component message = Component.text("Successful config reload");

                source.sendMessage(message);
            }

            default -> {
                Component message = Component.text("There can be no such argument");

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
