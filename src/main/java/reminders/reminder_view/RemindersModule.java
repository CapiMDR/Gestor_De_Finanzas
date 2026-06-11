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
    
    public static void initReminders() {
        try {
            FXMLLoader loader = new FXMLLoader(RemindersModule.class.getResource("/fxml/reminders.fxml"));
            Parent root = loader.load();

            RemindersViewFX view = loader.getController();
            view.setController(controller);
            controller.setView(view);

            Stage stage = new Stage();
            stage.setTitle("Recordatorios");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}