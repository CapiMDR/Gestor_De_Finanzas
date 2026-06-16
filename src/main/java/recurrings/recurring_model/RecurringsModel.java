package recurrings.recurring_model;

import java.util.SortedSet;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import movements.movement_model.MovementCategory;

/**
 * Model in charge of managing recurring reminders.
 * Keeps a TreeSet to guarantee efficient chronological sorting
 * in insertion, deletion and search.
 */
public class RecurringsModel {

    /**
     * Keeping reminders in a TreeSet for quick sorting in
     * insertion/deletion and efficiency when searching for existence.
     * 
     * Loaded from JSON on startup.
     */
    private final SortedSet<RecurringMove> allRecurrings = RecurringJSONHandler.loadRecurrings();

    /** List of observers subscribed to the model changes. */
    private final HashSet<RecurringObserver> observerList = new HashSet<>();

    /**
     * Adds a new recurring reminder to the model.
     *
     * @param concept     Concept or title of the recurring movement.
     * @param amount      Associated amount.
     * @param description Movement description.
     * @param initialDate Initial date and time of the reminder.
     * @param recurrence  Recurrence type (daily, weekly, monthly, etc.)
     */
    public void addRecurring(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence, MovementCategory category) {

        RecurringMove reminder = new RecurringMove(concept, amount, description, initialDate, recurrence, category);
        allRecurrings.add(reminder);
        notifyObservers();
    }

    /**
     * Adds a complete RecurringMove object to the model.
     *
     * @param recMove Recurring reminder.
     */
    public void addRecurring(RecurringMove recMove) {
        allRecurrings.add(recMove);
        notifyObservers();
    }

    /**
     * Removes a recurring reminder if it exists within the model.
     *
     * @param recMove Recurring reminder to remove.
     */
    public void deleteRecurring(RecurringMove recMove) {
        if (allRecurrings.contains(recMove))
            allRecurrings.remove(recMove);
        notifyObservers();
    }

    /**
     * Edits an existing reminder replacing it with a new one.
     * 
     * The previous one is removed and the new one is added to ensure
     * the chronological order within the TreeSet.
     *
     * @param oldRecMove Previous reminder.
     * @param newRecMove New updated reminder.
     */
    public void editRecurring(RecurringMove oldRecMove, RecurringMove newRecMove) {
        // Removing the edited reminder and creating a new one to preserve the
        // chronological order in the treeset
        if (!allRecurrings.contains(oldRecMove))
            return;
        deleteRecurring(oldRecMove);
        addRecurring(newRecMove);
        notifyObservers();
    }

    /**
     * Notifies all registered observers, sending the complete list
     * of reminders.
     */
    private void notifyObservers() {
        for (RecurringObserver observer : observerList) {
            observer.observeRecurrings(allRecurrings);
        }
    }

    /**
     * Registers a new observer.
     *
     * @param observer Observer to add.
     */
    public void addObserver(RecurringObserver observer) {
        observerList.add(observer);
    }

    /**
     * Removes an existing observer from the model.
     *
     * @param observer Observer to remove.
     */
    public void removeObserver(RecurringObserver observer) {
        if (observerList.contains(observer))
            observerList.remove(observer);
    }

    /**
     * Returns the complete set of recurring reminders.
     *
     * @return TreeSet with all RecurringMoves.
     */
    public SortedSet<RecurringMove> getRecurrings() {
        return allRecurrings;
    }

    /**
     * Saves all recurring reminders in the JSON file.
     */
    public void saveRecurrings() {
        RecurringJSONHandler.saveRecurrings(allRecurrings);
    }
}


