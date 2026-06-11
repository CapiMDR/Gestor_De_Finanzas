package accounts.account_controller;

import javax.swing.JOptionPane;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountEditViewFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
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

    public AccountEditController(Account accountToEdit) {
        this.accountToEdit = accountToEdit;
        this.dialog = new Dialog<>();
        initDialog();
    }

    private void initDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/account_edit.fxml"));
            DialogPane pane = loader.load();
            this.view = loader.getController();

            dialog.setDialogPane(pane);
            dialog.setTitle("Editar cuenta");
            dialog.setHeaderText("Editar: " + accountToEdit.getName());

            // Pre-fill form
            view.getTxtNameAccount().setText(accountToEdit.getName());
            view.getCmbAccountType().setValue(accountToEdit.getType().toString());
            view.getCmbCurrency().setValue(accountToEdit.getCoin().toString());

        } catch (Exception e) {
            e.printStackTrace();
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
            JOptionPane.showMessageDialog(null,
                    "Debe completar todos los campos.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Account.AccountType newType = Account.AccountType.valueOf(newTypeStr.toUpperCase());
            Account.Coin newCoin = Account.Coin.valueOf(newCoinStr.toUpperCase());

            AccountManager.editAccount(accountToEdit, newName, newType, newCoin);
            
            JOptionPane.showMessageDialog(null,
                    "Cuenta '" + newName + "' actualizada.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error de mapeo. Verifique que Tipo o Moneda seleccionados sean válidos.",
                    "Error de Configuración", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error inesperado al editar la cuenta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
