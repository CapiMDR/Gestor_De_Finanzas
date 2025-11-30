package com.example.AccountsModule.Model;

import com.example.GoalsModule.Model.Goal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's financial account within the system.
 * (Stub updated with Coin support)
 */

public class Account {

    private int idAccount;
    private String name;
    
    private Coin coin; 
    
    private List<Goal> goals;
    //private List<Movement> movements; 

    public Account() {
        this.goals = new ArrayList<>();
        //this.movements = new ArrayList<>();
        
        this.coin = Coin.MXN;
    }
    
    //Getters and setters
    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }
    
    public List<Goal> getGoals() {
        return this.goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public String getName() {
        return this.name; 
    }
    public void setName(String name) {
        this.name = name;
    }
}