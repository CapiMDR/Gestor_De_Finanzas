package recurringMoves.recurring_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Modelo encargado de gestionar los recordatorios recurrentes.
 * Mantiene un TreeSet para garantizar ordenamiento cronológico eficiente
 * en inserción, eliminación y búsqueda.
 */
public class RecurringsModel {

    /**
     * Manteniendo a los recordatorios en un TreeSet para rápido ordenamiento en
     * inserción/eliminación y eficiencia al buscar existencia.
     * 
     * Cargados desde JSON al iniciar.
     */
    private final TreeSet<RecurringMove> allRecurrings = RecurringJSONHandler.loadReminders();

    /** Lista de observadores suscritos a los cambios del modelo. */
    private final HashSet<RecurringObserver> observerList = new HashSet<>();

    /**
     * Agrega un nuevo recordatorio recurrente al modelo.
     *
     * @param concept     Concepto o título del movimiento recurrente.
     * @param amount      Monto asociado.
     * @param description Descripción del movimiento.
     * @param initialDate Fecha y hora inicial del recordatorio.
     * @param recurrence  Tipo de recurrencia (diaria, semanal, mensual, etc.)
     */
    public void addRecurring(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence) {

        RecurringMove reminder = new RecurringMove(concept, amount, description, initialDate, recurrence);
        allRecurrings.add(reminder);
        notifyObservers();
    }

    /**
     * Agrega un objeto RecurringMove completo al modelo.
     *
     * @param recMove Recordatorio recurrente.
     */
    public void addRecurring(RecurringMove recMove) {
        allRecurrings.add(recMove);
        notifyObservers();
    }

    /**
     * Elimina un recordatorio recurrente si existe dentro del modelo.
     *
     * @param recMove Recordatorio recurrente a eliminar.
     */
    public void deleteRecurring(RecurringMove recMove) {
        if (allRecurrings.contains(recMove))
            allRecurrings.remove(recMove);
        notifyObservers();
    }

    /**
     * Edita un recordatorio existente reemplazándolo por uno nuevo.
     * 
     * Se elimina el anterior y se agrega el nuevo para asegurar
     * el orden cronológico dentro del TreeSet.
     *
     * @param oldRecMove Recordatorio previo.
     * @param newRecMove Nuevo recordatorio actualizado.
     */
    public void editRecurring(RecurringMove oldRecMove, RecurringMove newRecMove) {
        // Eliminando al recordatorio que se editó y creando uno nuevo para preservar el
        // orden cronológico en el treeset
        if (!allRecurrings.contains(oldRecMove))
            return;
        deleteRecurring(oldRecMove);
        addRecurring(newRecMove);
        notifyObservers();
    }

    /**
     * Notifica a todos los observadores registrados, enviando la lista completa
     * de recordatorios.
     */
    private void notifyObservers() {
        for (RecurringObserver observer : observerList) {
            observer.observeRecurrings(allRecurrings);
        }
    }

    /**
     * Registra un nuevo observador.
     *
     * @param observer Observador a agregar.
     */
    public void addObserver(RecurringObserver observer) {
        observerList.add(observer);
    }

    /**
     * Elimina un observador existente del modelo.
     *
     * @param observer Observador a remover.
     */
    public void removeObserver(RecurringObserver observer) {
        if (observerList.contains(observer))
            observerList.remove(observer);
    }

    /**
     * Devuelve el conjunto completo de recordatorios recurrentes.
     *
     * @return TreeSet con todos los RecurringMove.
     */
    public TreeSet<RecurringMove> getRecurrings() {
        return allRecurrings;
    }

    /**
     * Guarda todos los recordatorios recurrentes en el archivo JSON.
     */
    public void saveRecurrings() {
        RecurringJSONHandler.saveReminders(allRecurrings);
    }
}
