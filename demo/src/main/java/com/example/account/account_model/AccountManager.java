package com.example.account.account_model;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private List<Account> accounts = new ArrayList<>();
    private AccountManagerSubject subject;

    public AccountManager(AccountManagerSubject subject) {
        this.subject = subject;
    }

    public void addAccount(Account account){
        accounts.add(account);
        subject.notifyObservers();;
    }

    public void removeAccount(int id){
        accounts.removeIf(account -> account.getId() == id);
        subject.notifyObservers();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public AccountManagerSubject getSubject() {
        return subject;
    }
    
    
}
