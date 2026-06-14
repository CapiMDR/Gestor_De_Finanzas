package reminders.reminder_controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ReminderEditController {

    @FXML private TextField txtReminderName;
    @FXML private TextField txtMessage;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> cmbHour;
    @FXML private ComboBox<String> cmbMinute;

    @FXML
    public void initialize() {
        config.UIUtils.setupTimeComboBoxes(cmbHour, cmbMinute);
    }

    public TextField getTxtReminderName() { return txtReminderName; }
    public TextField getTxtMessage() { return txtMessage; }
    public DatePicker getDatePicker() { return datePicker; }
    public ComboBox<String> getCmbHour() { return cmbHour; }
    public ComboBox<String> getCmbMinute() { return cmbMinute; }
}
