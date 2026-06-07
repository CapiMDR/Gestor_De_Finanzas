package com.mycompany.construccion;

import accounts.account_controller.AccountController;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountView;

/**
 * Main entry point of the application.
 * Initializes the account manager and displays the main account view.
 */
public class Main {
    public static void main(String[] args) {
        AccountManager.initAccountManager();
        AccountView accountsView = new AccountView();
        AccountController accountController = new AccountController(accountsView);
        AccountManager.loadInitialData();

        java.awt.EventQueue.invokeLater(() -> {
            accountsView.setVisible(true);
        });
    }
}
