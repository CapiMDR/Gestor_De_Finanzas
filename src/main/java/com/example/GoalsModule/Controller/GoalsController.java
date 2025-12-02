package com.example.GoalsModule.Controller;

import com.example.AccountsModule.Model.Account;
import com.example.AccountsModule.Model.AccountManager;
import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalEditView;
import com.example.GoalsModule.View.GoalActionListener;
import com.example.GoalsModule.View.GoalsView;
import com.example.MovementsModule.Model.Movement;
import com.example.MovementsModule.Model.MovementCategory.MovementType;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Main Controller for the Goals Module.
 * Coordinates interaction between Views, Models, and Persistence.
 * Implements GoalActionListener to handle events from Goal Cards.
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

        this.mainView.getBtnAddGoal().addActionListener(e -> handleAddGoalFromMainView());
        this.mainView.setCardActionListener(this);
        this.mainView.setController(this);
    }

    /**
     * Sets the active account context and refreshes the view.
     * @param account The selected account.
     */

    public void setAccount(Account account) {
        this.currentAccount = account;
        if (currentAccount != null) {
            String currency = "USD"; 
            if (currentAccount.getCoin() != null) {
                currency = currentAccount.getCoin().toString(); 
            }
            mainView.setCurrencyLabel(currency);
            
            // Auto-fill account name in the form
            String accName = (currentAccount.getName() != null) ? currentAccount.getName() : "Current Account";
            mainView.setAccountName(accName);
            
            refreshView();
        }
    }

    private void refreshView() {
        if (currentAccount != null) {
            mainView.updateGoalList(currentAccount.getGoals());
        }
    }


    /**
     * Handles updates triggered by external modules (like Movements).
     */

    public void handleExternalUpdates() {
        if (currentAccount != null) {
            List<Movement> movements = currentAccount.getMovements(); 
            recalculateGoalsProgress(currentAccount.getGoals(), movements);
            accountManager.saveAll();
            refreshView();
            System.out.println("CONTROLLER: Goals refreshed based on external movements.");
        }
    }

    private void recalculateGoalsProgress(List<Goal> goals, List<Movement> movements) {
        for (Goal goal : goals) {
        BigDecimal totalSaved = BigDecimal.ZERO;
        for (Movement m : movements) {
            if (m.getCategory().getType() == MovementType.EXPENSE && m.getDescription().equals(goal.getName())) {
            totalSaved = totalSaved.add(m.getAmount());
            }
        }
        goal.setCurrentAmount(totalSaved);
        }
    }


    private void handleAddGoalFromMainView() {
        String name = mainView.getGoalName();
        BigDecimal target = mainView.getTargetAmount();
        String desc = mainView.getDescription();

        //Validate
        if (name.isEmpty() || target.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(mainView, "Please enter a valid name and target amount.");
            return;
        }

        createNewGoal(name, target, desc); //Testing in JUnit
        mainView.clearForm(); 
    }

    /**
     * Creates a new goal and saves it to the account.
     */

    public void createNewGoal(String name, BigDecimal target, String desc) {
        Goal newGoal = new Goal(name, target, desc);
        
        if (currentAccount != null) {
            currentAccount.getGoals().add(newGoal);
            accountManager.saveAll(); // Persist to JSON
            refreshView();            // Update UI
        }
    }


    @Override
    public void onViewDetails(Goal goal) {
        //Delegate visualization to the Detail Controller
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
                "Are you sure you want to delete: " + goal.getName() + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION && currentAccount != null) {
            currentAccount.getGoals().remove(goal);
            accountManager.saveAll();
            refreshView();
        }
    }
}