package reminder_model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reminder {
    private String name;
    private String message;
    private LocalDateTime date;

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

    @Override
    public String toString() {
        return "Name: " + name + " Message: " + message + " Date "
                + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
