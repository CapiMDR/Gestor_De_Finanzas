package reminders.reminder_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the Reminders view.
 * Replaces {@code RemindersView.java} (Swing).
 *
 * <p>Layout is defined declaratively in {@code /fxml/reminders.fxml}.
 * Uses the native JavaFX {@link DatePicker}, replacing the legacy {@code JCalendar} dependency.
 * Business logic is delegated to {@code RemindersController}.
 *
 * @see reminders.reminder_controller.RemindersController
 */
public class RemindersViewFX {

    @FXML private ListView<String> listReminders;
    @FXML private TextField txtReminderName;
    @FXML private DatePicker datePicker;
    @FXML private Button btnAddReminder;
    @FXML private Button btnDeleteReminder;

    // TODO: Implement — Phase 3.4 (Reminders module migration)
}
