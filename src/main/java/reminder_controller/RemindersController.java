package reminder_controller;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import reminder_model.RemindersModel;
import reminder_model.Reminder;
import reminder_view.RemindersEditorView;
import reminder_view.RemindersView;

public class RemindersController {

    private final RemindersModel remindersModel = new RemindersModel();
    private final RemindersView remindersView = new RemindersView(this, remindersModel);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public RemindersController() {
        remindersView.setVisible(true);
        scheduler.scheduleAtFixedRate(this::watchReminders, 0, 1, TimeUnit.SECONDS);
    }

    //Revisando los recordatorios cada segundo y activando aquellos que deban ser activados
    private void watchReminders() {
        for (Reminder reminder : remindersModel.getReminders()) {
            if (reminder.shouldTrigger()){
                 triggerReminder(reminder);
            }else{
                //Como los recordatorios están ordenados por su fecha, si el actual no ha llegado a su tiempo, los demás tampoco
                break;
            }
        }
    }
    
    private void triggerReminder(Reminder reminder){
        reminder.setTriggered(true);
        javax.swing.SwingUtilities.invokeLater(() -> {
            remindersView.showReminderAlert(reminder);
        });
    }

    public void handleReminderAddition(String name, String message, LocalDateTime date) {
        if (!isValidReminder(name, message, date)) return;

        remindersModel.addReminder(name, message, date);
        remindersModel.saveReminders();
    }

    private boolean isValidReminder(String name, String message, LocalDateTime date) {
        if (name == null || name.isEmpty()) return false;
        if (date == null) return false;
        return true;
    }

    public void handleReminderDeletion(Reminder reminder) {
        remindersModel.deleteReminder(reminder);
        remindersModel.saveReminders();
    }

    public void handleReminderEdit(Reminder oldReminder, Reminder newReminder) {
        remindersModel.editReminder(oldReminder, newReminder);
        remindersModel.saveReminders();
    }

    public void onEditRequest(Reminder reminder) {
        RemindersEditorView editor = new RemindersEditorView(null, reminder);
        editor.setVisible(true);

        Reminder editedReminder = editor.getEditedReminder();
        if (editedReminder != null) handleReminderEdit(reminder, editedReminder);
    }
}
