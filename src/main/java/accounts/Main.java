package accounts;

import accounts.account_controller.AccountController;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.JsonDataHandler;
import accounts.account_view.AccountView;

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