package dev.orewaee.config;

public interface MinecraftMessages {
    void reload();

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

    String sendKey();

    String authFirst();
}
