/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filters.modelFilter;

/**
 *
 * @author villa
 */
import java.util.ArrayList;
import java.util.List;

import reports.modelReport.Movement;

public class FilterSubject {

    private List<FilterObserver> observers = new ArrayList<>();

    public void add(FilterObserver obs) {
        observers.add(obs);
    }

    public void notifyObservers(List<Movement> movements) {
        for (FilterObserver obs : observers) {
            obs.update(movements);
        }
    }

}
