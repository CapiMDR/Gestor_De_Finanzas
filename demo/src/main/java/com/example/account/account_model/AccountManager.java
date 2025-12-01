package com.example.account.account_model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.account.account_model.Account.AccountType;
import com.example.account.account_model.Account.Coin;

public class AccountManager {
    private List<Account> accounts = new ArrayList<>();
    private AccountManagerSubject subject;
    private JsonDataHandler dataHandler;

    
    //public AccountManager(AccountManagerSubject subject){
    //    this.dataHandler = new JsonDataHandler();
    //    this.accounts = dataHandler.loadAccounts();
    //    this.subject = subject;
    //}

    public AccountManager(AccountManagerSubject subject, JsonDataHandler dataHandler){
        this.dataHandler = dataHandler;
        this.accounts = dataHandler.loadAccounts();
        this.subject = subject;
    }

    public void loadInitialData(){
        notifyObservers();
    }

    public void saveAccountsData(){
        dataHandler.saveAccounts(accounts);
    }

    public void addAccount(String name, AccountType type, Coin coin, BigDecimal initialBalace){
        Account newAccount = new Account(generateUniqueId(), name, type, coin, initialBalace);

        accounts.add(newAccount);
        saveAccountsData();
        notifyObservers();
    }

    public void removeAccount(int id){
        accounts.removeIf(account -> account.getId() == id);
        saveAccountsData();
        notifyObservers();
    }
    public void editAccount(Account account, String name, AccountType type, Coin coin){
        account.setName(name);
        account.setType(type);
        account.setCoin(coin);
        saveAccountsData();
        notifyObservers();
    }

    private int generateUniqueId(){
        int maxId = 0;
        for (Account account : accounts) {
            if (account.getId() > maxId) {
                maxId = account.getId();
            }
        }
        return maxId + 1;
    }

    public Account getAccountByIndex(int index){
        if (index >= 0 && index < accounts.size()) {
            return accounts.get(index);
        }
        return null;
    }

    public Account getAccountById(int id){
        return accounts.stream()
                        .filter(a -> a.getId() == id)
                        .findFirst()
                        .orElse(null);    
    }
        
    public void addObserver(AccountObserver observer){
        subject.addObserver(observer);
    }
    public void removeObserver(AccountObserver observer){
        subject.removeObserver(observer);
    }
    private void notifyObservers(){
        subject.notifyObservers(accounts);
    }
    
    public List<Account> getAccounts() {
        return accounts;
    }

    public AccountManagerSubject getSubject() {
        return subject;
    }
    
    
}
