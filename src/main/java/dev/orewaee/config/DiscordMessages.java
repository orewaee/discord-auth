package dev.orewaee.config;

public interface DiscordMessages {
    String accountWithThatNameAlreadyExists();
    String accountWithThatDiscordIdAlreadyExists();

    String missingAccount();

    String invalidKey();
    String keyNotFound();

    String successfulAuth();
}
