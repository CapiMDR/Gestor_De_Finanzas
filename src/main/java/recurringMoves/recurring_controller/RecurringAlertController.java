package recurringMoves.recurring_controller;

import java.util.List;

import accounts.account_model.Account;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import recurringMoves.recurring_model.RecurringMove;
import recurringMoves.recurring_view.RecurringAlertViewFX;

public class RecurringAlertController {

    public Account showAlertDialog(RecurringMove recMove, List<Account> accounts) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recurrings/recurring_alert.fxml"));
            DialogPane dialogPane = loader.load();
            RecurringAlertViewFX view = loader.getController();

            // Populate fields
            view.getLblConcept().setText("Concepto: " + recMove.getConcept());
            view.getLblAmount().setText("Monto: $" + recMove.getAmount().toPlainString());

            // Populate accounts
            for (Account account : accounts) {
                view.getCmbAccounts().getItems().add(account.getName());
            }

            if (!accounts.isEmpty()) {
                view.getCmbAccounts().getSelectionModel().select(0);
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Alerta de Pago Recurrente");

            // Convert result
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.APPLY) {
                    return ButtonType.APPLY;
                }
                return null;
            });

            if (dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.APPLY) {
                String selectedAccountName = view.getCmbAccounts().getSelectionModel().getSelectedItem();
                for (Account account : accounts) {
                    if (account.getName().equals(selectedAccountName)) {
                        return account;
                    }
                }
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
