package recurringMoves.recurring_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

/**
 * Representa un movimiento recurrente (un pago o acción que debe repetirse
 * con una cierta frecuencia). Cada instancia contiene la información necesaria
 * para determinar cuándo debe activarse y generar la siguiente ocurrencia.
 */
public class RecurringMove {

    /** Concepto o nombre del movimiento recurrente. */
    private String concept;

    /** Monto asociado con el movimiento. */
    private BigDecimal amount;

    /** Descripción adicional del movimiento. */
    private String description;

    /**
     * Fecha de la primera ocurrencia del movimiento recurrente.
     * Esta fecha determina la base desde la cual se calculan las siguientes.
     */
    private final LocalDateTime initialDate; // Fecha de la primera ocurrencia del movimiento recurrente

    /**
     * Frecuencia con la que se repite el movimiento (diario, mensual, anual, etc.).
     */
    private RecurrenceType recurrence; // Diario/Mensual/Anual

    /**
     * Si el pago recurrente ya se ha notificado al usuario en esta ejecución
     */
    private boolean hasTriggered = false;

    private MovementCategory category;

    /**
     * Crea un nuevo movimiento recurrente usando la fecha inicial como momento
     * de la primera activación y estableciendo la frecuencia correspondiente.
     *
     * @param concept     nombre del movimiento recurrente
     * @param amount      monto asociado
     * @param description descripción del movimiento
     * @param initialDate fecha de la primera activación del movimiento
     * @param recurrence  tipo de recurrencia (diario, semanal, mensual, anual,
     *                    etc.)
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

    /** @return el concepto del movimiento */
    public String getConcept() {
        return concept;
    }

    /** @return el monto del movimiento */
    public BigDecimal getAmount() {
        return amount;
    }

    /** @return la descripción del movimiento */
    public String getDescription() {
        return description;
    }

    /** @return la fecha de la primera ocurrencia del movimiento */
    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    /** @return la recurrencia del movimiento */
    public RecurrenceType getRecurrence() {
        return recurrence;
    }

    /** @param concept nuevo concepto */
    public void setConcept(String concept) {
        this.concept = concept;
    }

    /** @param amount nuevo monto */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /** @param description nueva descripción */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @param recurrence nuevo tipo de recurrencia */
    public void setRecurrence(RecurrenceType recurrence) {
        this.recurrence = recurrence;
    }

    /**
     * Indica si el movimiento debe activarse en este momento.
     * El movimiento debe dispararse cuando su fecha inicial ya pasó o es igual al
     * momento actual.
     *
     * @return true si debe dispararse, false de lo contrario
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
     * Crea una nueva ocurrencia del movimiento recurrente sumando la frecuencia
     * correspondiente a la fecha inicial y devolviendo una nueva instancia.
     *
     * @return un nuevo {@link RecurringMove} con la siguiente fecha programada,
     *         o null si no se puede calcular
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
     * Devuelve la siguiente fecha en la que se enviará la notificación del pago
     * según la frecuencia configurada.
     *
     * @param t    fecha base desde la cual calcular la siguiente
     * @param type tipo de recurrencia
     * @return la fecha siguiente según la recurrencia especificada
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
