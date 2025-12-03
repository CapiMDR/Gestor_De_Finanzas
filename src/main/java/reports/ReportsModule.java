package reports;

import com.mycompany.construccion.FrmMain;

import accounts.account_model.Account;
import reports.controllerReport.ReportController;

import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportSubject;

public class ReportsModule {

    public static void initReportsModule(Account selectedAccount) {
        FrmMain reportsView = new FrmMain(selectedAccount);
        ReportSubject subject = new ReportSubject();
        
        ReportGenerator generator = new ReportGenerator(subject, selectedAccount);

        ReportController controller = new ReportController();
        controller.setViewModule(reportsView, generator, selectedAccount);
        reportsView.setVisible(true);
    }
}