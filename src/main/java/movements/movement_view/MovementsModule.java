package movements.movement_view;

import accounts.account_model.Account;
import accounts.account_model.JsonDataHandler;
import movements.movement_controller.MovementController;
import movements.movement_model.CategoryManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * Entry point for the Movements JavaFX module.
 * Loads {@code movements.fxml}, instantiates {@link MovementsViewFX} via FXMLLoader,
 * then creates and wires {@link MovementController} to the view.
 *
 * Mirrors the original {@code MovementsModule} structure: same model/subject
 * setup, same controller constructor. Only the view type changes from
 * {@code MovementManagerView} (Swing) to {@code MovementsViewFX} (JavaFX).
 *
 * @author Capi Madera de Regil
 */
public class MovementsModule {

    private static Map<String, Stage> activeStages = new HashMap<>();

    /**
     * Initializes the entire movements module using the selected account.
     * Configures the Subject for notifications, the model that manages categories,
     * the view and the controller responsible for coordinating the interaction.
     *
     * @param selectedAccount the account on which the movements will be managed
     */
    public static void initMovements(Account selectedAccount) {
        if (selectedAccount == null) return;
        String accountId = selectedAccount.getName();
        if (activeStages.containsKey(accountId) && activeStages.get(accountId).isShowing()) {
            activeStages.get(accountId).toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                MovementsModule.class.getResource("/fxml/movements/movements.fxml"));

            Parent root = loader.load();

            // FXMLLoader created MovementsViewFX and injected @FXML fields
            MovementsViewFX movementView = loader.getController();

            JsonDataHandler testDataHandler = new JsonDataHandler();
            CategoryManager movementModel = new CategoryManager(testDataHandler);

            // Controller registers itself as Observer and wires button events
            MovementController controller = new MovementController(movementModel, movementView, selectedAccount);

            Stage stage = new Stage();
            activeStages.put(accountId, stage);
            stage.setTitle("Movimientos — " + selectedAccount.getName());
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(MovementsModule.class.getResource("/styles/app.css").toExternalForm());
            stage.setScene(scene);
            
            stage.setOnCloseRequest(e -> {
                activeStages.remove(accountId);
                movementModel.removeObserver(controller);
            });
            
            stage.show();

        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error de Carga");
            alert.setHeaderText("Error al abrir el módulo de Movimientos");
            alert.setContentText(e.getMessage() != null ? e.getMessage() : e.toString());
            alert.showAndWait();
        }
    }

    /**
     * Loads the movements view for the given account and returns its root node
     * for embedding inside an {@link accounts.account_view.AccountShell} tab.
     * No {@link Stage} is created.
     *
     * @param selectedAccount the account to show movements for
     * @return the root {@link javafx.scene.Node} of the movements view, or {@code null} on error
     */
    public static javafx.scene.Node loadForAccount(Account selectedAccount, Runnable onBack) {
        if (selectedAccount == null) return null;
        try {
            FXMLLoader loader = new FXMLLoader(
                MovementsModule.class.getResource("/fxml/movements/movements.fxml"));
            Parent root = loader.load();
            MovementsViewFX movementView = loader.getController();
            JsonDataHandler dataHandler = new JsonDataHandler();
            CategoryManager movementModel = new CategoryManager(dataHandler);
            
            MovementController controller = new MovementController(movementModel, movementView, selectedAccount);

            if (onBack != null) {
                movementView.setOnBack(() -> {
                    movementModel.removeObserver(controller);
                    onBack.run();
                });
            }

            root.getStylesheets().add(
                MovementsModule.class.getResource("/styles/app.css").toExternalForm());
            return root;
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            return null;
        }
    }
}