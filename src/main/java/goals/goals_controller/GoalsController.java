package goals.goals_controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import goals.goals_model.Goal;
import goals.goals_view.GoalsViewFX;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import movements.movement_model.Movement;

/**
 * Main controller of the Goals Module for JavaFX.
 * Coordinates the interaction between Views, Models and Persistence.
 * 
 * @author Jose Pablo
 */
public class GoalsController implements AccountObserver {

    private static final Logger logger = LoggerFactory.getLogger(GoalsController.class);

    private final GoalsViewFX mainView;
    private final GoalEditController editController;
    private final GoalDetailControllerFX detailController;

    private Account currentAccount;

    public GoalsController(GoalsViewFX mainView,
            GoalEditController editController,
            GoalDetailControllerFX detailController) {
        this.mainView = mainView;
        this.editController = editController;
        this.detailController = detailController;

        assignEvents();
        AccountManagerSubject.addObserver(this);
    }

    private void assignEvents() {
        // Setup ListView to display only the goal names
        mainView.getListGoals().setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        // Add Goal
        mainView.getBtnAddGoal().setOnAction(e -> handleAddGoal());

        // Edit Goal
        mainView.getBtnEditGoal().setOnAction(e -> {
            Goal selected = getSelectedGoal();
            if (selected != null) {
                boolean saved = editController.showEditDialog(selected);
                if (saved) {
                    AccountManager.saveAccountsData();
                    refreshView();
                }
            } else {
                showAlert(AlertType.WARNING, "Selección requerida", "Por favor selecciona una meta para editar.");
            }
        });

        // Delete Goal
        mainView.getBtnDeleteGoal().setOnAction(e -> {
            Goal selected = getSelectedGoal();
            if (selected != null) {
                handleDeleteGoal(selected);
            } else {
                showAlert(AlertType.WARNING, "Selección requerida", "Por favor selecciona una meta para eliminar.");
            }
        });

        // View Details (Double click on list)
        mainView.getListGoals().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Goal selected = getSelectedGoal();
                if (selected != null) {
                    detailController.showDetails(selected);
                }
            }
        });
        
        // Update progress bar on selection
        mainView.getListGoals().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            Goal selected = getSelectedGoal();
            if (selected != null) {
                double progress = 0.0;
                if (selected.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                    progress = selected.getCurrentAmount().divide(selected.getTargetAmount(), 4, java.math.RoundingMode.HALF_UP).doubleValue();
                }
                mainView.getProgressBarGoal().setProgress(Math.min(1.0, progress));
            } else {
                mainView.getProgressBarGoal().setProgress(0.0);
            }
        });
    }

    private Goal getSelectedGoal() {
        int index = mainView.getListGoals().getSelectionModel().getSelectedIndex();
        if (index >= 0 && currentAccount != null && index < currentAccount.getGoals().size()) {
            return currentAccount.getGoals().get(index);
        }
        return null;
    }

    public void setAccount(Account account) {
        this.currentAccount = account;
        if (currentAccount != null) {
            onNotify(AccountManager.getAccounts());
        }
    }

    private void refreshView() {
        Platform.runLater(() -> {
            if (currentAccount != null) {
                List<String> goalNames = currentAccount.getGoals().stream()
                        .map(Goal::getName)
                        .toList();
                mainView.getListGoals().setItems(FXCollections.observableArrayList(goalNames));
            }
        });
    }

    @Override
    public void onNotify(List<Account> accountsList) {
        if (currentAccount == null) return;
        
        // Find if this specific account was updated
        for (Account a : accountsList) {
            if (a.getName().equals(this.currentAccount.getName())) {
                this.currentAccount = a;
                break;
            }
        }

        List<Movement> movements = currentAccount.getMovements();
        recalculateGoalsProgress(currentAccount.getGoals(), movements);

        AccountManager.saveAccountsData();
        refreshView();
    }

    private BigDecimal calculateActualBalance() {
        if (currentAccount == null) return BigDecimal.ZERO;

        BigDecimal balance = currentAccount.getInitialBalance();
        if (balance == null) balance = BigDecimal.ZERO;

        List<Movement> movements = currentAccount.getMovements();
        if (movements != null) {
            for (Movement m : movements) {
                if (m.getCategory() != null) {
                    switch (m.getCategory().getType()) {
                        case INCOME: balance = balance.add(m.getAmount()); break;
                        case EXPENSE: balance = balance.subtract(m.getAmount()); break;
                        default: break;
                    }
                }
            }
        }
        return balance;
    }

    private void recalculateGoalsProgress(List<Goal> goals, List<Movement> movements) {
        BigDecimal totalBalance = calculateActualBalance();

        if (goals != null) {
            for (Goal goal : goals) {
                BigDecimal progress = totalBalance.min(goal.getTargetAmount());
                goal.setCurrentAmount(progress.max(BigDecimal.ZERO));
            }
        }
    }

    private void handleAddGoal() {
        String name = mainView.getTxtGoalName().getText().trim();
        String targetStr = mainView.getTxtTargetAmount().getText().trim();
        String desc = mainView.getTxtDescription().getText().trim();

        if (name.isEmpty() || targetStr.isEmpty()) {
            showAlert(AlertType.ERROR, "Datos incompletos", "Por favor ingresa un nombre y monto objetivo válidos.");
            return;
        }

        try {
            BigDecimal target = new BigDecimal(targetStr);
            if (target.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert(AlertType.ERROR, "Monto inválido", "El monto objetivo debe ser mayor a cero.");
                return;
            }

            Goal newGoal = new Goal(name, target, desc);

            if (currentAccount != null) {
                BigDecimal currentBalance = calculateActualBalance();
                newGoal.setCurrentAmount(currentBalance);

                currentAccount.getGoals().add(newGoal);
                AccountManager.saveAccountsData();
                refreshView();
                
                // Clear fields
                mainView.getTxtGoalName().clear();
                mainView.getTxtTargetAmount().clear();
                mainView.getTxtDescription().clear();
                
                logger.info("Goal created: '{}' with target={} for account '{}'.",
                        name, target, currentAccount.getName());
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Formato inválido", "Por favor ingresa un monto numérico válido.");
        }
    }

    private void handleDeleteGoal(Goal goal) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar la meta: " + goal.getName() + "?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (currentAccount != null) {
                logger.info("Goal deleted: '{}' from account '{}'.",
                        goal.getName(), currentAccount.getName());
                currentAccount.getGoals().remove(goal);
                AccountManager.saveAccountsData();
                refreshView();
            }
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}