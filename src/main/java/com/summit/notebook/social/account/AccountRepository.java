package com.summit.notebook.social.account;

public interface AccountRepository {

    void createAccount(Account account) throws UsernameAlreadyInUseException;

    Account findAccountByUsername(String username);

}
