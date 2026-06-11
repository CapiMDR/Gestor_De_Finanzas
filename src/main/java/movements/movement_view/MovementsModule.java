package movements.movement_view;

import accounts.account_model.Account;
import accounts.account_model.JsonDataHandler;
import movements.movement_controller.MovementController;
import movements.movement_model.CategoryManager;
import movements.movement_model.MovementManagerSubject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @SuppressWarnings("unused")
    private static MovementController movementController;

    /**
     * Initializes the entire movements module using the selected account.
     * Configures the Subject for notifications, the model that manages categories,
     * the view and the controller responsible for coordinating the interaction.
     *
     * @param selectedAccount the account on which the movements will be managed
     */
    public static void initMovements(Account selectedAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(
                MovementsModule.class.getResource("/fxml/movements.fxml"));

            Parent root = loader.load();

            // FXMLLoader created MovementsViewFX and injected @FXML fields
            MovementsViewFX movementView = loader.getController();

            JsonDataHandler testDataHandler = new JsonDataHandler();
            MovementManagerSubject movementSubject = new MovementManagerSubject();
            CategoryManager movementModel = new CategoryManager(movementSubject, testDataHandler);

            // Controller registers itself as Observer and wires button events
            movementController = new MovementController(movementModel, movementView, selectedAccount);

            Stage stage = new Stage();
            stage.setTitle("Movimientos — " + selectedAccount.getName());
            stage.setScene(new Scene(root, 960, 680));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}