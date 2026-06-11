package recurringMoves.recurring_view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import recurringMoves.recurring_controller.RecurringsController;

/**
 * Main class of the recurring movements module.
 * Holds the background controller and provides a method to open the JavaFX UI.
 */
public class RecurringsModule {
    
    // The controller is initialized statically so the ScheduledExecutorService
    // starts monitoring as soon as the application launches, without needing
    // the UI to be open.
    public static RecurringsController controller = new RecurringsController();

    /**
     * Initializes and shows the JavaFX interface for managing recurring movements.
     */
    public static void initRecurrings() {
        try {
            FXMLLoader loader = new FXMLLoader(RecurringsModule.class.getResource("/fxml/recurrings.fxml"));
            Parent root = loader.load();
            RecurringsViewFX view = loader.getController();

            // Inject the view into the existing background controller
            controller.setView(view);

            Stage stage = new Stage();
            stage.setTitle("Movimientos Recurrentes");
            stage.setScene(new Scene(root, 900, 600));

            // When the window is closed, we un-inject the view to avoid memory leaks,
            // but the controller stays alive to keep monitoring in the background.
            stage.setOnCloseRequest(e -> {
                controller.setView(null);
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}