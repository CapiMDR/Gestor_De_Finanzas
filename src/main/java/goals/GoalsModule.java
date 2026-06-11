package goals;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import goals.goals_controller.GoalDetailControllerFX;
import goals.goals_controller.GoalEditController;
import goals.goals_controller.GoalsController;
import goals.goals_view.GoalsViewFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * Main entry point of the Goals module (JavaFX).
 * Integrates the Accounts and Goals modules.
 *
 * @author Team Integration
 */
public class GoalsModule {


    private static GoalsController goalsController;
    private static Map<String, Stage> activeStages = new HashMap<>();

    public static void initGoals(Account selectedAccount) {
        if (selectedAccount == null) return;
        String accountId = selectedAccount.getName();
        if (activeStages.containsKey(accountId) && activeStages.get(accountId).isShowing()) {
            activeStages.get(accountId).toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(GoalsModule.class.getResource("/fxml/goals/goals.fxml"));
            Parent root = loader.load();
            GoalsViewFX view = loader.getController();

            GoalEditController editController = new GoalEditController();
            GoalDetailControllerFX detailController = new GoalDetailControllerFX();

            goalsController = new GoalsController(view, editController, detailController);
            if (selectedAccount != null) {
                // Pass the account to the goals module
                goalsController.setAccount(selectedAccount);
            }

            Stage stage = new Stage();
            activeStages.put(accountId, stage);
            stage.setTitle("Metas Financieras");
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(GoalsModule.class.getResource("/styles/app.css").toExternalForm());
            stage.setScene(scene);

            // Unregister observer when closing the window to prevent accumulation
            // of dead observers and duplicated notifications
            stage.setOnCloseRequest(e -> {
                AccountManagerSubject.removeObserver(goalsController);
                activeStages.remove(accountId);
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}