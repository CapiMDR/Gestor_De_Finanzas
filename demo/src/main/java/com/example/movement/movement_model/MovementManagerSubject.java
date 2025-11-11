package com.example.movement.movement_model;

import java.util.ArrayList;
import java.util.List;

public class MovementManagerSubject implements MovementObservable{
    private List<MovementObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(MovementObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MovementObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (MovementObserver observer : observers) {
            observer.update();
        }
    }

}
