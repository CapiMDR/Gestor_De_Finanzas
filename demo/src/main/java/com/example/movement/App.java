package com.example.movement;

import java.math.BigDecimal;

import com.example.account.account_model.Account;
import com.example.movement.movement_controller.MovementController;
import com.example.movement.movement_model.Movement.MovementType;
import com.example.movement.movement_model.MovementManager;
import com.example.movement.movement_model.MovementManagerSubject;
import com.example.movement.movement_view.MovementManagerView;

public class App {
    public static void main( String[] args )
    {
        MovementManagerSubject movementSubject = new MovementManagerSubject();
        MovementManager movementModel = new MovementManager(movementSubject);
        MovementManagerView movementView = new MovementManagerView();
        MovementController movementController = new MovementController(movementModel, movementView);

        movementSubject.addObserver(movementView);

        String name = "Cuenta principal";
        String typeString = "CASH";
        String coinString = "USD";
        int balanceInt = 1000;

        Account.AccountType type = Account.AccountType.valueOf(typeString.toUpperCase());
        Account.Coin coin = Account.Coin.valueOf(coinString.toUpperCase());
        BigDecimal balance = BigDecimal.valueOf(balanceInt);

        Account cuenta = new Account(1, name, type, coin, balance);

        movementController.addCategory("Sueldo", MovementType.INCOME);
        movementController.addCategory("Comida", MovementType.EXPENSE);

        movementController.addIncome("Pago de quincena", new BigDecimal("2500"), MovementType.INCOME, cuenta);
        movementController.addExpense("Comida rápida", new BigDecimal("150"), MovementType.EXPENSE, cuenta);

        movementController.showCategories();
        movementController.showMovements();

    }
}