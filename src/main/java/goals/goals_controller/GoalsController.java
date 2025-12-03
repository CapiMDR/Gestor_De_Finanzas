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
 * Controlador principal del Módulo de Metas.
 * Coordina la interacción entre Vistas, Modelos y Persistencia.
 * Implementa GoalActionListener para manejar los eventos de las tarjetas de metas.
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
     * Establece la cuenta activa y refresca la vista.
     * 
     * @param account La cuenta seleccionada.
     */

    public void setAccount(Account account) {
        this.currentAccount = account;
        if (currentAccount != null) {
            String currency = "USD";
            if (currentAccount.getCoin() != null) {
                currency = currentAccount.getCoin().toString();
            }
            mainView.setCurrencyLabel(currency);

            // Auto-rellenar el nombre de la cuenta
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
     * Maneja actualizaciones activadas por módulos externos (como Movimientos).
     */

    @Override
    public void onNotify(List<Account> accountsList) {
        if (currentAccount == null)
            return;

        List<Movement> movements = currentAccount.getMovements();
        recalculateGoalsProgress(currentAccount.getGoals(), movements);

        // Guardando cuentas nuevamente después de recalcular las metas
        AccountManager.saveAccountsData();
        refreshView();
        System.out.println("CONTROLLER: Goals refreshed based.");
    }

    /**
     * Calcula el saldo total (Inicial + Movimientos).
     * Devuelve el valor BigDecimal directamente.
     */

    private BigDecimal calculateActualBalance() {
        if (currentAccount == null) return BigDecimal.ZERO;

        // Obtener el saldo inicial
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
                            System.out.println("Entrada no valida, tipo no encontrado");
                         break;
                    }
                }
            }
        }
        return balance;
    }

    /**
     * Actualiza el progreso de las metas basándose en el saldo inicial
     * de la cuenta y los movimientos.
     */

    private void recalculateGoalsProgress(List<Goal> goals, List<Movement> movements) {
        //Calculamos el balance actual de la cuenta
        BigDecimal totalBalance = calculateActualBalance();

        //Asignamos el balance a las metas
        if (goals != null) {
        for (Goal goal : goals) {
            goal.setCurrentAmount(totalBalance);
        }
    }
    }

    public void createNewGoal(String name, BigDecimal target, String desc) {
        Goal newGoal = new Goal(name, target, desc);


        if (currentAccount != null) {
            //Calculamos el balance actual y se lo ponemos a la meta recien creada
            BigDecimal currentBalance =calculateActualBalance();
            newGoal.setCurrentAmount(currentBalance);

            currentAccount.getGoals().add(newGoal);
            AccountManager.saveAccountsData();
            refreshView();
        }
    }

     /**
     * Lógica para los botones en la vista.
     */

    private void handleAddGoalFromMainView() {
        String name = mainView.getGoalName();
        BigDecimal target = mainView.getTargetAmount();
        String desc = mainView.getDescription();

        // Validate
        if (name.isEmpty() || target.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(mainView, "Please enter a valid name and target amount.");
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
                JOptionPane.showMessageDialog(editView, "Invalid Data.");
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