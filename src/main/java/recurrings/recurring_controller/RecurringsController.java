package recurrings.recurring_controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import notifications.notification_controller.NotificationManager;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import recurrings.recurring_model.RecurrenceType;
import recurrings.recurring_model.RecurringMove;
import recurrings.recurring_model.RecurringsModel;
import recurrings.recurring_view.RecurringsViewFX;

/**
 * Main controller for the Recurring Movements Module in JavaFX.
 * Coordinates interaction between the view, the model, and the background notification logic.
 */
public class RecurringsController {

    private static final Logger logger = LoggerFactory.getLogger(RecurringsController.class);

    private final RecurringsModel recurringsModel;
    private RecurringsViewFX recurringsView;
    
    private final RecurringEditController editController = new RecurringEditController();
    private final RecurringAlertController alertController = new RecurringAlertController();

    private final ScheduledExecutorService scheduler;

    /**
     * Default constructor. Initializes with default model and a background scheduler.
     */
    public RecurringsController() {
        this(new RecurringsModel(), Executors.newSingleThreadScheduledExecutor());
    }

    /**
     * Constructor for dependency injection, useful for testing.
     *
     * @param model     the RecurringsModel instance
     * @param scheduler the ScheduledExecutorService to run the background task
     */
    public RecurringsController(RecurringsModel model, ScheduledExecutorService scheduler) {
        this.recurringsModel = model;
        this.scheduler = scheduler;
        logger.info("RecurringsController background scheduler initialized.");
        this.scheduler.scheduleAtFixedRate(this::watchRecurrings, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Injects the JavaFX view when the module is opened by the user.
     */
    public void setView(RecurringsViewFX view) {
        this.recurringsView = view;
        if (view != null) {
            assignEvents();
            refreshView();
        }
    }

    private void assignEvents() {
        // Setup ListView to display only the concepts
        recurringsView.getListRecurrings().setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        // Setup Category ComboBox
        recurringsView.getCmbCategory().getItems().clear();
        recurringsView.getCmbCategory().getItems().addAll("Ingreso", "Egreso");

        // Add Recurring
        recurringsView.getBtnAddRecurring().setOnAction(e -> handleRecurringAddition());

        // Delete Recurring
        recurringsView.getBtnDeleteRecurring().setOnAction(e -> {
            RecurringMove selected = getSelectedRecurring();
            if (selected != null) {
                handleRecurringDeletion(selected);
            } else {
                showAlert(AlertType.WARNING, "Selección requerida", "Por favor selecciona un movimiento recurrente para eliminar.");
            }
        });

        // Edit Recurring (on Edit Button)
        recurringsView.getBtnEditRecurring().setOnAction(e -> {
            RecurringMove selected = getSelectedRecurring();
            if (selected != null) {
                onEditRequest(selected);
            } else {
                showAlert(AlertType.WARNING, "Selección requerida", "Por favor selecciona un movimiento recurrente para editar.");
            }
        });

        // Edit Recurring (on Double Click)
        recurringsView.getListRecurrings().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                RecurringMove selected = getSelectedRecurring();
                if (selected != null) {
                    onEditRequest(selected);
                }
            }
        });
    }

    private RecurringMove getSelectedRecurring() {
        if (recurringsView == null) return null;
        int index = recurringsView.getListRecurrings().getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < recurringsModel.getRecurrings().size()) {
            return (RecurringMove) recurringsModel.getRecurrings().toArray()[index];
        }
        return null;
    }

    private void refreshView() {
        if (recurringsView != null) {
            Platform.runLater(() -> {
                List<String> names = recurringsModel.getRecurrings().stream()
                        .map(RecurringMove::getConcept)
                        .collect(Collectors.toList());
                recurringsView.getListRecurrings().setItems(FXCollections.observableArrayList(names));
            });
        }
    }

    private void watchRecurrings() {
        for (RecurringMove recMove : recurringsModel.getRecurrings()) {
            if (recMove.shouldTrigger()) {
                triggerRecurring(recMove);
                recMove.setTriggered(true);
            } else {
                break;
            }
        }
    }

    private void triggerRecurring(RecurringMove recMove) {
        logger.info("Recurring movement triggered: '{}' (recurrence={}).",
                recMove.getConcept(), recMove.getRecurrence());
        
        Platform.runLater(() -> {
            NotificationManager.getInstance().agregarNotificacion(
                new notifications.notification_model.AppNotification(
                    notifications.notification_model.AppNotification.Tipo.RECURRENTE_VENCIDO,
                    "Pago Recurrente",
                    "El movimiento recurrente '" + recMove.getConcept() + "' está listo para ser aplicado.",
                    java.time.LocalDateTime.now()
                )
            );
            showRecurringMoveView(recMove);
        });
    }

    private void showRecurringMoveView(RecurringMove recMove) {
        List<Account> accounts = AccountManager.getAccounts();
        
        Account selectedAccount = alertController.showAlertDialog(recMove, accounts);
        
        if (selectedAccount != null) {
            performMovement(recMove, selectedAccount);

            recurringsModel.deleteRecurring(recMove);
            RecurringMove next = recMove.createNextOccurrence();
            recurringsModel.addRecurring(next);
            recurringsModel.saveRecurrings();
            
            logger.info("Recurring movement applied to account '{}'. Next occurrence: {}.",
                    selectedAccount.getName(), next.getInitialDate());
            
            refreshView();
        } else {
            logger.warn("Recurring movement '{}' dismissed — no account selected.", recMove.getConcept());
        }
    }

    private void performMovement(RecurringMove recMove, Account account) {
        Movement movement = new Movement(UUID.randomUUID(), recMove.getDescription(), recMove.getAmount(),
                recMove.getCategory(),
                account, LocalDateTime.now());

        account.addMovement(movement);
        AccountManager.saveAccountsData();
        AccountManagerSubject.notifyObservers(AccountManager.getAccounts());
    }

    public void handleRecurringAddition() {
        String concept = recurringsView.getTxtDescription().getText().trim();
        String amountStr = recurringsView.getTxtAmount().getText().trim();
        String categoryStr = recurringsView.getCmbCategory().getSelectionModel().getSelectedItem();
        java.time.LocalDate date = recurringsView.getDatePicker().getValue();

        if (concept.isEmpty() || amountStr.isEmpty() || categoryStr == null || date == null) {
            showAlert(AlertType.ERROR, "Datos inválidos", "Todos los campos son requeridos.");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (!isValidRecurring(concept, amount, concept, date.atStartOfDay(), RecurrenceType.Mensual)) {
                showAlert(AlertType.ERROR, "Datos inválidos", "Asegúrate de que el monto sea mayor a 0 y con máximo 2 decimales.");
                return;
            }

            String typeEnumStr = "Ingreso".equals(categoryStr) ? "INCOME" : "EXPENSE";
            MovementCategory category = new MovementCategory(typeEnumStr, MovementCategory.MovementType.valueOf(typeEnumStr));
            // Defaulting recurrence to Mensual for MVP, this can be added to UI later
            recurringsModel.addRecurring(concept, amount, concept, LocalDateTime.of(date, LocalTime.MIDNIGHT), RecurrenceType.Mensual, category);
            recurringsModel.saveRecurrings();
            
            logger.info("Recurring movement added: '{}' ({}, every {}).", concept, amount, RecurrenceType.Mensual);
            refreshView();
            
            // Clear fields
            recurringsView.getTxtDescription().clear();
            recurringsView.getTxtAmount().clear();
            recurringsView.getDatePicker().setValue(null);
            
        } catch (NumberFormatException ex) {
            showAlert(AlertType.ERROR, "Monto inválido", "Por favor ingresa un monto numérico válido.");
        }
    }

    private boolean isValidRecurring(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence) {
        if (concept == null || concept.isEmpty()) return false;
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (amount.scale() > 2) return false;
        return true;
    }

    public void handleRecurringDeletion(RecurringMove recMove) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que deseas eliminar el movimiento recurrente: " + recMove.getConcept() + "?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            recurringsModel.deleteRecurring(recMove);
            recurringsModel.saveRecurrings();
            logger.info("Recurring movement deleted: '{}'.", recMove.getConcept());
            refreshView();
        }
    }

    public void handleRecurringEdit(RecurringMove oldRecMove, RecurringMove newRecMove) {
        recurringsModel.editRecurring(oldRecMove, newRecMove);
        recurringsModel.saveRecurrings();
        logger.info("Recurring movement edited: '{}' -> '{}'.",
                oldRecMove.getConcept(), newRecMove.getConcept());
        refreshView();
    }

    public void onEditRequest(RecurringMove recMove) {
        RecurringMove editedRecMove = editController.showEditDialog(recMove);
        if (editedRecMove != null) {
            handleRecurringEdit(recMove, editedRecMove);
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
