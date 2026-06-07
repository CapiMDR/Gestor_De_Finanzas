package reminders.reminder_controller;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import reminders.reminder_model.RemindersModel;
import reminders.reminder_model.Reminder;
import reminders.reminder_view.RemindersEditorView;
import reminders.reminder_view.RemindersView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main controller in charge of coordinating the interaction between the
 * reminders model, the views and the periodic verification logic.
 * 
 * Responsible for:
 * - Listening to view requests to add, edit or delete reminders.
 * - Triggering reminders when their scheduled date is met.
 * - Automatically synchronizing changes with the JSON storage.
 */
public class RemindersController {

    private static final Logger logger = LoggerFactory.getLogger(RemindersController.class);

    /** Model that manages the collection of reminders. */
    private final RemindersModel remindersModel = new RemindersModel();

    /** Main view where reminders are shown. */
    private final RemindersView remindersView = new RemindersView(this, remindersModel);

    /** Scheduled executor to periodically check reminders. */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Creates the controller, shows the main view and schedules the
     * periodic reminder watch task.
     */
    public RemindersController() {
        logger.info("RemindersController initialized.");
        scheduler.scheduleAtFixedRate(this::watchReminders, 0, 1, TimeUnit.SECONDS);
    }

    public void showRemindersView() {
        remindersView.setVisible(true);
    }

    /**
     * Checks reminders every second and activates those whose date has been
     * reached.
     * 
     * Since the reminders are sorted chronologically, if one should not
     * be triggered yet, the following ones shouldn't either.
     */
    private void watchReminders() {
        for (Reminder reminder : remindersModel.getReminders()) {
            if (reminder.shouldTrigger()) {
                triggerReminder(reminder);
            } else {
                // Since reminders are sorted by their date, if the current one has not
                // reached its time, the others won't either
                break;
            }
        }
    }

    /**
     * Activates a reminder by showing its alert in the graphical interface.
     * 
     * @param reminder Reminder to be triggered.
     */
    private void triggerReminder(Reminder reminder) {
        logger.info("Reminder triggered: '{}' scheduled for {}.", reminder.getName(), reminder.getDate());
        reminder.setTriggered(true);
        javax.swing.SwingUtilities.invokeLater(() -> {
            showReminderAlert(reminder);
        });
    }

    private void showReminderAlert(Reminder reminder) {
        JOptionPane.showMessageDialog(remindersView,
                reminder.getMessage(),
                reminder.getName() + " - Recordatorio",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles the request to add a new reminder from the view.
     * 
     * @param name    Reminder name.
     * @param message Reminder message.
     * @param date    Scheduled date and time.
     */
    public void handleReminderAddition(String name, String message, LocalDateTime date) {
        if (!isValidReminder(name, message, date)) {
            logger.warn("Invalid reminder data rejected: name='{}', date={}.", name, date);
            return;
        }

        remindersModel.addReminder(name, message, date);
        remindersModel.saveReminders();
        logger.info("Reminder added: '{}' scheduled for {}.", name, date);
    }

    /**
     * Verifies if the data provided for a reminder is valid.
     * 
     * @param name    Reminder name.
     * @param message Associated message.
     * @param date    Activation date.
     * @return {@code true} if the data is valid, {@code false} otherwise.
     */
    private boolean isValidReminder(String name, String message, LocalDateTime date) {
        if (name == null || name.isEmpty())
            return false;
        if (date == null)
            return false;
        return true;
    }

    /**
     * Handles the request to delete a reminder.
     * 
     * @param reminder Reminder to delete.
     */
    public void handleReminderDeletion(Reminder reminder) {
        remindersModel.deleteReminder(reminder);
        remindersModel.saveReminders();
        logger.info("Reminder deleted: '{}'.", reminder.getName());
    }

    /**
     * Handles the editing of a reminder replacing the old one with a
     * modified one.
     * 
     * @param oldReminder Original reminder.
     * @param newReminder Edited version of the reminder.
     */
    public void handleReminderEdit(Reminder oldReminder, Reminder newReminder) {
        remindersModel.editReminder(oldReminder, newReminder);
        remindersModel.saveReminders();
        logger.info("Reminder edited: '{}' -> '{}'.", oldReminder.getName(), newReminder.getName());
    }

    /**
     * Opens the edition window for a specific reminder.
     * 
     * @param reminder Reminder to edit.
     */
    public void onEditRequest(Reminder reminder) {
        RemindersEditorView editor = new RemindersEditorView(null, reminder);
        editor.setVisible(true);

        Reminder editedReminder = editor.getEditedReminder();
        if (editedReminder != null)
            handleReminderEdit(reminder, editedReminder);
    }
}
