package recurrings.recurring_model;


import java.util.SortedSet;
/**
 * Interface to observe recurring movement changes.
 */
public interface RecurringObserver {
    public void observeRecurrings(SortedSet<RecurringMove> reminders);
}

