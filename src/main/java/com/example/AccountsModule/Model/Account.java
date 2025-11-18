package com.example.AccountsModule.Model;

import com.example.GoalsModule.Model.Goal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's financial account within the system.
 * 
 * This class acts as a STUB (Dummy implementation) representing 
 * the external {@code AccountsModule}. It is currently provided to facilitate 
 * the compilation and testing of the {@code GoalsModule} without requiring 
 * the full integration of the Accounts subsystem.
 * @author Jose Pablo Martinez
 */

public class Account {

    private int idAccount;
    private String name;

    private List<Goal> goals;

    public Account() {

        this.goals = new ArrayList<>();
    }

    public List<Goal> getGoals() {
        return this.goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }
}