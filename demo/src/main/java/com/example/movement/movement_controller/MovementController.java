package com.example.movement.movement_controller;

import java.math.BigDecimal;
import com.example.account.account_model.Account;
import com.example.movement.movement_model.Movement;
import com.example.movement.movement_model.MovementCategory;
import com.example.movement.movement_model.Movement.MovementType;
import com.example.movement.movement_model.MovementManager;
import com.example.movement.movement_view.MovementManagerView;

public class MovementController {
    private MovementManager model;
    private MovementManagerView view;

    public MovementController(MovementManager model, MovementManagerView view){
        this.model = model;
        this.view = view;
    }

    public void addIncome(String description, BigDecimal amount, MovementType type, Account account){
        Movement movement  = new Movement(model.getMovements().size(), description, amount, type, account);
        model.addMovement(movement);
    }

    public void addExpense(String description, BigDecimal amount, MovementType type, Account account){
        Movement movement  = new Movement(model.getMovements().size(), description, amount, type, account);
        model.addMovement(movement);
    }

    public void addCategory(String name, MovementType type){
        MovementCategory movementCategory = new MovementCategory(name, type);
        model.addCategory(movementCategory);
    }

    public void removeCategory(String name, MovementType type){
        MovementCategory movementCategory = new MovementCategory(name, type);
        model.removeCategory(movementCategory);
    }

    public void showMovements(){
        view.showMovements(model.getMovements());
    }

    public void showCategories(){
        view.showCategories(model.getCategories());
    }
}
