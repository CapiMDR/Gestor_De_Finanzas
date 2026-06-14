package recurrings.recurring_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import movements.movement_model.MovementCategory;

/**
 * Represents a recurring movement (a payment or action that must be repeated
 * with a certain frequency). Each instance contains the necessary information
 * to determine when it should trigger and generate the next occurrence.
 */
public class RecurringMove {

    /** Concept or name of the recurring movement. */
    private String concept;

    /** Amount associated with the movement. */
    private BigDecimal amount;

    /** Additional description of the movement. */
    private String description;

    /**
     * Date of the first occurrence of the recurring movement.
     * This date determines the base from which the next ones are calculated.
     */
    private final LocalDateTime initialDate; // Date of the first occurrence of the recurring movement

    /**
     * Frequency with which the movement repeats (daily, monthly, yearly, etc.).
     */
    private RecurrenceType recurrence; // Diario/Mensual/Anual

    /**
     * Whether the recurring payment has already been notified to the user in this execution.
     */
    private boolean hasTriggered = false;

    private MovementCategory category;

    /**
     * Creates a new recurring movement using the initial date as the moment
     * of the first activation and establishing the corresponding frequency.
     *
     * @param concept     name of the recurring movement
     * @param amount      associated amount
     * @param description description of the movement
     * @param initialDate date of the first activation of the movement
     * @param recurrence  type of recurrence (daily, weekly, monthly, yearly, etc.)
     */
    public RecurringMove(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence, MovementCategory type) {

        this.concept = concept;
        this.amount = amount;
        this.description = description;
        this.initialDate = initialDate;
        this.recurrence = recurrence;
        this.category = type;
    }

    /** @return the movement concept */
    public String getConcept() {
        return concept;
    }

    /** @return the movement amount */
    public BigDecimal getAmount() {
        return amount;
    }

    /** @return the description of the movement */
    public String getDescription() {
        return description;
    }

    /** @return the date of the first occurrence of the movement */
    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    /** @return the movement recurrence */
    public RecurrenceType getRecurrence() {
        return recurrence;
    }

    /** @param concept new concept */
    public void setConcept(String concept) {
        this.concept = concept;
    }

    /** @param amount new amount */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /** @param description new description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @param recurrence new recurrence type */
    public void setRecurrence(RecurrenceType recurrence) {
        this.recurrence = recurrence;
    }

    /**
     * Indicates whether the movement should activate at this moment.
     * The movement should trigger when its initial date has already passed or is equal to
     * the current moment.
     *
     * @return true if it should trigger, false otherwise
     */
    public boolean shouldTrigger() {
        LocalDateTime now = LocalDateTime.now();
        return !initialDate.isAfter(now) && !hasTriggered;
    }

    public void setTriggered(boolean state) {
        hasTriggered = state;
    }

    public MovementCategory getCategory() {
        return category;
    }

    /**
     * Creates a new occurrence of the recurring movement adding the frequency
     * corresponding to the initial date and returning a new instance.
     *
     * @return a new {@link RecurringMove} with the next scheduled date,
     *         or null if it cannot be calculated
     */
    public RecurringMove createNextOccurrence() {
        LocalDateTime nextDate = computeNextDate(initialDate, recurrence);
        if (nextDate == null)
            return null;

        return new RecurringMove(
                concept,
                amount,
                description,
                nextDate,
                recurrence,
                category);
    }

    /**
     * Returns the next date when the payment notification will be sent
     * according to the configured frequency.
     *
     * @param t    base date from which to calculate the next one
     * @param type recurrence type
     * @return the next date according to the specified recurrence
     */
    private LocalDateTime computeNextDate(LocalDateTime t, RecurrenceType type) {
        return switch (type) {
            case Diario -> t.plusDays(1);
            case Semanal -> t.plusWeeks(1);
            case Quincenal -> t.plusWeeks(2);
            case Mensual -> t.plusMonths(1);
            case Anual -> t.plusYears(1);
            default -> t;
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
