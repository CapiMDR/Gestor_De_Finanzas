package accounts.account_view;

import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignD;
import org.kordamp.ikonli.materialdesign2.MaterialDesignP;

import accounts.account_model.Account;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the Account Management view.
 * Replaces {@code AccountView.java} (Swing).
 *
 * <p>
 * This controller cooperates with {@link AccountController}, which holds
 * the full business logic. The view only handles UI interaction:
 * reading form fields, updating the list and showing dialogs.
 *
 * @see accounts.account_controller.AccountController
 */
public class AccountViewFX {

    // ── Form: Create account ─────────────────────────────────────────────────
    @FXML
    private TextField txtNameAccount;
    @FXML
    private ComboBox<String> cmbAccountType;
    @FXML
    private ComboBox<String> cmbCurrency;
    @FXML
    private TextField txtInitialBalance;
    @FXML
    private Button btnAddAccount;

    // ── Account list ─────────────────────────────────────────────────────────
    @FXML
    private ListView<String> listAccounts;

    // ── Action buttons ───────────────────────────────────────────────────────
    @FXML
    private Button btnEditAccount;
    @FXML
    private Button btnDeleteAccount;
    @FXML
    private Button btnCalculateInterest;
    @FXML
    private Button btnAccessAccount;

    /**
     * Called automatically by JavaFX after all {@code @FXML} fields are injected.
     * Populates combo boxes with static options and sets Ikonli icons on action
     * buttons.
     */
    @FXML
    public void initialize() {
        cmbAccountType.setItems(FXCollections.observableArrayList("Efectivo", "Digital"));
        cmbCurrency.setItems(FXCollections.observableArrayList("MXN", "USD"));

        FontIcon iconEdit = new FontIcon(MaterialDesignP.PENCIL);
        FontIcon iconDelete = new FontIcon(MaterialDesignD.DELETE);
        FontIcon iconCalc = new FontIcon(MaterialDesignC.CALCULATOR);

        iconEdit.setIconSize(20);
        iconDelete.setIconSize(20);
        iconCalc.setIconSize(20);

        btnEditAccount.setGraphic(iconEdit);
        btnDeleteAccount.setGraphic(iconDelete);
        btnCalculateInterest.setGraphic(iconCalc);
    }

    // ── Public methods called by AccountController / Observer ────────────────

    /**
     * Refreshes the accounts list from the updated model data.
     * Mirrors {@code AccountView#updateAccountList(List)}.
     *
     * @param accounts the current list of accounts
     */
    public void updateAccountList(List<Account> accounts) {
        List<String> items = accounts.stream()
                .map(a -> String.format("%s  —  %.2f %s",
                        a.getName(), a.getCurrentBalance(), a.getCoin()))
                .toList();
        Platform.runLater(() -> listAccounts.setItems(FXCollections.observableArrayList(items)));
    }

    // ── Form data accessors (used by AccountController) ───────────────────────

    /**
     * Returns the account name typed by the user.
     *
     * @return trimmed account name, or empty string if blank
     */
    public String getAccountName() {
        return txtNameAccount.getText().trim();
    }

    /**
     * Returns the initial balance string entered by the user.
     * The controller is responsible for numeric parsing and validation.
     *
     * @return raw text from the balance field
     */
    public String getInitialBalanceText() {
        return txtInitialBalance.getText().trim();
    }

    /**
     * Returns the account type selected in the combo box.
     *
     * @return "Cash" or "Digital", or {@code null} if nothing is selected
     */
    public String getSelectedAccountType() {
        return cmbAccountType.getValue();
    }

    /**
     * Returns the currency selected in the combo box.
     *
     * @return "MXN" or "USD", or {@code null} if nothing is selected
     */
    public String getSelectedCurrency() {
        return cmbCurrency.getValue();
    }

    /**
     * Returns the index of the currently selected account in the ListView.
     *
     * @return 0-based index, or -1 if no account is selected
     */
    public int getSelectedAccountIndex() {
        return listAccounts.getSelectionModel().getSelectedIndex();
    }

    /**
     * Clears all form fields after a successful account operation.
     */
    public void clearForm() {
        txtNameAccount.clear();
        txtInitialBalance.clear();
        cmbAccountType.getSelectionModel().clearSelection();
        cmbCurrency.getSelectionModel().clearSelection();
    }

    // ── Dialog helpers (replace JOptionPane) ─────────────────────────────────

    /**
     * Shows an informational alert dialog.
     *
     * @param title   dialog title
     * @param message message body
     */
    public void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    /**
     * Shows an error alert dialog.
     *
     * @param title   dialog title
     * @param message error message body
     */
    public void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    /**
     * Shows a warning alert dialog.
     *
     * @param title   dialog title
     * @param message warning message body
     */
    public void showWarning(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    /**
     * Shows a yes/no confirmation dialog.
     *
     * @param title   dialog title
     * @param message confirmation question
     * @return {@code true} if the user clicked YES
     */
    public boolean showConfirm(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().filter(r -> r == ButtonType.YES).isPresent();
    }

    // ── Button getters (used by AccountController#AssignEvents) ──────────────
    // AccountController wires event handlers via setOnAction() to preserve the
    // original Observer architecture where the controller owns all business logic.

    /** @return the "Agregar cuenta" button */
    public Button getBtnAddAccount() {
        return btnAddAccount;
    }

    /** @return the delete icon button */
    public Button getBtnDeleteAccount() {
        return btnDeleteAccount;
    }

    /** @return the edit (pencil) icon button */
    public Button getBtnEditAccount() {
        return btnEditAccount;
    }

    /** @return the interest calculator icon button */
    public Button getBtnCalculateInterest() {
        return btnCalculateInterest;
    }

    /** @return the "Entrar" access button */
    public Button getBtnAccessAccount() {
        return btnAccessAccount;
    }
}
