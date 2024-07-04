package dev.orewaee.discordauth.velocity.commands;

import com.velocitypowered.api.command.RawCommand;

import dev.orewaee.discordauth.api.account.AccountManager;
import dev.orewaee.discordauth.api.key.KeyManager;
import dev.orewaee.discordauth.api.pool.PoolManager;
import dev.orewaee.discordauth.api.session.SessionManager;

public class Debug implements RawCommand {
    private final AccountManager accountManager;
    private final KeyManager keyManager;
    private final PoolManager poolManager;
    private final SessionManager sessionManager;

    public Debug(AccountManager accountManager, KeyManager keyManager, PoolManager poolManager, SessionManager sessionManager) {
        this.accountManager = accountManager;
        this.keyManager = keyManager;
        this.poolManager = poolManager;
        this.sessionManager = sessionManager;
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
