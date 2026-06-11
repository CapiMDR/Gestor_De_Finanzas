package accounts.account_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the Accounts view.
 * Replaces {@code AccountView.java} (Swing).
 *
 * <p>Layout is defined declaratively in {@code /fxml/account.fxml}.
 * Business logic is delegated to {@code AccountController}.
 *
 * @see accounts.account_controller.AccountController
 */
public class AccountViewFX {

    @FXML private ListView<String> listAccounts;
    @FXML private TextField txtNameAccount;
    @FXML private TextField txtInitialBalance;
    @FXML private Button btnAddAccount;
    @FXML private Button btnEditAccount;
    @FXML private Button btnDeleteAccount;
    @FXML private Button btnAccessAccount;
    @FXML private Button btnCalculateInterest;

    // TODO: Implement — Phase 3.4 (Accounts module migration)
}
