package reminders.reminder_view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * Base class containing the common form fields for creating and editing a Reminder.
 * Used to avoid duplication between RemindersViewFX and ReminderEditController.
 */
public abstract class AbstractReminderForm {

    @FXML protected TextField txtReminderName;
    @FXML protected TextField txtMessage;
    @FXML protected DatePicker datePicker;
    @FXML protected ComboBox<String> cmbHour;
    @FXML protected ComboBox<String> cmbMinute;

    protected void initTimeComboBoxes() {
        utils.UIUtils.setupTimeComboBoxes(cmbHour, cmbMinute);
    }

    public TextField getTxtReminderName() { return txtReminderName; }
    public TextField getTxtMessage() { return txtMessage; }
    public DatePicker getDatePicker() { return datePicker; }
    public ComboBox<String> getCmbHour() { return cmbHour; }
    public ComboBox<String> getCmbMinute() { return cmbMinute; }
}
