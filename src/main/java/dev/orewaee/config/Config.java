package dev.orewaee.config;

public interface Config {
    String accountsFileName();

    Long keyExpirationTime();
    Long sessionExpirationTime();

    String lobbyServerName();

    String botToken();
    String adminDiscordId();
}
