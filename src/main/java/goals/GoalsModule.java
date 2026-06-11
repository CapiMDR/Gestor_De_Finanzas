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

/**
 * Main entry point of the Goals module (JavaFX).
 * Integrates the Accounts and Goals modules.
 *
 * @author Team Integration
 */
public class GoalsModule {

    @SuppressWarnings("unused")
    private static GoalsController goalsController;

    public static void initGoals(Account selectedAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(GoalsModule.class.getResource("/fxml/goals.fxml"));
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
            stage.setTitle("Metas Financieras");
            stage.setScene(new Scene(root, 900, 600));

            // Unregister observer when closing the window to prevent accumulation
            // of dead observers and duplicated notifications
            stage.setOnCloseRequest(e -> {
                AccountManagerSubject.removeObserver(goalsController);
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}