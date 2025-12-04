package movements.movement_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa el patrón Observer para la gestión de categorías de
 * movimientos.
 * Permite registrar, eliminar y notificar observadores cuando ocurre un cambio
 * relacionado con la lista de categorías de movimiento.
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