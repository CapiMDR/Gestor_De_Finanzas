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
        if (cmbHour != null && cmbMinute != null) {
            for (int i = 0; i < 24; i++) cmbHour.getItems().add(String.format("%02d", i));
            for (int i = 0; i < 60; i++) cmbMinute.getItems().add(String.format("%02d", i));
        }
    }

    public TextField getTxtReminderName() { return txtReminderName; }
    public TextField getTxtMessage() { return txtMessage; }
    public DatePicker getDatePicker() { return datePicker; }
    public ComboBox<String> getCmbHour() { return cmbHour; }
    public ComboBox<String> getCmbMinute() { return cmbMinute; }
}
