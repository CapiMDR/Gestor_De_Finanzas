package recurrings.recurring_view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import recurrings.recurring_controller.RecurringsController;

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
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
        }
    }

    /**
     * Loads the recurring movements view and returns its root node for embedding
     * inside an {@link accounts.account_view.AccountShell} tab.
     * The existing background controller is reused (same scheduler, same view injection).
     *
     * @param selectedAccount ignored for now — recurrings are global, not per-account
     * @param onBack callback to run when back button is pressed
     * @return the root node of the recurrings view, or {@code null} on error
     */
    public static javafx.scene.Node loadForAccount(accounts.account_model.Account selectedAccount, Runnable onBack) {
        if (selectedAccount == null) return null;
        try {
            FXMLLoader loader = new FXMLLoader(RecurringsModule.class.getResource("/fxml/recurrings/recurrings.fxml"));
            Parent root = loader.load();
            RecurringsViewFX view = loader.getController();
            if (selectedAccount != null) view.setAccountName(selectedAccount.getName());
            if (onBack != null) {
                view.setOnBack(() -> {
                    controller.setView(null);
                    onBack.run();
                });
            }
            controller.setView(view);
            root.getStylesheets().add(RecurringsModule.class.getResource("/styles/app.css").toExternalForm());
            return root;
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            return null;
        }
    }

    /** Forces static initialization so the background scheduler starts at app launch. */
    public static void initGlobalRecurrings() {}
}