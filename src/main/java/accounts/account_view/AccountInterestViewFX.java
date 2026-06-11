package accounts.account_view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * JavaFX View Controller for the Account Interest dialog.
 * Handles pure UI components and provides accessors for the actual Controller.
 */
public class AccountInterestViewFX {

    @FXML private TextField txtInitialBalance;
    @FXML private TextField txtInterestRate;
    @FXML private TextField txtTimeYears;
    @FXML private Label lblFutureBalance;

    // ── Getters for Controller to read/write data ──────────────────────────

    public TextField getTxtInitialBalance() { return txtInitialBalance; }
    public TextField getTxtInterestRate() { return txtInterestRate; }
    public TextField getTxtTimeYears() { return txtTimeYears; }
    public Label getLblFutureBalance() { return lblFutureBalance; }
}
