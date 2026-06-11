package accounts.account_view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * JavaFX View Controller for the Account Edit dialog.
 * Handles pure UI components and provides accessors for the actual Controller.
 */
public class AccountEditViewFX {

    @FXML private TextField txtNameAccount;
    @FXML private ComboBox<String> cmbAccountType;
    @FXML private ComboBox<String> cmbCurrency;

    /**
     * Automatically called after FXML fields are injected.
     */
    @FXML
    public void initialize() {
        cmbAccountType.setItems(FXCollections.observableArrayList("Efectivo", "Digital"));
        cmbCurrency.setItems(FXCollections.observableArrayList("MXN", "USD"));

        // Limit account name to 18 characters
        txtNameAccount.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 18) {
                return null;
            }
            return change;
        }));
    }

    // ── Getters for Controller to read/write data ──────────────────────────

    public TextField getTxtNameAccount() { return txtNameAccount; }
    public ComboBox<String> getCmbAccountType() { return cmbAccountType; }
    public ComboBox<String> getCmbCurrency() { return cmbCurrency; }
}
