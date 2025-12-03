package accounts;

import accounts.account_controller.AccountController;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountView;

public class Main {

    public static void main(String args[]) {
        AccountView view = new AccountView();

        AccountController controller = new AccountController(view);
        AccountManager.loadInitialData();

        java.awt.EventQueue.invokeLater(() -> {
            view.setVisible(true);
        });
    }

}