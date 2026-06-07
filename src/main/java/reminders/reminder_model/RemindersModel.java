package reminders.reminder_model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Model in charge of managing simple (non-recurring) reminders.
 * 
 * Manages the creation, editing, deletion and storage of {@link Reminder}
 * objects, as well as notifying registered observers every time
 * a change occurs in the collection.
 */
public class RemindersModel {

    // Keeping reminders in a TreeSet for fast sorting on
    // insertion/deletion and efficiency when checking existence
    /** Main collection of reminders, sorted by date and name. */
    private final TreeSet<Reminder> allReminders = ReminderJSONHandler.loadReminders();

    /**
     * List of observers that will be notified when reminders change.
     */
    private final HashSet<ReminderObserver> observerList = new HashSet<>();

    /**
     * Adds a new reminder using its individual data.
     *
     * @param name    reminder identifier name
     * @param message reminder message or description
     * @param date    reminder date and time
     */
    public void addReminder(String name, String message, LocalDateTime date) {
        Reminder reminder = new Reminder(name, message, date);
        allReminders.add(reminder);
        notifyObservers();
    }

    /**
     * Adds an existing reminder to the collection.
     *
     * @param reminder already built reminder
     */
    public void addReminder(Reminder reminder) {
        allReminders.add(reminder);
        notifyObservers();
    }

    /**
     * Deletes a specific reminder if it exists in the set.
     *
     * @param reminder reminder to delete
     */
    public void deleteReminder(Reminder reminder) {
        if (allReminders.contains(reminder))
            allReminders.remove(reminder);
        notifyObservers();
    }

    /**
     * Edits a reminder replacing it with a new instance.
     * 
     * The original reminder is removed and the new one is added to preserve
     * the chronological order within the {@link TreeSet}.
     *
     * @param oldReminder reminder that will be replaced
     * @param newReminder updated reminder
     */
    public void editReminder(Reminder oldReminder, Reminder newReminder) {
        // Removing the edited reminder and creating a new one to preserve the
        // chronological order in the treeset
        if (!allReminders.contains(oldReminder))
            return;
        deleteReminder(oldReminder);
        addReminder(newReminder);
        notifyObservers();
    }

    /**
     * Notifies all observers that the collection of reminders has
     * changed.
     */
    private void notifyObservers() {
        for (ReminderObserver observer : observerList) {
            observer.observeReminders(allReminders);
        }
    }

    /**
     * Registers an observer to be notified of changes.
     *
     * @param observer object implementing {@link ReminderObserver}
     */
    public void addObserver(ReminderObserver observer) {
        observerList.add(observer);
    }

    /**
     * Removes a previously registered observer.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(ReminderObserver observer) {
        if (observerList.contains(observer))
            observerList.remove(observer);
    }

    /**
     * Returns the sorted set of all reminders.
     *
     * @return {@link TreeSet} with the reminders
     */
    public TreeSet<Reminder> getReminders() {
        return allReminders;
    }

    /**
     * Saves all reminders in persistent storage (JSON file).
     */
    public void saveReminders() {
        ReminderJSONHandler.saveReminders(allReminders);
    }
}
