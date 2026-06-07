package recurringMoves.recurring_controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import recurringMoves.recurring_model.RecurrenceType;
import recurringMoves.recurring_model.RecurringMove;
import recurringMoves.recurring_model.RecurringsModel;
import recurringMoves.recurring_view.RecurringMoveAlertView;
import recurringMoves.recurring_view.RecurringsEditorView;
import recurringMoves.recurring_view.RecurringsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main controller in charge of coordinating the interaction between the view,
 * the model and the notification logic of recurring payments.
 *
 * Administrates the creation, edition, deletion and automatic monitoring
 * of {@link RecurringMove} using a {@link ScheduledExecutorService}.
 */
public class RecurringsController {

    private static final Logger logger = LoggerFactory.getLogger(RecurringsController.class);

    /** Model containing all recurring operations. */
    private final RecurringsModel recurringsModel = new RecurringsModel();

    /** Main view in charge of showing recurring reminders. */
    private final RecurringsView recurringsView = new RecurringsView(this, recurringsModel);

    /** Executor service to periodically check reminders. */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void showRecMovesView() {
        recurringsView.setVisible(true);
    }

    /**
     * Constructs the controller, initializes the view and starts the periodic
     * monitoring of reminders.
     */
    public RecurringsController() {
        logger.info("RecurringsController initialized.");
        recurringsView.setVisible(true);
        scheduler.scheduleAtFixedRate(this::watchRecurrings, 0, 1, TimeUnit.SECONDS);
    }

    // Checking reminders every second and activating those that should be
    // activados

    /**
     * Checks all sorted reminders and activates those whose date has already
     * passed.
     * Since the reminders are sorted, the check ends when one shouldn't
     * be triggered yet.
     */
    private void watchRecurrings() {
        for (RecurringMove recMove : recurringsModel.getRecurrings()) {
            if (recMove.shouldTrigger()) {
                triggerRecurring(recMove);
                recMove.setTriggered(true);
            } else {
                // Since reminders are chronologically sorted in the TreeSet,
                // if the current one shouldn't trigger yet, the following ones won't either.
                break;
            }
        }
    }

    // When a recurring payment notification is triggered, it is removed and the
    // next instance is created according to its frequency

    /**
     * Handles the activation of a recurring reminder.
     *
     * The process consists of:
     * - Deleting the current reminder.
     * - Creating its next instance depending on the frequency.
     * - Saving the changes.
     * - Showing an alert in the view.
     *
     * @param recMove the reminder that should be activated
     */
    private void triggerRecurring(RecurringMove recMove) {
        logger.info("Recurring movement triggered: '{}' (recurrence={}).",
                recMove.getConcept(), recMove.getRecurrence());
        SwingUtilities.invokeLater(() -> showRecurringMoveView(recMove));
    }

    private void showRecurringMoveView(RecurringMove recMove) {

        List<Account> accounts = AccountManager.getAccounts();

        RecurringMoveAlertView view = new RecurringMoveAlertView(recurringsView, recMove, accounts);

        view.setOnApply(() -> {
            Account selected = view.getSelectedAccount();
            if (selected != null) {
                performMovement(recMove, selected);

                // The "next date" is only updated if the movement is applied to an account,
                // if the payment is not made the user will continue to be notified
                recurringsModel.deleteRecurring(recMove);
                RecurringMove next = recMove.createNextOccurrence();
                recurringsModel.addRecurring(next);
                recurringsModel.saveRecurrings();
                logger.info("Recurring movement applied to account '{}'. Next occurrence: {}.",
                        selected.getName(), next.getInitialDate());
            } else {
                logger.warn("Recurring movement '{}' dismissed — no account selected.",
                        recMove.getConcept());
            }
            view.dispose();
        });

        view.setOnCancel(() -> view.dispose());

        view.setVisible(true);
    }

    private void performMovement(RecurringMove recMove, Account account) {
        Movement movement = new Movement(UUID.randomUUID(), recMove.getDescription(), recMove.getAmount(),
                recMove.getCategory(),
                account, LocalDateTime.now());

        account.addMovement(movement);
        AccountManager.saveAccountsData();

        AccountManagerSubject.notifyObservers(AccountManager.getAccounts());
    }

    /**
     * Processes the request to create a new reminder.
     *
     * @param concept     reminder name
     * @param amount      associated amount
     * @param description optional description
     * @param initialDate initial date
     * @param recurrence  repetition type
     */
    public void handleRecurringAddition(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence, MovementCategory category) {
        if (!isValidRecurring(concept, amount, description, initialDate, recurrence)) {
            logger.warn("Invalid recurring movement data rejected: concept='{}'.", concept);
            return;
        }

        recurringsModel.addRecurring(concept, amount, description, initialDate, recurrence, category);
        recurringsModel.saveRecurrings();
        logger.info("Recurring movement added: '{}' ({}, every {}).", concept, amount, recurrence);
    }

    /**
     * Validates the necessary fields to create or edit a recurring reminder.
     *
     * @return true if the data is valid, false otherwise
     */
    private boolean isValidRecurring(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence) {
        if (concept == null || concept.isEmpty())
            return false;

        if (amount == null)
            return false;

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return false;

        if (amount.scale() > 2)
            return false;

        return true;
    }

    /**
     * Handles the deletion of a recurring reminder.
     *
     * @param recMove the reminder to delete
     */
    public void handleRecurringDeletion(RecurringMove recMove) {
        recurringsModel.deleteRecurring(recMove);
        recurringsModel.saveRecurrings();
        logger.info("Recurring movement deleted: '{}'.", recMove.getConcept());
    }

    /**
     * Processes the editing of a recurring reminder given an old one and a
     * new one.
     *
     * @param oldRecMove original reminder
     * @param newRecMove edited reminder
     */
    public void handleRecurringEdit(RecurringMove oldRecMove, RecurringMove newRecMove) {
        recurringsModel.editRecurring(oldRecMove, newRecMove);
        recurringsModel.saveRecurrings();
        logger.info("Recurring movement edited: '{}' -> '{}'.",
                oldRecMove.getConcept(), newRecMove.getConcept());
    }

    /**
     * Opens the edition window for a reminder and processes the result if the
     * user confirms the changes.
     *
     * @param recMove reminder to edit
     */
    public void onEditRequest(RecurringMove recMove) {
        RecurringsEditorView editor = new RecurringsEditorView(null, recMove);
        editor.setVisible(true);

        RecurringMove editedRecMove = editor.getEditedRecMove();
        if (editedRecMove != null)
            handleRecurringEdit(recMove, editedRecMove);
    }
}
