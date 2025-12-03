package com.mycompany.construccion;

import accounts.account_controller.AccountController;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountView;
import reports.controllerReport.ReportController;
import reports.modelReport.JSONControllerPersistence;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportSubject;

/**
 *
 * @author villa
 */

// MAIN PRINCIPAL, LLAMAR ESTO ANTES QUE TODOOOOOO
public class Main {
    public static void main(String[] args) {
        AccountManager.initAccountManager();
        JSONControllerPersistence persistence = new JSONControllerPersistence();

        AccountView accountsView = new AccountView();
        AccountController accountController = new AccountController(accountsView);
        AccountManager.loadInitialData();

        FrmMain reportsView = new FrmMain();

        ReportSubject subject = new ReportSubject();
        ReportGenerator generator = new ReportGenerator(subject, persistence);

        ReportController controller = new ReportController();
        controller.setViewModule(reportsView, generator);
        reportsView.setVisible(true);
    }
}
