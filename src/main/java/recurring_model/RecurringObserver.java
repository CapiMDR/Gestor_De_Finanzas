package recurring_model;

import java.util.TreeSet;

public interface RecurringObserver {
    public void observeRecurrings(TreeSet<RecurringMove> reminders);
}
