package goals.goals_controller;

import movements.movement_model.Movement;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import goals.goals_model.Goal;
import goals.goals_view.GoalActionListener;
import goals.goals_view.GoalEditView;
import goals.goals_view.GoalsView;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Main controller of the Goals Module.
 * Coordinates the interaction between Views, Models and Persistence.
 * Implements GoalActionListener to handle the events of the goal cards.
 * 
 * @author Jose Pablo
 */

public class GoalsController implements GoalActionListener, AccountObserver {

    private final GoalsView mainView;
    private final GoalEditView editView;
    private final GoalDetailController detailController;

    private Account currentAccount;

    public GoalsController(GoalsView mainView,
            GoalEditView editView,
            GoalDetailController detailController) {
        this.mainView = mainView;
        this.editView = editView;
        this.detailController = detailController;

        this.mainView.getBtnAddGoal().addActionListener(e -> handleAddGoalFromMainView());
        this.mainView.setCardActionListener(this);
        this.mainView.setController(this);
        AccountManagerSubject.addObserver(this);
    }

    /**
     * Sets the active account and refreshes the view.
     * 
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

            // Auto-fill account name
            String accName = (currentAccount.getName() != null) ? currentAccount.getName() : "Current Account";
            mainView.setAccountName(accName);

            onNotify(AccountManager.getAccounts());
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

    @Override
    public void onNotify(List<Account> accountsList) {
        if (currentAccount == null)
            return;

        List<Movement> movements = currentAccount.getMovements();
        recalculateGoalsProgress(currentAccount.getGoals(), movements);

        // Saving accounts again after recalculating goals
        AccountManager.saveAccountsData();
        refreshView();
    }

    /**
     * Calculates the total balance (Initial + Movements).
     * Returns the BigDecimal value directly.
     */

    private BigDecimal calculateActualBalance() {
        if (currentAccount == null)
            return BigDecimal.ZERO;

        // Get initial balance
        BigDecimal balance = currentAccount.getInitialBalance();
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }

        // Sumar y restar movimientos
        List<Movement> movements = currentAccount.getMovements();
        if (movements != null) {
            for (Movement m : movements) {
                if (m.getCategory() != null) {
                    switch (m.getCategory().getType()) {
                        case INCOME:
                            balance = balance.add(m.getAmount());
                            break;
                        case EXPENSE:
                            balance = balance.subtract(m.getAmount());
                            break;
                        default:
                            // Entrada no valida, tipo no encontrado
                            break;
                    }
                }
            }
        }
        return balance;
    }

    /**
     * Updates the progress of the goals based on the account's initial
     * balance and movements.
     */

    private void recalculateGoalsProgress(List<Goal> goals, List<Movement> movements) {
        // We calculate the current account balance
        BigDecimal totalBalance = calculateActualBalance();

        // We assign the balance to the goals (without exceeding the target amount)
        // Fix: Previously the progress was assigned to all goals equally,
        // now the progress is distributed according to the target amount of each goal
        if (goals != null) {
            for (Goal goal : goals) {
                BigDecimal progress = totalBalance.min(goal.getTargetAmount());
                goal.setCurrentAmount(progress.max(BigDecimal.ZERO));
            }
        }
    }

    public void createNewGoal(String name, BigDecimal target, String desc) {
        Goal newGoal = new Goal(name, target, desc);

        if (currentAccount != null) {
            // We calculate the current balance and set it to the newly created goal
            BigDecimal currentBalance = calculateActualBalance();
            newGoal.setCurrentAmount(currentBalance);

            currentAccount.getGoals().add(newGoal);
            AccountManager.saveAccountsData();
            refreshView();
        }
    }

    /**
     * Logic for the buttons in the view.
     */

    private void handleAddGoalFromMainView() {
        String name = mainView.getGoalName();
        BigDecimal target = mainView.getTargetAmount();
        String desc = mainView.getDescription();

        // Validate
        if (name.isEmpty() || target.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(mainView, "Por favor ingresa un nombre y monto objetivo válidos.");
            return;
        }

        createNewGoal(name, target, desc); // Testing in JUnit
        mainView.clearForm();
    }

    @Override
    public void onViewDetails(Goal goal) {
        // Delegate visualization to the Detail Controller
        detailController.showDetails(goal);
    }

    @Override
    public void onEdit(Goal goal) {
        editView.populateFields(goal.getName(), goal.getTargetAmount(), goal.getDescription());

        editView.addSaveListener(e -> {
            String newName = editView.getNameInput();
            BigDecimal newTarget = editView.getTargetInput();

            if (newName.isEmpty() || newTarget.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(editView, "Datos inválidos.");
                return;
            }

            goal.setName(newName);
            goal.setTargetAmount(newTarget);
            goal.setDescription(editView.getDescriptionInput());

            AccountManager.saveAccountsData();
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
            AccountManager.saveAccountsData();
            refreshView();
        }
    }
}