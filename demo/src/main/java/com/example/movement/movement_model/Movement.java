package com.example.movement.movement_model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.account.account_model.Account;

public class Movement {

    private int idMovement;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private MovementCategory category;
    Account account;

    public Movement(int idMovement, String description, BigDecimal amount, MovementCategory category, Account account) {
        this.idMovement = idMovement;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = LocalDateTime.now();
        this.account = account;
    }

    //construct for load a JSON
    public Movement(int idMovement, String description, BigDecimal amount, MovementCategory category, Account account, LocalDateTime date) {
        this.idMovement = idMovement;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
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

    public MovementCategory getCategory() {
        return category;
    }

    public Account getAccount() {
        return account;
    }

    
    public int getIdMovement() {
        return idMovement;
    }

    public void setIdMovement(int idMovement){
        this.idMovement = idMovement;
    }

    @Override
    public String toString() {
        return "Movement [getDescription()=" + getDescription() + ", getAmount()=" + getAmount() + ", getDate()="
                + getDate() + ", getCategory()=" + getCategory() + ", getAccount()=" + getAccount()
                + ", getIdMovement()=" + getIdMovement() + "]";
    }
    
   
}
