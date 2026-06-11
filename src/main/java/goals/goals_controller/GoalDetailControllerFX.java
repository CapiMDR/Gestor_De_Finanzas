package goals.goals_controller;

import java.math.BigDecimal;

import goals.goals_model.Goal;
import goals.goals_view.GoalDetailViewFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class GoalDetailControllerFX {

    public void showDetails(Goal goal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/goals/goal_detail.fxml"));
            DialogPane dialogPane = loader.load();
            GoalDetailViewFX view = loader.getController();

            // Populate fields
            view.getLblName().setText(goal.getName());
            view.getLblDescription().setText(goal.getDescription());
            view.getLblTargetAmount().setText("$" + goal.getTargetAmount().toPlainString());
            view.getLblCurrentAmount().setText("$" + goal.getCurrentAmount().toPlainString());

            // Calculate progress and remaining
            BigDecimal target = goal.getTargetAmount();
            BigDecimal current = goal.getCurrentAmount();
            BigDecimal remaining = target.subtract(current);
            if (remaining.compareTo(BigDecimal.ZERO) < 0) remaining = BigDecimal.ZERO;

            view.getLblRemainingAmount().setText("$" + remaining.toPlainString());

            double progress = 0.0;
            if (target.compareTo(BigDecimal.ZERO) > 0) {
                progress = current.divide(target, 4, java.math.RoundingMode.HALF_UP).doubleValue();
            }
            if (progress > 1.0) progress = 1.0;
            view.getProgressBar().setProgress(progress);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Detalles de la Meta");

            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
