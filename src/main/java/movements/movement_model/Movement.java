package movements.movement_model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import accounts.account_model.Account;

/**
 * Representa un movimiento financiero asociado a una cuenta, ya sea ingreso o gasto.
 * Contiene información sobre descripción, monto, categoría, fecha y cuenta asociada.
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
     * Constructor principal utilizado al crear un movimiento manualmente desde el sistema.
     * Genera automáticamente un nuevo UUID y asigna la fecha actual.
     *
     * @param idMovement no se utiliza, pero se mantiene por compatibilidad
     * @param description descripción del movimiento
     * @param amount monto del movimiento
     * @param category categoría del movimiento
     * @param account cuenta asociada al movimiento
     */
    public Movement(UUID idMovement, String description, BigDecimal amount, MovementCategory category, Account account) {
        this.idMovement = UUID.randomUUID();
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = LocalDateTime.now();
        this.account = account;
    }

    /**
     * Constructor utilizado al cargar un movimiento desde un archivo JSON.
     *
     * @param idMovement identificador único cargado desde persistencia
     * @param description descripción del movimiento
     * @param amount monto del movimiento
     * @param category categoría del movimiento
     * @param account cuenta asociada
     * @param date fecha exacta del movimiento cargada desde persistencia
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
