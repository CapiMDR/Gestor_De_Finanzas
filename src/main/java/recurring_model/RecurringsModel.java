package recurring_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

public class RecurringsModel {
    // Manteniendo a los recordatorios en un TreeSet para rápido ordenamiento en
    // inserción/eliminación y eficiencia al buscar existencia
    private final TreeSet<RecurringMove> allRecurrings = RecurringJSONHandler.loadReminders();

    private final HashSet<RecurringObserver> observerList = new HashSet<>();

    public void addRecurring(String concept, BigDecimal amount, String description, LocalDateTime initialDate,
            RecurrenceType recurrence) {
        RecurringMove reminder = new RecurringMove(concept, amount, description, initialDate, recurrence);
        allRecurrings.add(reminder);
        notifyObservers();
    }

    public void addRecurring(RecurringMove recMove) {
        allRecurrings.add(recMove);
        notifyObservers();
    }

    public void deleteRecurring(RecurringMove recMove) {
        if (allRecurrings.contains(recMove))
            allRecurrings.remove(recMove);
        notifyObservers();
    }

    public void editRecurring(RecurringMove oldRecMove, RecurringMove newRecMove) {
        // Eliminando al recordatorio que se editó y creando uno nuevo para preservar el
        // orden cronológico en el treeset
        if (!allRecurrings.contains(oldRecMove))
            return;
        deleteRecurring(oldRecMove);
        addRecurring(newRecMove);
        notifyObservers();

    }

    private void notifyObservers() {
        for (RecurringObserver observer : observerList) {
            observer.observeRecurrings(allRecurrings);
        }
    }

    public void addObserver(RecurringObserver observer) {
        observerList.add(observer);
    }

    public void removeObserver(RecurringObserver observer) {
        if (observerList.contains(observer))
            observerList.remove(observer);
    }

    public TreeSet<RecurringMove> getRecurrings() {
        return allRecurrings;
    }

    public void saveRecurrings() {
        RecurringJSONHandler.saveReminders(allRecurrings);
    }
}
