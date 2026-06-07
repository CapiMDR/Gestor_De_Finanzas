package reports.modelReport;

import java.util.List;
import java.util.ArrayList;

/**
 * Subject that maintains a list of report observers and notifies them of new reports.
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
