package dev.orewaee.config;

public interface MinecraftMessages {
    String invalidCommandSyntax();

    String accountAlreadyExists();
    String accountWithThatNameAlreadyExists();
    String accountWithThatDiscordIdAlreadyExists();

    String accountAdded();
    String accountRemoved();

    String configReloaded();
    String messagesReloaded();

    String sessionRestored();

    String missingAccount();

    String successfulAuth();
}
