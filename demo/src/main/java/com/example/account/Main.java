package com.example.account;

import com.example.account.account_cotroller.AccountController;
import com.example.account.account_model.AccountManager;
import com.example.account.account_model.AccountManagerSubject;
import com.example.account.account_model.JsonDataHandler;
import com.example.account.account_view.AccountView;

public class Main {

    public static void main(String args[]) {
        JsonDataHandler dataHandler = new JsonDataHandler(); 
        AccountManagerSubject subject = new AccountManagerSubject();
        AccountManager model = new AccountManager(subject, dataHandler);

        AccountView view = new AccountView();
        
        AccountController controller = new AccountController(model, view); 
        model.loadInitialData();
        
        java.awt.EventQueue.invokeLater(() -> {
            view.setVisible(true);
        });
    }
    
}