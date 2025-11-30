package com.example.GoalsModule.Controller;

import com.example.AccountsModule.Model.Account;
import com.example.AccountsModule.Model.AccountManager;
import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalEditView;
import com.example.GoalsModule.View.GoalActionListener;
import com.example.GoalsModule.View.GoalsView;
import com.example.MovementsModule.Model.Movement;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Main Controller for the Goals Module.
 * Manages interaction between the split-screen Main View, Models, and Popups.
 *
 * @author Jose Pablo Martinez
 */

public class GoalsController implements GoalActionListener {

    private final GoalsView mainView;
    private final GoalEditView editView;      
    private final GoalDetailController detailController;
    
    private Account currentAccount;
    private final AccountManager accountManager;

    public GoalsController(GoalsView mainView, 
                           GoalEditView editView, 
                           GoalDetailController detailController, 
                           AccountManager accountManager) {
        this.mainView = mainView;
        this.editView = editView;
        this.detailController = detailController;
        this.accountManager = accountManager;

        //Add Button
        this.mainView.getBtnAddGoal().addActionListener(e -> handleAddGoalFromMainView());
        //Card Actions
        this.mainView.setCardActionListener(this);
        this.mainView.setController(this);
    }

    public void setAccount(Account account) {
        this.currentAccount = account;
        if (currentAccount != null) {
            // Set Currency Label based on Account
            String currency = "USD"; 
            if (currentAccount.getCoin() != null) {
                currency = currentAccount.getCoin().toString(); 
            }
            mainView.setCurrencyLabel(currency);
            
            refreshView();
        }
    }

    private void refreshView() {
        if (currentAccount != null) {
            mainView.updateGoalList(currentAccount.getGoals());
        }
    }

    //Goal management functionalities

    private void handleAddGoalFromMainView() {
        //Extract Data 
        String name = mainView.getGoalName();
        BigDecimal target = mainView.getTargetAmount();
        String desc = mainView.getDescription();
        if (name.isEmpty() || target.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(mainView, "Invalid data. Please check name and amount.");
            return;
        }
        createNewGoal(name, target, desc); //Tested with JUnit
        mainView.clearForm(); 
    }

    //Public for Testing in JUnit
    public void createNewGoal(String name, BigDecimal target, String desc) {
        Goal newGoal = new Goal(name, target, desc);
        
        if (currentAccount != null) {
            currentAccount.getGoals().add(newGoal);
            accountManager.saveAll(); // Persist
            refreshView();            // Update UI
        }
    }

    @Override
    public void onViewDetails(Goal goal) {
        detailController.showDetails(goal);
    }

    @Override
    public void onEdit(Goal goal) {
        editView.populateFields(goal.getName(), goal.getTargetAmount(), goal.getDescription());
        
        editView.addSaveListener(e -> {
            String newName = editView.getNameInput();
            BigDecimal newTarget = editView.getTargetInput();
            
            if (newName.isEmpty() || newTarget.compareTo(BigDecimal.ZERO) <= 0) {
                 JOptionPane.showMessageDialog(editView, "Invalid Data.");
                 return;
            }
            
            // Business Logic for Update
            goal.setName(newName);
            goal.setTargetAmount(newTarget);
            goal.setDescription(editView.getDescriptionInput());
            
            accountManager.saveAll();
            refreshView();
            editView.closeDialog();
        });
        editView.showDialog();
    }

    @Override
    public void onDelete(Goal goal) {
        int confirm = JOptionPane.showConfirmDialog(mainView, 
                "Delete goal: " + goal.getName() + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION && currentAccount != null) {
            currentAccount.getGoals().remove(goal);
            accountManager.saveAll();
            refreshView();
        }
    }
    
    public void handleExternalUpdates(List<Movement> movements) {
        refreshView();
    }
}