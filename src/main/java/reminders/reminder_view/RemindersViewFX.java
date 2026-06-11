package reminders.reminder_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import reminders.reminder_model.Reminder;


/**
 * JavaFX controller for the Reminders view.
 * Replaces {@code RemindersView.java} (Swing).
 *
 * Layout is defined declaratively in {@code /fxml/reminders.fxml}.
 * Uses the native JavaFX {@link DatePicker}, replacing the legacy {@code JCalendar} dependency.
 * Business logic is delegated to {@code RemindersController}.
 *
 * @see reminders.reminder_controller.RemindersController
 */
public class RemindersViewFX {

    @FXML private ListView<String> listReminders;
    @FXML private TextField txtReminderName;
    @FXML private TextField txtMessage;
    @FXML private DatePicker datePicker;
    @FXML private Button btnAddReminder;
    @FXML private Button btnEditReminder;
    @FXML private Button btnDeleteReminder;

    private reminders.reminder_controller.RemindersController controller;

    @FXML
    public void initialize() {
        btnAddReminder.setOnAction(e -> addReminder());
        btnDeleteReminder.setOnAction(e -> deleteReminder());
        btnEditReminder.setOnAction(e -> editReminder());
    }

    public void setController(reminders.reminder_controller.RemindersController controller) {
        this.controller = controller;
    }

    public void refreshList(java.util.List<Reminder> reminders) {
        listReminders.getItems().clear();
        for (Reminder r : reminders) {
            listReminders.getItems().add(String.format("%s: %s (Fecha: %s)", r.getName(), r.getMessage(), r.getDate().toString()));
        }
    }

    private void addReminder() {
        if (controller != null) {
            String name = txtReminderName.getText().trim();
            String message = txtMessage.getText().trim();
            java.time.LocalDate date = datePicker.getValue();
            
            if (date != null) {
                // Defaulting to midnight for MVP
                controller.handleReminderAddition(name, message, date.atStartOfDay());
            }
        }
    }

    private void deleteReminder() {
        if (controller != null) {
            int selectedIndex = listReminders.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                // Call controller by index since we just displayed them sequentially
                controller.deleteReminderByIndex(selectedIndex);
            }
        }
    }

    private void editReminder() {
        if (controller != null) {
            int selectedIndex = listReminders.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                controller.editReminderByIndex(selectedIndex);
            }
        }
    }
}
