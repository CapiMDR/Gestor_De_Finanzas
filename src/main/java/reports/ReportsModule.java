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

/**
 * Main class for initializing the reports module (Dashboard).
 * Wires up the model, view, and controller for reporting functionalities
 * using JavaFX.
 */
public class ReportsModule {

   
    private static ReportController reportController;

    public static void initReportsModule(Account selectedAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(
                ReportsModule.class.getResource("/fxml/reports.fxml"));
            
            Parent root = loader.load();
            ReportsViewFX view = loader.getController();

            ReportSubject subject = new ReportSubject();
            ReportGenerator generator = new ReportGenerator(subject, selectedAccount);

            reportController = new ReportController();
            reportController.setViewModule(view, generator, selectedAccount);

            Stage stage = new Stage();
            stage.setTitle("Dashboard — " + selectedAccount.getName());
            stage.setScene(new Scene(root, 1100, 750));

            // Unregister observer when closing the window to prevent accumulation
            // of dead observers and duplicated notifications
            stage.setOnCloseRequest(e -> {
                AccountManagerSubject.removeObserver(reportController);
            });

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}