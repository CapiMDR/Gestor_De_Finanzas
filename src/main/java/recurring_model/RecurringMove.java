package recurring_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecurringMove {

    private String concept;
    private BigDecimal amount;
    private String description;
    private final LocalDateTime initialDate; // Fecha de la primera ocurrencia del movimiento recurrente
    private RecurrenceType recurrence; // Diario/Mensual/Anual

    public RecurringMove(String concept, BigDecimal amount, String description,
                         LocalDateTime initialDate, RecurrenceType recurrence) {

        this.concept = concept;
        this.amount = amount;
        this.description = description;

        this.initialDate = initialDate;
        this.recurrence = recurrence;
    }

    public String getConcept() { return concept; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getInitialDate() { return initialDate; }
    public RecurrenceType getRecurrence() { return recurrence; }

    public void setConcept(String concept) { this.concept = concept; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setRecurrence(RecurrenceType recurrence) { this.recurrence = recurrence; }

    public boolean shouldTrigger() {
        LocalDateTime now = LocalDateTime.now();
        return !initialDate.isAfter(now);
    }


    public RecurringMove createNextOccurrence() {
        LocalDateTime nextDate = computeNextDate(initialDate, recurrence);
        if (nextDate == null) return null;

        return new RecurringMove(
                concept,
                amount,
                description,
                nextDate,
                recurrence
        );
    }

    //Devuelve la siguiente ocasión en la que se enviará la notificación del pago según la frecuencia
    private LocalDateTime computeNextDate(LocalDateTime t, RecurrenceType type) {
        return switch (type) {
            case Diario     -> t.plusDays(1);
            case Semanal    -> t.plusWeeks(1);
            case Quincenal  -> t.plusWeeks(2);
            case Mensual    -> t.plusMonths(1);
            case Anual      -> t.plusYears(1);
            default         -> t;
        };
    }

    @Override
    public String toString() {
        return "RecurringMove{" +
                "concept='" + concept + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", initialDate=" + initialDate +
                ", recurrence=" + recurrence +
                '}';
    }
}