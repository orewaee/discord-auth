package dev.orewaee.managers;

import java.util.List;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

public class ServerManager {
    private static ProxyServer proxy;
    private static RegisteredServer lobby;
    private static List<RegisteredServer> servers;

    public static void loadServers(
        ProxyServer proxy,
        RegisteredServer lobby,
        List<RegisteredServer> servers
    ) {
        setLobby(lobby);
        setProxy(proxy);
        setServers(servers);
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    private static void setProxy(ProxyServer newProxy) {
        proxy = newProxy;
    }

    public static RegisteredServer getLobby() {
        return lobby;
    }

    private static void setLobby(RegisteredServer newLobby) {
        lobby = newLobby;
    }

    public static List<RegisteredServer> getServers() {
        return servers;
    }

    private static void setServers(List<RegisteredServer> newServers) {
        servers = newServers;
    }
}
