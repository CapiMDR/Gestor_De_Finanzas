package goals.goals_controller;

import java.math.BigDecimal;

import goals.goals_model.Goal;
import goals.goals_view.GoalEditViewFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class GoalEditController {
    private static final String STR_ERROR = "Error";

    public GoalEditController() {
        // Default constructor
    }

    public boolean showEditDialog(Goal goal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/goals/goal_edit.fxml"));
            DialogPane dialogPane = loader.load();
            GoalEditViewFX view = loader.getController();

            // Populate fields
            view.getTxtGoalName().setText(goal.getName());
            view.getTxtTargetAmount().setText(goal.getTargetAmount().toPlainString());
            view.getTxtDescription().setText(goal.getDescription());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Editar Meta");

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    try {
                        String newName = view.getTxtGoalName().getText().trim();
                        String newTargetStr = view.getTxtTargetAmount().getText().trim();
                        String newDesc = view.getTxtDescription().getText().trim();

                        if (newName.isEmpty() || newTargetStr.isEmpty()) {
                            showAlert(STR_ERROR, "Nombre y Monto son requeridos.");
                            return null;
                        }

                        BigDecimal newTarget = new BigDecimal(newTargetStr);
                        if (newTarget.compareTo(BigDecimal.ZERO) <= 0) {
                            showAlert(STR_ERROR, "El monto debe ser mayor a 0.");
                            return null;
                        }

                        goal.setName(newName);
                        goal.setTargetAmount(newTarget);
                        goal.setDescription(newDesc);

                        return ButtonType.OK;
                    } catch (NumberFormatException e) {
                        showAlert(STR_ERROR, "Monto inválido.");
                        return null;
                    }
                }
                return null;
            });

            return dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;

        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
