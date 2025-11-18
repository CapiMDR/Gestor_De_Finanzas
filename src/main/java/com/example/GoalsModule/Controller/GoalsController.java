package com.example.GoalsModule.Controller;

import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalsView;
import com.example.GoalsModule.View.GoalEditView;
// Assumes that these exist in AccountsModule.Model
import com.example.AccountsModule.Model.Account;
import com.example.AccountsModule.Model.AccountManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane; 

/**
 * Coordinates the interaction between the Goals View and the Data Model.
 * Responsible for the lifecycle of Goal objects (Create, Update, Delete) and 
 * delegating persistence to the global AccountManager.
 * @author Jose Pablo Martinez
 * @version 1.0
 */

public class GoalsController implements ActionListener {

    private final GoalsView view;
    private final GoalEditView editView;
    private Account currentAccount;
    private final AccountManager accountManager;


    public GoalsController(GoalsView view, GoalEditView editView, AccountManager accountManager) {
        this.view = view;
        this.editView = editView;
        this.accountManager = accountManager;
    }


    public void setAccount(Account account) {
        this.currentAccount = account;
        if (currentAccount != null) {
            // Assuming Account has getGoals()
            view.updateGoalList(currentAccount.getGoals());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.getBtnAddGoal()) {
            handleAddGoal();
        } else if (source == view.getBtnDeleteGoal()) {
            deleteSelectedGoal();
        } else if (source == view.getBtnEditGoal()) {
            // Logic to open the edit dialog for the selected item
            Goal selected = view.getSelectedGoal();
            if (selected != null) {
                openEditDialog(selected);
            }
        }
    }

    private void handleAddGoal() {
        try {
            // Extract raw data directly from the View
            String name = view.getGoalName();
            BigDecimal target = view.getTargetAmount(); 
            String desc = view.getDescription();

            createNewGoal(name, target, desc);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid number format: " + ex.getMessage());
            // In a real application, display error to user via JOptionPane
        }
    }

  
    public void createNewGoal(String name, BigDecimal target, String desc) {
        Goal newGoal = new Goal(name, target, desc);
        
        // Manipulate the list
        addGoalToAccount(newGoal);
        
        // Delegate persistence to the external manager
        accountManager.saveAll();
        
        // Update the main view list
        view.updateGoalList(currentAccount.getGoals());
    }

    public void deleteSelectedGoal() {
        Goal selected = view.getSelectedGoal();
        if (selected != null) {
            removeGoalFromAccount(selected); 
            accountManager.saveAll();
            view.updateGoalList(currentAccount.getGoals());
        }
    }


    public void saveGoalData(Goal originalGoal, String name, BigDecimal target, String desc) {
        if (originalGoal != null) {
            originalGoal.setName(name);
            originalGoal.setTargetAmount(target);
            originalGoal.setDescription(desc);

            accountManager.saveAll();
            view.updateGoalList(currentAccount.getGoals());
        }
    }

    /**
     * Prepares and displays the editing popup.
     * Connects the Save button of the popup to the saveGoalData logic.
     */
    
    private void openEditDialog(Goal goal) {
        // Push data to the view (populate fields)
        editView.populateFields(goal.getName(), goal.getTargetAmount(), goal.getDescription());
        
        // Using a lambda to capture the specific 'goal' instance being edited
        editView.addSaveListener(e -> { 
            try {
                String newName = editView.getEditedName();
                BigDecimal newTarget = editView.getEditedTarget(); 
                String newDesc = editView.getEditedDescription();

                saveGoalData(goal, newName, newTarget, newDesc);
                editView.closeDialog(); 
            } catch (NumberFormatException ex) {
                System.err.println("Error updating goal: Invalid number format.");
            }
        });

        editView.showDialog();
    } 

    // The Account class is passive; this controller manages the list logic.

    private void addGoalToAccount(Goal goal) {
        if (currentAccount != null) {
            currentAccount.getGoals().add(goal);
        }
    }
 
    private void removeGoalFromAccount(Goal goal) { 
        if (currentAccount != null) {
            currentAccount.getGoals().remove(goal);
        }
    }
}