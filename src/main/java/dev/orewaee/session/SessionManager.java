package dev.orewaee.session;

import dev.orewaee.account.Account;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface SessionManager {
    void addSession(Account account, Session session);
    void removeSession(Account account);

    @Nullable
    Session getSessionByAccount(Account account);

    // ?
    boolean containsSessionByAccount(Account account);
    boolean containsSessionByIp(String ip);

    Map<Account, Session> getSessions();
}
