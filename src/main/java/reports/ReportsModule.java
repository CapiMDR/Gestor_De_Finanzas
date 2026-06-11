package reports;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reports.controllerReport.ReportController;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportSubject;
import reports.report_view.ReportsViewFX;

import java.util.HashMap;
import java.util.Map;

/**
 * Main class for initializing the reports module (Dashboard).
 * Wires up the model, view, and controller for reporting functionalities
 * using JavaFX.
 */
public class ReportsModule {

   
    private static ReportController reportController;
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

            reportController = new ReportController();
            reportController.setViewModule(view, generator, selectedAccount);

            Stage stage = new Stage();
            activeStages.put(accountId, stage);
            stage.setTitle("Dashboard — " + selectedAccount.getName());
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(ReportsModule.class.getResource("/styles/app.css").toExternalForm());
            stage.setScene(scene);

            // Unregister observer when closing the window to prevent accumulation
            // of dead observers and duplicated notifications
            stage.setOnCloseRequest(e -> {
                AccountManagerSubject.removeObserver(reportController);
                activeStages.remove(accountId);
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}