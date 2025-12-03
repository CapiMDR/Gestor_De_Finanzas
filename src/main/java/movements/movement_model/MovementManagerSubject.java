package movements.movement_model;

import java.util.ArrayList;
import java.util.List;

public class MovementManagerSubject {
    private List<CategoryObserver> observers = new ArrayList<>();

    public void addObserver(CategoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CategoryObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(List<MovementCategory> categories) {
        for (CategoryObserver observer : observers) {
            observer.onNotify(categories);
        }
    }

}