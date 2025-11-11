package com.example.account.account_cotroller;

import java.math.BigDecimal;

import com.example.account.account_model.Account.AccountType;
import com.example.account.account_model.Account.Coin;
import com.example.account.account_model.Account;
import com.example.account.account_model.AccountManager;
import com.example.account.account_model.AccountObserver;
import com.example.account.account_view.AccountView;

public class AccountController implements AccountObserver{
    private AccountManager model;
    private AccountView view;

    public AccountController(AccountManager model, AccountView view){
        this.model = model;
        this.view = view;
        this.model.addObserver(this);
        
    }

    public void addAccount(String name, AccountType type, Coin coin, BigDecimal balance){
        Account account = new Account(model.getAccounts().size()+1, name, type, coin, balance);
        model.addAccount(account);
    }

    public void showMessage(String message){
        view.method(message);
    }

    @Override
    public void update() {
        // código que actualizo en view
        
    }
}
