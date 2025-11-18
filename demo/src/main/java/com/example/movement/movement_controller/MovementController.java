package com.example.movement.movement_controller;

import java.math.BigDecimal;
import java.util.List;
import com.example.account.account_model.Account;
import com.example.account.account_model.AccountManager;
import com.example.movement.movement_model.Movement;
import com.example.movement.movement_model.MovementCategory;
import com.example.movement.movement_model.MovementCategory.MovementType;
import com.example.movement.movement_model.MovementManager;
import com.example.movement.movement_model.MovementObserver;
import com.example.movement.movement_view.MovementManagerView;

public class MovementController implements MovementObserver{
    private MovementManager model;
    private MovementManagerView view;
    private AccountManager accountManager;

    public MovementController(MovementManager model, AccountManager accountManager){
        this.model = model;
        this.view = view;
        this.accountManager = accountManager;
        model.addOserver(this);
    }

    public void addMovement(String description, BigDecimal amount, MovementCategory category, Account account){
        Movement movement  = new Movement(model.getMovements().size(), description, amount, category, account);
        account.addMovement(movement);
        model.addMovement(movement);
        accountManager.saveAccountsData();
        accountManager.getSubject().notifyObservers(accountManager.getAccounts());
    }

    public void addCategory(String name, MovementType type){
        MovementCategory movementCategory = new MovementCategory(name, type);
        model.addCategory(movementCategory);
    }

    public void removeCategory(String name, MovementType type){
        MovementCategory movementCategory = new MovementCategory(name, type);
        model.removeCategory(movementCategory);
    }

    @Override
    public void onNotify(List<MovementCategory> categories) {
        //todo lo que se actualizará de la vista
    }
}
