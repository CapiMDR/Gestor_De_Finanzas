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
    private static RecurringsController controller = new RecurringsController();
    private static Stage activeStage;

    /**
     * Initializes and shows the JavaFX interface for managing recurring movements.
     */
    public static void initRecurrings() {
        if (activeStage != null && activeStage.isShowing()) {
            activeStage.toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(RecurringsModule.class.getResource("/fxml/recurrings/recurrings.fxml"));
            Parent root = loader.load();
            RecurringsViewFX view = loader.getController();

            // Inject the view into the existing background controller
            controller.setView(view);

            activeStage = new Stage();
            activeStage.setTitle("Movimientos Recurrentes");
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(RecurringsModule.class.getResource("/styles/app.css").toExternalForm());
            activeStage.setScene(scene);

            // When the window is closed, we un-inject the view to avoid memory leaks,
            // but the controller stays alive to keep monitoring in the background.
            activeStage.setOnCloseRequest(e -> {
                controller.setView(null);
            });

            activeStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}