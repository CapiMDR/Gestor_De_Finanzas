package com.example.account.account_model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.movement.movement_model.Movement;

public class Account {

    public enum AccountType {
        CASH,
        DIGITAL
    }

    public enum Coin {
        USD,
        MXN
    }

    private int id;
    private String name;
    private AccountType type;
    private Coin coin;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private List<Movement> movements;

    public Account(int id, String name, AccountType type, Coin coin, BigDecimal initialBalance) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coin = coin;
        this.initialBalance = initialBalance;
        this.currentBalance = initialBalance;
        this.movements = new ArrayList<>();
    }

    public void updateBalance(BigDecimal currentBalance){
        this.currentBalance = currentBalance;
    }

    public void addMovement(Movement movement){
        this.movements.add(movement);
        
        switch (movement.getCategory().getType()) {
            case INCOME:
                this.currentBalance = this.currentBalance.add(movement.getAmount());
                break;
            
            case EXPENSE:
                this.currentBalance = this.currentBalance.subtract(movement.getAmount());
                break;
            default:
                break;
        }
    }

    //getters, setters  and toString method
    public List<Movement> getMovements() {
        return movements;
    }
    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
    
        public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }
    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }


    @Override
    public String toString() {
        return "Account [getInitialBalance()=" + getInitialBalance() + ", getCurrentBalance()=" + getCurrentBalance()
                + ", getId()=" + getId() + ", getName()=" + getName() + ", getType()=" + getType() + ", getCoin()="
                + getCoin() + "]";
    }

    

    
    
        
}