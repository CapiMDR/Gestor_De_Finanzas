package reminders.reminder_model;

import java.util.SortedSet;
/**
 * Interface to observe reminder changes.
 */
public interface ReminderObserver {
    public void observeReminders(SortedSet<Reminder> reminders);
}

