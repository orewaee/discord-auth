package dev.orewaee;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import dev.orewaee.bot.Bot;
import dev.orewaee.commands.ReloadCommand;
import dev.orewaee.config.Config;
import dev.orewaee.events.*;
import dev.orewaee.commands.AccountCommand;
import dev.orewaee.commands.TestCommand;
import dev.orewaee.managers.ServerManager;
import dev.orewaee.config.TomlConfig;
import dev.orewaee.version.Updater;

@Plugin(id = Constants.ID, name = Constants.NAME, version = Constants.VERSION)
public class Main {
    private final ProxyServer proxy;
    private final Logger logger;

    private final Bot bot;

    @Inject
    public Main(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;

        Config config = TomlConfig.getInstance();

        this.bot = new Bot(config.botToken());

        RegisteredServer lobby = null;
        List<RegisteredServer> servers = new ArrayList<>();
        for (RegisteredServer server : proxy.getAllServers()) {
            ServerInfo info = server.getServerInfo();

            if (info.getName().equals(config.lobbyServerName())) lobby = server;
            else servers.add(server);
        }

        ServerManager.loadServers(proxy, lobby, servers);

        Updater.checkForUpdates();

        logger.info("Plugin launched!");
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        System.out.println("ProxyInitializeEvent");

        EventManager eventManager = proxy.getEventManager();
        CommandManager commandManager = proxy.getCommandManager();

        registerEvents(eventManager);
        registerCommands(commandManager);
    }

    private void registerEvents(EventManager eventManager) {
        eventManager.register(this, new DisconnectEventListener());
        eventManager.register(this, new PostLoginEventListener());
        eventManager.register(this, new PreLoginEventListener());
        eventManager.register(this, new ServerPreConnectEventListener());
    }

    private void registerCommands(CommandManager commandManager) {
        // todo rewrite commands to brigadier commands

        CommandMeta reloadCommandMeta = commandManager.metaBuilder("discordauth")
            .aliases("da")
            .plugin(this)
            .build();

        CommandMeta testCommandMeta = commandManager.metaBuilder("test")
            .plugin(this)
            .build();

        CommandMeta accoundCommandMeta = commandManager.metaBuilder("account")
            .plugin(this)
            .build();

        commandManager.register(reloadCommandMeta, new ReloadCommand());
        commandManager.register(testCommandMeta, new TestCommand());
        commandManager.register(accoundCommandMeta, new AccountCommand());
    }
}