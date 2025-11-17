package reminder_controller;

import java.awt.event.*;

import reminder_model.RemindersModel;
import reminder_view.RemindersView;

public class RemindersController implements ActionListener {
    private final RemindersView remindersView = new RemindersView();
    private final RemindersModel remindersModel = new RemindersModel();

    public RemindersController() {

    }

    public void actionPerformed(ActionEvent e) {
        // TODO: Listen to view events
    }

    private void handleReminderAddition() {

    }

    private void handleReminderDeletion() {

    }

    private void handleReminderEdit() {

    }
}
