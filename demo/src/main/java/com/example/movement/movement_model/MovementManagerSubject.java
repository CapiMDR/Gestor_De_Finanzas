package com.example.movement.movement_model;

import java.util.ArrayList;
import java.util.List;

public class MovementManagerSubject{
    private List<MovementObserver> observers = new ArrayList<>();

    public void addObserver(MovementObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MovementObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(List<MovementCategory> categories) {
        for (MovementObserver observer : observers) {
            observer.onNotify(categories);
        }
    }

}
