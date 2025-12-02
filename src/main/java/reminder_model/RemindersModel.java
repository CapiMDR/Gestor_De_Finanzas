package reminder_model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Modelo encargado de administrar los recordatorios simples (no recurrentes).
 * <p>
 * Gestiona la creación, edición, eliminación y almacenamiento de objetos
 * {@link Reminder}, además de notificar a los observadores registrados cada vez
 * que ocurre un cambio en la colección.
 * </p>
 */
public class RemindersModel {

    // Manteniendo a los recordatorios en un TreeSet para rápido ordenamiento en
    // inserción/eliminación y eficiencia al buscar existencia
    /** Colección principal de recordatorios, ordenada por fecha y nombre. */
    private final TreeSet<Reminder> allReminders = ReminderJSONHandler.loadReminders();

    /**
     * Lista de observadores que serán notificados cuando cambien los recordatorios.
     */
    private final HashSet<ReminderObserver> observerList = new HashSet<>();

    /**
     * Agrega un nuevo recordatorio utilizando sus datos individuales.
     *
     * @param name    nombre identificador del recordatorio
     * @param message mensaje o descripción del recordatorio
     * @param date    fecha y hora del recordatorio
     */
    public void addReminder(String name, String message, LocalDateTime date) {
        Reminder reminder = new Reminder(name, message, date);
        allReminders.add(reminder);
        notifyObservers();
    }

    /**
     * Agrega un recordatorio existente a la colección.
     *
     * @param reminder recordatorio ya construido
     */
    public void addReminder(Reminder reminder) {
        allReminders.add(reminder);
        notifyObservers();
    }

    /**
     * Elimina un recordatorio específico si existe en el conjunto.
     *
     * @param reminder recordatorio a eliminar
     */
    public void deleteReminder(Reminder reminder) {
        if (allReminders.contains(reminder))
            allReminders.remove(reminder);
        notifyObservers();
    }

    /**
     * Edita un recordatorio sustituyéndolo por una nueva instancia.
     * <p>
     * Se elimina el recordatorio original y se agrega el nuevo para preservar
     * el orden cronológico dentro del {@link TreeSet}.
     * </p>
     *
     * @param oldReminder recordatorio que será sustituido
     * @param newReminder recordatorio actualizado
     */
    public void editReminder(Reminder oldReminder, Reminder newReminder) {
        // Eliminando al recordatorio que se editó y creando uno nuevo para preservar el
        // orden cronológico en el treeset
        if (!allReminders.contains(oldReminder))
            return;
        deleteReminder(oldReminder);
        addReminder(newReminder);
        notifyObservers();
    }

    /**
     * Notifica a todos los observadores que la colección de recordatorios ha
     * cambiado.
     */
    private void notifyObservers() {
        for (ReminderObserver observer : observerList) {
            observer.observeReminders(allReminders);
        }
    }

    /**
     * Registra un observador para que sea notificado ante cambios.
     *
     * @param observer objeto que implementa {@link ReminderObserver}
     */
    public void addObserver(ReminderObserver observer) {
        observerList.add(observer);
    }

    /**
     * Elimina un observador previamente registrado.
     *
     * @param observer el observador a remover
     */
    public void removeObserver(ReminderObserver observer) {
        if (observerList.contains(observer))
            observerList.remove(observer);
    }

    /**
     * Devuelve el conjunto ordenado de todos los recordatorios.
     *
     * @return {@link TreeSet} con los recordatorios
     */
    public TreeSet<Reminder> getReminders() {
        return allReminders;
    }

    /**
     * Guarda todos los recordatorios en almacenamiento persistente (archivo JSON).
     */
    public void saveReminders() {
        ReminderJSONHandler.saveReminders(allReminders);
    }
}
