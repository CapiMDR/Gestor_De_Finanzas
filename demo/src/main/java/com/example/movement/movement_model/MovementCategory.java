package com.example.movement.movement_model;

import com.example.movement.movement_model.Movement.MovementType;

public class MovementCategory {
    private String name;
    private MovementType type;

    public MovementCategory(String name, MovementType type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public MovementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MovementCategory [name=" + name + ", type=" + type + "]";
    }

    
    
}
