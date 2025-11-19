package reminder_model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

public class RemindersModel {
    // Manteniendo a los recordatorios en un TreeSet para rápido ordenamiento en
    // inserción/eliminación y eficiencia al buscar existencia
    private TreeSet<Reminder> allReminders = ReminderJSONHandler.loadReminders();

    private HashSet<ReminderObserver> observerList = new HashSet<>();

    public void addReminder(String name, String message, LocalDateTime date) {
        Reminder reminder = new Reminder(name, message, date);
        allReminders.add(reminder);
        notifyObservers();
    }

    public void addReminder(String name, String message) {
        Reminder reminder = new Reminder(name, message);
        allReminders.add(reminder);
        notifyObservers();
    }

    public void deleteReminder(Reminder reminder) {
        if (allReminders.contains(reminder))
            allReminders.remove(reminder);
        notifyObservers();
    }

    public void editReminder(Reminder oldReminder, String newName, String newMessage, LocalDateTime newDate) {
        // Eliminando al recordatorio que se editó y creando uno nuevo para preservar el
        // orden cronológico en el treeset
        if (!allReminders.contains(oldReminder))
            return;
        deleteReminder(oldReminder);
        addReminder(newName, newMessage, newDate);
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

    public TreeSet<Reminder> getReminders() {
        return allReminders;
    }

    public void saveReminders() {
        ReminderJSONHandler.saveReminders(allReminders);
    }
}
