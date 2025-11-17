package reminder_model;

import java.util.Date;
import java.util.HashSet;

public class RemindersModel {
    private HashSet<Reminder> remindersList = new HashSet<>();
    private HashSet<ReminderObserver> observerList = new HashSet<>();

    public void addReminder(Reminder reminder) {
        remindersList.add(reminder);
        notifyObservers();
    }

    public void deleteReminder(Reminder reminder) {
        if (remindersList.contains(reminder))
            remindersList.remove(reminder);
        notifyObservers();
    }

    public void editReminder(Reminder reminder, String name, String message, Date date) {
        if (!remindersList.contains(reminder))
            return;
        reminder.setName(name);
        reminder.setMessage(message);
        reminder.setDate(date);
        notifyObservers();

    }

    private void notifyObservers() {
        for (ReminderObserver observer : observerList) {
            observer.observe();
        }
    }

    public void addObserver(ReminderObserver observer) {
        observerList.add(observer);
    }

    public void removeObserver(ReminderObserver observer) {
        if (observerList.contains(observer))
            observerList.remove(observer);
    }

    public HashSet<Reminder> getReminders() {
        return remindersList;
    }
}
