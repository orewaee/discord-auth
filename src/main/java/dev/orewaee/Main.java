package dev.orewaee;

import java.nio.file.Path;
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
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;

import dev.orewaee.account.AccountManager;
import dev.orewaee.bot.Bot;
import dev.orewaee.events.EventsListener;
import dev.orewaee.commands.AccountCommand;
import dev.orewaee.commands.TestCommand;
import dev.orewaee.utils.ServerManager;
import dev.orewaee.config.TomlConfig;

@Plugin(id = "discordauth", name = "DiscordAuth", version = "0.1.0", authors = {"orewaee"})
public class Main {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    private final Bot bot;

    @Inject
    public Main(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        TomlConfig.loadConfig();

        this.bot = new Bot(TomlConfig.getBotToken());

        RegisteredServer lobby = null;
        List<RegisteredServer> servers = new ArrayList<>();
        for (RegisteredServer server : proxy.getAllServers()) {
            ServerInfo info = server.getServerInfo();

            if (info.getName().equals(TomlConfig.getLobbyServer())) lobby = server;
            else servers.add(server);
        }

        ServerManager.loadServers(proxy, lobby, servers);

        logger.info("Plugin launched!");
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        System.out.println("ProxyInitializeEvent");

        AccountManager.loadAccounts();

        EventManager eventManager = proxy.getEventManager();
        CommandManager commandManager = proxy.getCommandManager();

        registerEvents(eventManager);
        registerCommands(commandManager);
    }

    private void registerEvents(EventManager eventManager) {
        eventManager.register(this, new EventsListener());
    }

    private void registerCommands(CommandManager commandManager) {
        CommandMeta testCommandMeta = commandManager.metaBuilder("test")
            .plugin(this)
            .build();

        CommandMeta accoundCommandMeta = commandManager.metaBuilder("account")
            .plugin(this)
            .build();

        commandManager.register(testCommandMeta, new TestCommand());
        commandManager.register(accoundCommandMeta, new AccountCommand());
    }
}