package reminder_model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reminder {
    private String name;
    private String message;
    private LocalDateTime date;
    private boolean triggered = false; // Indica si el recordatorio ya fue disparado en esta ejecución

    public Reminder(String name, String message, LocalDateTime date) {
        this.name = name;
        this.message = message;
        this.date = date;
    }

    public Reminder(String name, String message) {
        this.name = name;
        this.message = message;
        this.date = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Sin método setDate para asegurar que el orden correcto se mantenga en la
    // lista de recordatorios

    public void setTriggered(boolean t) {
        triggered = t;
    }

    public boolean shouldTrigger() {
        LocalDateTime now = LocalDateTime.now();
        return !triggered && date.isBefore(now);
    }

    @Override
    public String toString() {
        return "Name: " + name + " Message: " + message + " Date "
                + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
