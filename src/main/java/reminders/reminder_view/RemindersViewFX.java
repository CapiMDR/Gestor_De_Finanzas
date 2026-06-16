package reminders.reminder_view;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
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
public class RemindersViewFX extends AbstractReminderForm {

    @FXML private ListView<String> listReminders;
    @FXML private Button btnAddReminder;
    @FXML private Button btnEditReminder;
    @FXML private Button btnDeleteReminder;
    @FXML private Label lblAccountName;
    @FXML private Button btnVolver;

    private Runnable onBack;

    private reminders.reminder_controller.RemindersController controller;

    @FXML
    public void initialize() {
        if (btnVolver != null) {
            btnVolver.setOnAction(e -> {
                if (onBack != null) onBack.run();
            });
        }
        initTimeComboBoxes();

        btnAddReminder.setOnAction(e -> addReminder());
        btnDeleteReminder.setOnAction(e -> deleteReminder());
        btnEditReminder.setOnAction(e -> editReminder());
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    public void setAccountName(String accountName) {
        if (lblAccountName != null && accountName != null) {
            lblAccountName.setText(accountName);
        }
    }

    public void setController(reminders.reminder_controller.RemindersController controller) {
        this.controller = controller;
    }

    public void refreshList(List<Reminder> reminders) {
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
            
            if (date != null && cmbHour.getValue() != null && cmbMinute.getValue() != null) {
                int h = Integer.parseInt(cmbHour.getValue());
                int m = Integer.parseInt(cmbMinute.getValue());
                java.time.LocalTime time = java.time.LocalTime.of(h, m);
                controller.handleReminderAddition(name, message, java.time.LocalDateTime.of(date, time));
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
