package GoalsModule.Controller;

import GoalsModule.Model.Goal;
import GoalsModule.View.GoalsView;
import GoalsModule.View.GoalEditView;
//Asumes that exists in AccountsModule.Model
import FinancialManager.AccountsModule.Model.Account;
import FinancialManager.AccountsModule.Model.AccountManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
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

    /**
     * Initializes the controller with necessary views and the global manager.
     * @param view           Main interface for the goals list.
     * @param editView       Popup interface for editing goals.
     * @param accountManager External manager handling JSON persistence.
     */

    public GoalsController(GoalsView view, GoalEditView editView, AccountManager accountManager) {
        this.view = view;
        this.editView = editView;
        this.accountManager = accountManager;
    }

    /**
     * Sets the context for the controller. 
     * Populates the view with the goals belonging to the provided account.
     */
    public void setAccount(Account account) {
        this.currentAccount = account;
        if (currentAccount != null) {
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
            // should be separated if it grows complex.
            System.err.println("Invalid number format: " + ex.getMessage());
        }
    }

    /**
     * Core business logic to instantiate and store a new goal.
     * @param name   Goal identifier.
     * @param target Monetary target.
     * @param desc   Additional notes.
     */

    public void createNewGoal(String name, BigDecimal target, String desc) {
        Goal newGoal = new Goal(name, target, desc);
        
        addGoalToAccount(newGoal);
        
        // Delegate persistence to the external manager (Centralized Persistence)
        accountManager.saveAll();
        
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

    /**
     * Called when the user confirms changes in the Edit View.
     * @param originalGoal The object instance being modified.
     * @param name         New name.
     * @param target       New target amount.
     * @param desc         New description.
     */

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

    // --- Internal List Management ---
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