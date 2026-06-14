package goals.goals_view;

import accounts.account_model.Account;
import goals.goals_controller.GoalDetailControllerFX;
import goals.goals_controller.GoalEditController;
import goals.goals_controller.GoalsController;
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

            GoalsController controller = new GoalsController(view, editController, detailController);
            if (selectedAccount != null) {
                // Pass the account to the goals module
                controller.setAccount(selectedAccount);
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
                controller.dispose();
                activeStages.remove(accountId);
            });

            stage.show();
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
        }
    }

    /**
     * Loads the goals view for the given account and returns its root node
     * for embedding inside an {@link accounts.account_view.AccountShell} tab.
     *
     * @param selectedAccount the account to show goals for
     * @return the root node of the goals view, or {@code null} on error
     */
    public static javafx.scene.Node loadForAccount(Account selectedAccount, Runnable onBack) {
        if (selectedAccount == null) return null;
        try {
            FXMLLoader loader = new FXMLLoader(GoalsModule.class.getResource("/fxml/goals/goals.fxml"));
            Parent root = loader.load();
            GoalsViewFX view = loader.getController();

            GoalEditController editController = new GoalEditController();
            GoalDetailControllerFX detailController = new GoalDetailControllerFX();
            
            GoalsController controller = new GoalsController(view, editController, detailController);
            controller.setAccount(selectedAccount);
            
            if (onBack != null) {
                view.setOnBack(() -> {
                    controller.dispose();
                    onBack.run();
                });
            }

            root.getStylesheets().add(GoalsModule.class.getResource("/styles/app.css").toExternalForm());
            return root;
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            return null;
        }
    }
}