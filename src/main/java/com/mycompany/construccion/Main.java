package com.mycompany.construccion;

import accounts.account_controller.AccountController;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point of the application.
 * Initializes the account manager and displays the main account view.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Application starting...");
        AccountManager.initAccountManager();
        AccountView accountsView = new AccountView();
        AccountController accountController = new AccountController(accountsView);
        AccountManager.loadInitialData();

        java.awt.EventQueue.invokeLater(() -> {
            accountsView.setVisible(true);
            logger.info("Main view displayed successfully.");
        });
    }
}
