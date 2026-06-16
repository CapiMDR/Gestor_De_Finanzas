package goals.goals_controller;

import java.util.List;
import java.math.BigDecimal;
import notifications.notification_controller.NotificationManager;

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

    private static final String STR_SELECCION_REQUERIDA = "Selección requerida";

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
        setupListCellFactory();
        mainView.getBtnAddGoal().setOnAction(e -> handleAddGoal());
        mainView.getBtnEditGoal().setOnAction(e -> handleEditAction());
        mainView.getBtnDeleteGoal().setOnAction(e -> handleDeleteAction());
        
        mainView.getListGoals().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) handleViewDetailsAction();
        });
        mainView.getBtnViewGoalDetails().setOnAction(e -> handleViewDetailsAction());
        
        mainView.getListGoals().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> handleListSelection());
        
        mainView.getProgressContainer().setVisible(false);
    }

    private void setupListCellFactory() {
        mainView.getListGoals().setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
    }

    private void handleEditAction() {
        Goal selected = getSelectedGoal();
        if (selected != null) {
            if (editController.showEditDialog(selected)) {
                AccountManager.saveAccountsData();
                refreshView();
            }
        } else {
            showAlert(AlertType.WARNING, STR_SELECCION_REQUERIDA, "Por favor selecciona una meta para editar.");
        }
    }

    private void handleDeleteAction() {
        Goal selected = getSelectedGoal();
        if (selected != null) {
            handleDeleteGoal(selected);
        } else {
            showAlert(AlertType.WARNING, STR_SELECCION_REQUERIDA, "Por favor selecciona una meta para eliminar.");
        }
    }

    private void handleViewDetailsAction() {
        Goal selected = getSelectedGoal();
        if (selected != null) {
            detailController.showDetails(selected);
        } else {
            showAlert(AlertType.WARNING, STR_SELECCION_REQUERIDA, "Por favor selecciona una meta para ver sus detalles.");
        }
    }

    private void handleListSelection() {
        Goal selected = getSelectedGoal();
        if (selected != null) {
            mainView.getProgressContainer().setVisible(true);
            double progress = 0.0;
            if (selected.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
                progress = selected.getCurrentAmount().divide(selected.getTargetAmount(), 4, java.math.RoundingMode.HALF_UP).doubleValue();
            }
            mainView.getProgressBarGoal().setProgress(Math.min(1.0, progress));
        } else {
            mainView.getProgressContainer().setVisible(false);
        }
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
            mainView.setAccountName(currentAccount.getName());
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
                checkAndNotifyGoalReached(goal);
            }
        }
    }

    private void checkAndNotifyGoalReached(Goal goal) {
        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            if (!goal.isNotificadaCompleta()) {
                goal.setNotificadaCompleta(true);
                NotificationManager.getInstance().agregarNotificacion(
                    new notifications.notification_model.AppNotification(
                        notifications.notification_model.AppNotification.Tipo.META_CUMPLIDA,
                        "¡Meta cumplida!",
                        "Has alcanzado la meta '" + goal.getName() + "' en tu cuenta " + currentAccount.getName() + ".",
                        java.time.LocalDateTime.now(java.time.ZoneId.systemDefault())
                    )
                );
                logger.info("Meta cumplida notificada: {}", goal.getName());
            }
        } else {
            // Reset flag if balance drops below target
            goal.setNotificadaCompleta(false);
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

            saveNewGoal(name, target, desc);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Formato inválido", "Por favor ingresa un monto numérico válido.");
        }
    }

    private void saveNewGoal(String name, BigDecimal target, String desc) {
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
    }

    private void handleDeleteGoal(Goal goal) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar la meta: " + goal.getName() + "?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK && currentAccount != null) {
                logger.info("Goal deleted: '{}' from account '{}'.",
                        goal.getName(), currentAccount.getName());
                currentAccount.getGoals().remove(goal);
                AccountManager.saveAccountsData();
                refreshView();
        }
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Unregisters this controller as an observer to prevent memory leaks.
     */
    public void dispose() {
        AccountManagerSubject.removeObserver(this);
    }
}