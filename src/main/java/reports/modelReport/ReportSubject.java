/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports.modelReport;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author villa
 */
public class ReportSubject {

    private List<ReportObserver> observers = new ArrayList<>();

    public void add(ReportObserver obs) {
        observers.add(obs);
    }

    public void remove(ReportObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers(ReportData data) {
        for (ReportObserver obs : observers) {
            obs.onNotify(data);
        }
    }
}
