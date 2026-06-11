package reminders.reminder_view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reminders.reminder_controller.RemindersController;

import java.io.IOException;

/**
 * Main class of the reminders module.
 * Initializes the controller for managing reminders.
 */
public class RemindersModule {
    
    // Static controller to keep the scheduler running
    public static final RemindersController controller = new RemindersController();
    
    private static Stage activeStage;
    
    public static void initReminders() {
        if (activeStage != null && activeStage.isShowing()) {
            activeStage.toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(RemindersModule.class.getResource("/fxml/reminders/reminders.fxml"));
            Parent root = loader.load();

            RemindersViewFX view = loader.getController();
            view.setController(controller);
            controller.setView(view);

            activeStage = new Stage();
            activeStage.setTitle("Recordatorios");
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(RemindersModule.class.getResource("/styles/app.css").toExternalForm());
            activeStage.setScene(scene);
            activeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}