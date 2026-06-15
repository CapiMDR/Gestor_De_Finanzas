package recurrings.recurring_controller;

import java.math.BigDecimal;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import movements.movement_model.MovementCategory;
import recurrings.recurring_model.RecurringMove;
import recurrings.recurring_view.RecurringEditViewFX;

public class RecurringEditController {
    private static final String STR_INGRESO = "Ingreso";
    private static final String STR_EGRESO = "Egreso";
    private static final String STR_ERROR = "Error";

    public RecurringMove showEditDialog(RecurringMove oldRecMove) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/recurrings/recurring_edit.fxml"));
            DialogPane dialogPane = loader.load();
            RecurringEditViewFX view = loader.getController();

            populateFields(oldRecMove, view);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Editar Movimiento Recurrente");

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK && validateInput(view)) {
                    return ButtonType.OK;
                }
                return null;
            });

            if (dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                return createRecurringMove(oldRecMove, view);
            }

            return null;

        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            return null;
        }
    }

    private void populateFields(RecurringMove oldRecMove, RecurringEditViewFX view) {
        view.getCmbCategory().getItems().addAll(STR_INGRESO, STR_EGRESO);
        view.getTxtDescription().setText(oldRecMove.getDescription());
        view.getTxtAmount().setText(oldRecMove.getAmount().toPlainString());
        if (oldRecMove.getCategory() != null && oldRecMove.getCategory().getType() != null) {
            String typeStr = oldRecMove.getCategory().getType().name();
            if ("INCOME".equals(typeStr)) view.getCmbCategory().getSelectionModel().select(STR_INGRESO);
            else if ("EXPENSE".equals(typeStr)) view.getCmbCategory().getSelectionModel().select(STR_EGRESO);
        }
        view.getDatePicker().setValue(oldRecMove.getInitialDate().toLocalDate());
    }

    private boolean validateInput(RecurringEditViewFX view) {
        String desc = view.getTxtDescription().getText().trim();
        String amountStr = view.getTxtAmount().getText().trim();
        String categoryStr = view.getCmbCategory().getSelectionModel().getSelectedItem();
        java.time.LocalDate date = view.getDatePicker().getValue();

        if (desc.isEmpty() || amountStr.isEmpty() || categoryStr == null || date == null) {
            showAlert(STR_ERROR, "Todos los campos son requeridos.");
            return false;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert(STR_ERROR, "El monto debe ser mayor a 0.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert(STR_ERROR, "Monto inválido.");
            return false;
        }
    }

    private RecurringMove createRecurringMove(RecurringMove oldRecMove, RecurringEditViewFX view) {
        String desc = view.getTxtDescription().getText().trim();
        BigDecimal amount = new BigDecimal(view.getTxtAmount().getText().trim());
        String categoryStr = view.getCmbCategory().getSelectionModel().getSelectedItem();
        String typeEnumStr = STR_INGRESO.equals(categoryStr) ? "INCOME" : "EXPENSE";
        MovementCategory category = new MovementCategory(typeEnumStr, MovementCategory.MovementType.valueOf(typeEnumStr));
        java.time.LocalDate date = view.getDatePicker().getValue();

        return new RecurringMove(
            oldRecMove.getConcept(),
            amount,
            desc,
            date.atStartOfDay(),
            oldRecMove.getRecurrence(),
            category
        );
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
