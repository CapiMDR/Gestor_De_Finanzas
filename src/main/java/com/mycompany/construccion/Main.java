package com.mycompany.construccion;

import accounts.account_controller.AccountController;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountView;

/**
 *
 * @author villa
 */

// MAIN PRINCIPAL, LLAMAR ESTO ANTES QUE TODOOOOOO
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
