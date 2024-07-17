package dev.orewaee.discordauth.velocity.commands;

import com.velocitypowered.api.command.RawCommand;

import dev.orewaee.discordauth.api.DiscordAuthAPI;
import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.SessionManager;

import dev.orewaee.discordauth.velocity.DiscordAuth;

public class DebugCommand implements RawCommand {
    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    public DebugCommand() {
        DiscordAuthAPI api = DiscordAuth.getInstance();

        this.accountManager = api.getAccountManager();
        this.keyManager = api.getKeyManager();
        this.poolManager = api.getPoolManager();
        this.sessionManager = api.getSessionManager();
    }

    @Override
    public void execute(Invocation invocation) {
        System.out.println("AccountManager\n" + accountManager);
        System.out.println("KeyManager\n" + keyManager);
        System.out.println("PoolManager\n" + poolManager);
        System.out.println("SessionManager\n" + sessionManager);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("discordauth.command.debug");
    }
}
