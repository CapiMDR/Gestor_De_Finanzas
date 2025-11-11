package com.example.movement.movement_model;

public interface MovementObservable {
    void addObserver(MovementObserver observer);
    void removeObserver(MovementObserver observer);
    void notifyObservers();
}
