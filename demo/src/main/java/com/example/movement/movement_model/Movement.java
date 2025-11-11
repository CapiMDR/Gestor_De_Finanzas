package com.example.movement.movement_model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.account.account_model.Account;



public class Movement {

    public enum MovementType{
    INCOME, EXPENSE
}
    private int idMovement;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private MovementType type;
    Account account;

    public Movement(int idMovement, String description, BigDecimal amount, MovementType type, Account account) {
        this.idMovement = idMovement;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = LocalDateTime.now();
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public MovementType getType() {
        return type;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "Movement [description=" + description + ", amount=" + amount + ", date=" + date + ", type=" + type
                + ", account=" + account + "]";
    }

    public int getIdMovement() {
        return idMovement;
    }

   
}
