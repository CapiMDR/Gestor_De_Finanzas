package com.example.account.account_cotroller;

import java.math.BigDecimal;
import java.util.List;

import com.example.account.account_model.Account.AccountType;
import com.example.account.account_model.Account.Coin;
import com.example.account.account_model.Account;
import com.example.account.account_model.AccountManager;
import com.example.account.account_model.AccountObserver;
import com.example.account.account_view.AccountView;

public class AccountController implements AccountObserver{
    private AccountManager model;
    private AccountView view;

    public AccountController(AccountManager model){
        this.model = model;
        this.view = view;
        this.model.addObserver(this);
        AssignEvents();
    }

    private void AssignEvents() {
        //eventos donde cotroller será listener
    }

    public void addAccount(String name, AccountType type, Coin coin, BigDecimal balance){
        model.addAccount(name, type, coin, balance);
    }
    public void removeAccount(int idAccount){
        model.removeAccount(idAccount);
    }
    public void editAccount(Account account, String name, AccountType type, Coin coin){
        model.editAccount(account, name, type, coin);
    }

   @Override
    public void onNotify(List<Account> accountsList) {
        // código que actualizo en view
        
    }
}
