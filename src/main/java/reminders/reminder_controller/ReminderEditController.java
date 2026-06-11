package reminders.reminder_controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ReminderEditController {

    @FXML private TextField txtReminderName;
    @FXML private TextField txtMessage;
    @FXML private DatePicker datePicker;

    public TextField getTxtReminderName() { return txtReminderName; }
    public TextField getTxtMessage() { return txtMessage; }
    public DatePicker getDatePicker() { return datePicker; }
}
