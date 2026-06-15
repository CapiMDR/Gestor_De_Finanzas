package accounts.account_controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountEditViewFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

/**
 * Controller for the Account Edit dialog.
 * Loads the FXML, sets up the view, and handles business logic when confirmed.
 */
public class AccountEditController {

    private final Account accountToEdit;
    private final Dialog<ButtonType> dialog;
    private AccountEditViewFX view;

    private static final String ERROR_TITLE = "Error";

    public AccountEditController(Account accountToEdit) {
        this.accountToEdit = accountToEdit;
        this.dialog = new Dialog<>();
        initDialog();
    }

    private void initDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accounts/account_edit.fxml"));
            DialogPane pane = loader.load();
            this.view = loader.getController();

            dialog.setDialogPane(pane);
            dialog.setTitle("Editar cuenta");
            dialog.setHeaderText("Editar: " + accountToEdit.getName());

            // Pre-fill form
            view.getTxtNameAccount().setText(accountToEdit.getName());
            String typeStr = accountToEdit.getType().toString();
            if ("DIGITAL".equalsIgnoreCase(typeStr)) typeStr = "Digital";
            else if ("CASH".equalsIgnoreCase(typeStr) || "EFECTIVO".equalsIgnoreCase(typeStr)) typeStr = "Efectivo";
            view.getCmbAccountType().setValue(typeStr);
            view.getCmbCurrency().setValue(accountToEdit.getCoin().toString());

        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
        }
    }

    public void show() {
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                handleConfirmEdit();
            }
        });
    }

    private void handleConfirmEdit() {
        String newName = view.getTxtNameAccount().getText().trim();
        String newTypeStr = view.getCmbAccountType().getValue();
        String newCoinStr = view.getCmbCurrency().getValue();

        if (newName.isEmpty() || newTypeStr == null || newCoinStr == null) {
            showAlert(AlertType.WARNING, "Error de Validación", "Debe completar todos los campos.");
            return;
        }

        try {
            Account.AccountType newType;
            if ("Efectivo".equalsIgnoreCase(newTypeStr)) {
                newType = Account.AccountType.CASH;
            } else {
                newType = Account.AccountType.valueOf(newTypeStr.toUpperCase());
            }
            Account.Coin newCoin = Account.Coin.valueOf(newCoinStr.toUpperCase());

            AccountManager.editAccount(accountToEdit, newName, newType, newCoin);
            
            showAlert(AlertType.INFORMATION, "Éxito", "Cuenta '" + newName + "' actualizada.");

        } catch (IllegalArgumentException ex) {
            showAlert(AlertType.ERROR, "Error de Configuración", "Error de mapeo. Verifique que Tipo o Moneda seleccionados sean válidos.");
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, ERROR_TITLE, "Error inesperado al editar la cuenta: " + ex.getMessage());
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }
}
