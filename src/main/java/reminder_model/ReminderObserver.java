package reminder_model;

import java.util.TreeSet;

public interface ReminderObserver {
    public void observeReminders(TreeSet<Reminder> reminders);
}
