package reports;

import com.mycompany.construccion.FrmMain;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import reports.controllerReport.ReportController;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportSubject;

/**
 * Main class for initializing the reports module.
 * Wires up the model, view, and controller for reporting functionalities.
 */
public class ReportsModule {

    public static void initReportsModule(Account selectedAccount) {
        FrmMain reportsView = new FrmMain(selectedAccount);
        ReportSubject subject = new ReportSubject();
        
        ReportGenerator generator = new ReportGenerator(subject, selectedAccount);

        ReportController controller = new ReportController();
        controller.setViewModule(reportsView, generator, selectedAccount);

        // Unregister observer when closing the window to prevent accumulation
        // of dead observers and duplicated notifications
        reportsView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                AccountManagerSubject.removeObserver(controller);
            }
        });

        reportsView.setVisible(true);
    }
}