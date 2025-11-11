package com.example.account.account_view;

import com.example.account.account_cotroller.AccountController;
import com.example.account.account_model.AccountObserver;
import java.util.List;
import com.example.account.account_model.Account;

public class AccountView implements AccountObserver{
    AccountController controller;

    @Override
    public void update(){
        System.out.println("updated");
    }

    public void showAccounts(List<Account> accounts){
        for (Account account : accounts) {
            System.out.println(account);
        }
    }
}
