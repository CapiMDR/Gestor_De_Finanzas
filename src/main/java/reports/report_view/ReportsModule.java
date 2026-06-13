package reports.report_view;

import accounts.account_model.Account;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reports.controllerReport.ReportController;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportSubject;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for initializing the reports module (Dashboard).
 * Wires up the model, view, and controller for reporting functionalities
 * using JavaFX.
 */
public class ReportsModule {

   
    private static Map<String, Stage> activeStages = new HashMap<>();

    public static void initReportsModule(Account selectedAccount) {
        if (selectedAccount == null) return;
        String accountId = selectedAccount.getName();
        if (activeStages.containsKey(accountId) && activeStages.get(accountId).isShowing()) {
            activeStages.get(accountId).toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                ReportsModule.class.getResource("/fxml/reports/reports.fxml"));
            
            Parent root = loader.load();
            ReportsViewFX view = loader.getController();

            ReportSubject subject = new ReportSubject();
            ReportGenerator generator = new ReportGenerator(subject, selectedAccount);

            ReportController controller = new ReportController();
            controller.setViewModule(view, generator, selectedAccount);

            Stage stage = new Stage();
            activeStages.put(accountId, stage);
            stage.setTitle("Dashboard — " + selectedAccount.getName());
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(ReportsModule.class.getResource("/styles/app.css").toExternalForm());
            stage.setScene(scene);

            stage.setOnCloseRequest(e -> {
                controller.dispose();
                activeStages.remove(accountId);
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the reports dashboard view for the given account and returns its root node
     * for embedding inside an {@link accounts.account_view.AccountShell} tab.
     * No {@link Stage} is created.
     *
     * @param selectedAccount the account to show reports for
     * @return the root node of the reports view, or {@code null} on error
     */
    public static javafx.scene.Node loadForAccount(Account selectedAccount) {
        if (selectedAccount == null) return null;
        try {
            FXMLLoader loader = new FXMLLoader(
                ReportsModule.class.getResource("/fxml/reports/reports.fxml"));
            
            Parent root = loader.load();
            ReportsViewFX view = loader.getController();

            ReportSubject subject = new ReportSubject();
            ReportGenerator generator = new ReportGenerator(subject, selectedAccount);

            ReportController controller = new ReportController();
            controller.setViewModule(view, generator, selectedAccount);

            root.getStylesheets().add(ReportsModule.class.getResource("/styles/app.css").toExternalForm());
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}