package reminders.reminder_model;

import java.util.TreeSet;

/**
 * Interface to observe reminder changes.
 */
public interface ReminderObserver {
    public void observeReminders(TreeSet<Reminder> reminders);
}
