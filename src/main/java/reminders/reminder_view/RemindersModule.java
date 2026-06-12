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
    
    /** Forces static initialization to start the background thread globally. */
    public static void initGlobalReminders() {}
    
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

    /**
     * Loads the reminders view and returns its root node for embedding
     * inside an {@link accounts.account_view.AccountShell} tab.
     * The existing background controller is reused.
     *
     * @param selectedAccount ignored for now — reminders are global
     * @return the root node of the reminders view, or {@code null} on error
     */
    public static javafx.scene.Node loadForAccount(accounts.account_model.Account selectedAccount, Runnable onBack) {
        if (selectedAccount == null) return null;
        try {
            FXMLLoader loader = new FXMLLoader(RemindersModule.class.getResource("/fxml/reminders/reminders.fxml"));
            Parent root = loader.load();

            RemindersViewFX view = loader.getController();
            if (selectedAccount != null) view.setAccountName(selectedAccount.getName());
            if (onBack != null) view.setOnBack(onBack);
            view.setController(controller);
            controller.setView(view);

            root.getStylesheets().add(RemindersModule.class.getResource("/styles/app.css").toExternalForm());
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}