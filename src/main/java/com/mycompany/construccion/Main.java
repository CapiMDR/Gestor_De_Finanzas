package com.mycompany.construccion;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import controllerFilter.FilterController;
import controllerReport.ReportController;
import modelFilter.FilterModel;
import modelFilter.FilterSubject;
import modelReport.JSONControllerPersistence;
import modelReport.ReportGenerator;
import modelReport.ReportSubject;
import com.mycompany.construccion.FrmMain;

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
        FilterSubject subject2 = new FilterSubject();
        FilterModel model = new FilterModel(subject2, persistence);
        
        FilterController controller2 = new FilterController();
        controller2.setViewModule(view, model);
        */
        
        

        view.setVisible(true);
    }
}
