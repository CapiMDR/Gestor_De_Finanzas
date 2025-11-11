package com.example.movement.movement_model;

import java.util.ArrayList;
import java.util.List;

public class MovementManager {
    private List<Movement> movements;
    private List<MovementCategory> categories;
    private MovementManagerSubject subject;

    public MovementManager(MovementManagerSubject subject){
    this.subject = subject;
    this.movements = new ArrayList<>();
    this.categories = new ArrayList<>();
    }


    public void addMovement(Movement movement){
        movements.add(movement);
        subject.notifyObservers();
    }

    public void removeMovement(Movement movement){
        movements.remove(movement);
        subject.notifyObservers();
    }

    public void addCategory(MovementCategory category){
        categories.add(category);
        subject.notifyObservers();
    }

    public void removeCategory(MovementCategory category){
        categories.remove(category);
        subject.notifyObservers();
    }

    public List<Movement> getMovements() {
        return movements;
    }

    public List<MovementCategory> getCategories() {
        return categories;
    }

    public MovementManagerSubject getSubject() {
        return subject;
    }
    
    
}
