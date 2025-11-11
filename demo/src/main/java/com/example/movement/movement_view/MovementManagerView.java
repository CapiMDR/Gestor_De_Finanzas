package com.example.movement.movement_view;

import java.util.List;

import com.example.movement.movement_controller.MovementController;
import com.example.movement.movement_model.MovementCategory;
import com.example.movement.movement_model.Movement;
import com.example.movement.movement_model.MovementObserver;

public class MovementManagerView implements MovementObserver{
    private MovementController controller;

    @Override
    public void update() {
        System.out.println("all movements manager components updated");
    }

    public void showMovements(List<Movement> movements){
        System.out.println(movements);
    }

    public void showCategories(List<MovementCategory> categories){
        System.out.println(categories);
    }
    
}
