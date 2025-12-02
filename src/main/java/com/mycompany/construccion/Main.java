package com.mycompany.construccion;

import reports.controllerReport.ReportController;
import reports.modelReport.JSONControllerPersistence;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportSubject;

/**
 *
 * @author villa
 */
public class Main {
    public static void main(String[] args) {
        JSONControllerPersistence persistence = new JSONControllerPersistence();
        FrmMain view = new FrmMain();

        ReportSubject subject = new ReportSubject();
        ReportGenerator generator = new ReportGenerator(subject, persistence);

        ReportController controller = new ReportController();
        controller.setViewModule(view, generator);
        /*
         * FilterSubject subject2 = new FilterSubject();
         * FilterModel model = new FilterModel(subject2, persistence);
         * 
         * FilterController controller2 = new FilterController();
         * controller2.setViewModule(view, model);
         */
        view.setVisible(true);
    }
}
