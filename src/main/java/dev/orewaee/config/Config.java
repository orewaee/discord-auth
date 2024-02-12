package dev.orewaee.config;

import java.util.List;

public interface Config {
    void reload();

    String accountsFileName();

    Long keyExpirationTime();
    Long sessionExpirationTime();

    String lobbyServerName();

    String botToken();
    List<String> adminDiscordId();
}
