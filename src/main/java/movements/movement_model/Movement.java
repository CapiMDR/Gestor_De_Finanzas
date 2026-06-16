package movements.movement_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import accounts.account_model.Account;

/**
 * Represents a financial movement associated with an account, either income or expense.
 * Contains information about description, amount, category, date and associated account.
 * @author Martín Jesús Pool Chuc
 */
public class Movement {

    private UUID idMovement;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private MovementCategory category;
    Account account;

    /**
     * Main constructor used when manually creating a movement from the system.
     * Automatically generates a new UUID and assigns the current date.
     *
     * @param idMovement not used, but kept for compatibility
     * @param description movement description
     * @param amount movement amount
     * @param category movement category
     * @param account account associated with the movement
     */
    public Movement(UUID idMovement, String description, BigDecimal amount, MovementCategory category, Account account) {
        this.idMovement = UUID.randomUUID();
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = LocalDateTime.now(java.time.ZoneId.systemDefault());
        this.account = account;
    }

    /**
     * Constructor used when loading a movement from a JSON file.
     *
     * @param idMovement unique identifier loaded from persistence
     * @param description movement description
     * @param amount movement amount
     * @param category movement category
     * @param account associated account
     * @param date exact date of the movement loaded from persistence
     */
    public Movement(UUID idMovement, String description, BigDecimal amount, MovementCategory category, Account account, LocalDateTime date) {
        this.idMovement = idMovement;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public MovementCategory getCategory() {
        return category;
    }

    public Account getAccount() {
        return account;
    }

    public UUID getIdMovement() {
        return idMovement;
    }

    public void setIdMovement(UUID idMovement) {
        this.idMovement = idMovement;
    }
}
