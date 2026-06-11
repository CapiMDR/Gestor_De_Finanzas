package reminders.reminder_controller;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.fxml.FXMLLoader;

import reminders.reminder_model.RemindersModel;
import reminders.reminder_model.Reminder;
import reminders.reminder_view.RemindersViewFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
    private RemindersViewFX remindersView;

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

    public void setView(RemindersViewFX view) {
        this.remindersView = view;
        refreshView();
    }

    private void refreshView() {
        if (remindersView != null) {
            Platform.runLater(() -> remindersView.refreshList(new java.util.ArrayList<>(remindersModel.getReminders())));
        }
    }

    /**
     * Checks reminders every second and activates those whose date has been
     * reached.
     * 
     * Since the reminders are sorted chronologically, if one should not
     * be triggered yet, the following ones shouldn't either.
     */
    private void watchReminders() {
        LocalDateTime now = LocalDateTime.now();
        for (Reminder reminder : remindersModel.getReminders()) {
            if (reminder.getDate().isAfter(now)) {
                // Since reminders are sorted by date, if this one is in the future,
                // all subsequent ones are also in the future.
                break;
            }
            if (!reminder.isTriggered()) {
                triggerReminder(reminder);
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
        remindersModel.saveReminders();
        Platform.runLater(() -> {
            showReminderAlert(reminder);
        });
    }

    private void showReminderAlert(Reminder reminder) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(reminder.getName() + " - Recordatorio");
        alert.setHeaderText(null);
        alert.setContentText(reminder.getMessage());
        alert.show();
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
            showAlert(Alert.AlertType.WARNING, "Datos incompletos", "Debe proporcionar al menos un nombre y la fecha del recordatorio.");
            return;
        }

        remindersModel.addReminder(name, message, date);
        remindersModel.saveReminders();
        logger.info("Reminder added: '{}' scheduled for {}.", name, date);
        refreshView();
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
        refreshView();
    }

    public void deleteReminderByIndex(int index) {
        java.util.List<Reminder> list = new java.util.ArrayList<>(remindersModel.getReminders());
        if (index >= 0 && index < list.size()) {
            handleReminderDeletion(list.get(index));
        }
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
        refreshView();
    }

    public void editReminderByIndex(int index) {
        java.util.List<Reminder> list = new java.util.ArrayList<>(remindersModel.getReminders());
        if (index >= 0 && index < list.size()) {
            onEditRequest(list.get(index));
        }
    }

    /**
     * Opens the edition window for a specific reminder.
     * 
     * @param reminder Reminder to edit.
     */
    public void onEditRequest(Reminder reminder) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reminders/reminder_edit.fxml"));
            DialogPane dialogPane = loader.load();
            ReminderEditController editController = loader.getController();

            editController.getTxtReminderName().setText(reminder.getName());
            editController.getTxtMessage().setText(reminder.getMessage());
            editController.getDatePicker().setValue(reminder.getDate().toLocalDate());
            editController.getCmbHour().setValue(String.format("%02d", reminder.getDate().getHour()));
            editController.getCmbMinute().setValue(String.format("%02d", reminder.getDate().getMinute()));

            Dialog<Reminder> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Editar Recordatorio");

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    try {
                        String name = editController.getTxtReminderName().getText().trim();
                        String msg = editController.getTxtMessage().getText().trim();
                        java.time.LocalDate d = editController.getDatePicker().getValue();
                        int h = Integer.parseInt(editController.getCmbHour().getValue());
                        int m = Integer.parseInt(editController.getCmbMinute().getValue());
                        LocalDateTime dateTime = LocalDateTime.of(d, java.time.LocalTime.of(h, m));

                        if (!isValidReminder(name, msg, dateTime)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Datos inválidos");
                            alert.showAndWait();
                            return null;
                        }

                        return new Reminder(name, msg, dateTime);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            });

            dialog.showAndWait().ifPresent(editedReminder -> {
                handleReminderEdit(reminder, editedReminder);
            });

        } catch (IOException e) {
            logger.error("Failed to load reminder edit dialog", e);
        }
    }

    /**
     * Displays a JavaFX Alert on the UI thread.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
