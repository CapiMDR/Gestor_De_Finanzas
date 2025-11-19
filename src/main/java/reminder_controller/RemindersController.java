package reminder_controller;

import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import reminder_model.RemindersModel;
import reminder_view.RemindersView;

public class RemindersController implements ActionListener {
    private final RemindersModel remindersModel = new RemindersModel();
    private final RemindersView remindersView = new RemindersView(this, remindersModel);

    public RemindersController() {
        remindersView.showMenu();
    }

    public void actionPerformed(ActionEvent e) {
        // TODO: Escuchar eventos de la UI usando java swing
    }

    public void handleReminderAddition(String name, String message, String dateText) {
        LocalDateTime date = LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        remindersModel.addReminder(name, message, date);
        remindersModel.saveReminders();
    }

    public void handleReminderAddition(String name, String message) {
        remindersModel.addReminder(name, message);
        remindersModel.saveReminders();
    }

    public void handleReminderDeletion() {
        remindersModel.saveReminders();
    }

    public void handleReminderEdit() {
        remindersModel.saveReminders();
    }
}
