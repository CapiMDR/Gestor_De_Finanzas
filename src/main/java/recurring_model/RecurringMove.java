package recurring_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecurringMove {
    private String concept;
    private BigDecimal amount;
    private String description;
    private LocalDateTime initialDate;
    private RecurrenceType recurrence; //Diario/Mensual/Anual
    private boolean triggered = false; // Indica si el recordatorio ya fue disparado en esta ejecución

    public RecurringMove(String name, String message, LocalDateTime date) {
        this.concept = name;
        this.description = message;
        this.initialDate = date;
    }

    public RecurringMove(String name, String message) {
        this.concept = name;
        this.description = message;
        this.initialDate = LocalDateTime.now();
    }

    public String getName() {
        return concept;
    }

    public String getMessage() {
        return description;
    }

    public LocalDateTime getDate() {
        return initialDate;
    }

    public void setName(String name) {
        this.concept = name;
    }

    public void setMessage(String message) {
        this.description = message;
    }

    // Sin método setDate para asegurar que el orden correcto se mantenga en la
    // lista de recordatorios

    public void setTriggered(boolean t) {
        triggered = t;
    }

    public boolean shouldTrigger() {
        LocalDateTime now = LocalDateTime.now();
        return !triggered && initialDate.isBefore(now);
    }

    @Override
    public String toString() {
        return "Name: " + concept + " Message: " + description + " Date "
                + initialDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
