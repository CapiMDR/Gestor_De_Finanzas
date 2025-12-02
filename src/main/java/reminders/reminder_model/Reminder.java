package reminders.reminder_model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un recordatorio con un nombre, mensaje y fecha programada.
 * 
 * <p>
 * La clase permite verificar si el recordatorio debe activarse según su
 * fecha programada y si ya fue disparado durante la ejecución actual.
 * </p>
 */
public class Reminder {

    /** Nombre del recordatorio. */
    private String name;

    /** Mensaje asociado al recordatorio. */
    private String message;

    /** Fecha y hora en la que debe dispararse el recordatorio. */
    private LocalDateTime date;

    /**
     * Indica si el recordatorio ya fue disparado en esta ejecución.
     * Se utiliza para evitar que un recordatorio atrasado se dispare múltiples
     * veces.
     */
    private boolean triggered = false;

    /**
     * Constructor principal del recordatorio.
     * 
     * @param name    Nombre del recordatorio.
     * @param message Mensaje asociado al recordatorio.
     * @param date    Fecha y hora en la que debe dispararse.
     */
    public Reminder(String name, String message, LocalDateTime date) {
        this.name = name;
        this.message = message;
        this.date = date;
    }

    /**
     * Constructor alternativo que asigna la fecha actual como fecha del
     * recordatorio.
     * 
     * @param name    Nombre del recordatorio.
     * @param message Mensaje asociado al recordatorio.
     */
    public Reminder(String name, String message) {
        this.name = name;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    /**
     * Obtiene el nombre del recordatorio.
     * 
     * @return Nombre del recordatorio.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el mensaje del recordatorio.
     * 
     * @return Mensaje del recordatorio.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtiene la fecha programada del recordatorio.
     * 
     * @return Fecha en formato {@link LocalDateTime}.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Cambia el nombre del recordatorio.
     * 
     * @param name Nuevo nombre del recordatorio.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Cambia el mensaje del recordatorio.
     * 
     * @param message Nuevo mensaje del recordatorio.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Establece si el recordatorio ya ha sido disparado.
     * 
     * @param t {@code true} si ya fue disparado, de lo contrario {@code false}.
     */
    public void setTriggered(boolean t) {
        triggered = t;
    }

    /**
     * Determina si el recordatorio debe dispararse.
     * 
     * <p>
     * Un recordatorio debe dispararse si:
     * </p>
     * <ul>
     * <li>No ha sido disparado aún en esta ejecución.</li>
     * <li>La fecha programada es anterior al momento actual.</li>
     * </ul>
     * 
     * @return {@code true} si debe dispararse, de lo contrario {@code false}.
     */
    public boolean shouldTrigger() {
        LocalDateTime now = LocalDateTime.now();
        return !triggered && date.isBefore(now);
    }

    /**
     * Devuelve una representación en texto del recordatorio.
     * 
     * @return Cadena descriptiva del recordatorio.
     */
    @Override
    public String toString() {
        return "Name: " + name + " Message: " + message + " Date "
                + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
