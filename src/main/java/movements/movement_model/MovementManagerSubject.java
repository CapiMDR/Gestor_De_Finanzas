package movements.movement_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the Observer pattern for the management of movement
 * categories.
 * Allows to register, remove and notify observers when a change occurs
 * related to the movement category list.
 * 
 * @author Martín Jesús Pool Chuc
 */
public class MovementManagerSubject {
    private static List<CategoryObserver> observers = new ArrayList<>();

    public static void addObserver(CategoryObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(CategoryObserver observer) {
        observers.remove(observer);
    }

    public static void notifyObservers(List<MovementCategory> categories) {
        for (CategoryObserver observer : observers) {
            observer.onNotify(categories);
        }
    }

}