package dev.orewaee.discordauth.velocity.commands;

import java.util.List;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import dev.orewaee.discordauth.common.config.Config;

public class ReloadCommand implements SimpleCommand {
    private final Config config;

    public ReloadCommand(Config config) {
        this.config = config;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] arguments = invocation.arguments();

        if (arguments.length != 1) {
            source.sendPlainMessage("Invalid command syntax");
            return;
        }

        if (!arguments[0].equalsIgnoreCase("reload")) return;

        config.reload();

        source.sendPlainMessage("Config reloaded");
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        if (invocation.arguments().length == 1) return List.of("reload");
        return List.of();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("discordauth.command.reload");
    }
}
