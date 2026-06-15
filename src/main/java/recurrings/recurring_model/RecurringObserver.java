package recurrings.recurring_model;

import java.util.TreeSet;

/**
 * Interface to observe recurring movement changes.
 */
public interface RecurringObserver {
    public void observeRecurrings(TreeSet<RecurringMove> reminders);
}
