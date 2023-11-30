package dev.orewaee.config;

public interface Config {
    String getAccountsFileName();

    long getKeyExpirationTime();
    long getSessionExpirationTime();

    String getLobbyServerName();

    String getBotToken();
    String getAdminDiscordId();
}
