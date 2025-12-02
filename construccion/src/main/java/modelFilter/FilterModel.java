/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelFilter;

import java.util.List;
import java.util.stream.Collectors;
import modelReport.JSONControllerPersistence;
import modelReport.Movement;

/**
 *
 * @author villa
 */
public class FilterModel {

    private final FilterSubject subject;
    private final JSONControllerPersistence persistence;

    public FilterModel(FilterSubject subject, JSONControllerPersistence persistence) {
        this.subject = subject;
        this.persistence = persistence;
    }

    public void addObserver(FilterObserver obs) {
        subject.add(obs);
    }

    public void filterByCategory(String categoryName) {
        List<Movement> movements = persistence.loadAllMovements().stream()
            .filter(m -> m.getCategoryName().equalsIgnoreCase(categoryName))
            .collect(Collectors.toList());

        subject.notifyObservers(movements);
    }

    
}
