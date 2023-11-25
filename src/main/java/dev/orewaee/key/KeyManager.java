package dev.orewaee.key;

import org.jetbrains.annotations.Nullable;

import dev.orewaee.account.Account;

import java.util.Map;

public interface KeyManager {
    void addKey(Account account, Key key);
    void removeKey(Account account);

    @Nullable
    Key getKeyByAccount(Account account);

    boolean containsKeyByCode(String code);

    Map<Account, Key> getKeys();
}
