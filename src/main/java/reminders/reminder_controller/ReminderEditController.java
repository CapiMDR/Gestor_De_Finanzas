package reminders.reminder_controller;

import javafx.fxml.FXML;
import reminders.reminder_view.AbstractReminderForm;

public class ReminderEditController extends AbstractReminderForm {

    @FXML
    public void initialize() {
        initTimeComboBoxes();
    }
}
