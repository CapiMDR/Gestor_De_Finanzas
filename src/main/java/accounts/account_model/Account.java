package accounts.account_model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import goals.goals_model.Goal;
import movements.movement_model.Movement;

/**
 * Representa una cuenta financiera que mantiene información sobre su saldo,
 * tipo, moneda, movimientos y metas asociadas.
 */
public class Account {

    /**
     * Tipos de cuenta disponibles.
     */
    public enum AccountType {
        /** Cuenta de efectivo. */
        CASH,
        /** Cuenta digital. */
        DIGITAL
    }

    /**
     * Tipos de moneda soportados.
     */
    public enum Coin {
        /** Dólar estadounidense. */
        USD,
        /** Peso mexicano. */
        MXN
    }

    private int id;
    private String name;
    private AccountType type;
    private Coin coin;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;

    // Lists
    private List<Movement> movements;
    private List<Goal> goals;

    /**
     * Crea una nueva cuenta con los datos especificados.
     *
     * @param id             identificador de la cuenta
     * @param name           nombre de la cuenta
     * @param type           tipo de cuenta (CASH o DIGITAL)
     * @param coin           tipo de moneda (USD o MXN)
     * @param initialBalance saldo inicial de la cuenta
     */
    public Account(int id, String name, AccountType type, Coin coin, BigDecimal initialBalance) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coin = coin;
        this.initialBalance = initialBalance;
        this.currentBalance = initialBalance;
        this.movements = new ArrayList<>();
        this.goals = new ArrayList<>(); // Initialize goals
    }

    /**
     * Actualiza el saldo actual de la cuenta.
     *
     * @param currentBalance nuevo saldo actual
     */
    public void updateBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * Agrega un movimiento y actualiza el saldo según si es ingreso o gasto.
     *
     * @param movement movimiento a registrar en la cuenta
     */
    public void addMovement(Movement movement) {
        this.movements.add(movement);

        switch (movement.getCategory().getType()) {
            case INCOME:
                updateBalance(getCurrentBalance().add(movement.getAmount()));
                break;

            case EXPENSE:
                updateBalance(getCurrentBalance().subtract(movement.getAmount()));
                break;
            default:
                break;
        }
    }

    /**
     * Obtiene la lista de movimientos registrados.
     *
     * @return lista de movimientos
     */
    public List<Movement> getMovements() {
        return movements;
    }

    public List<Goal> getGoals() {
        return goals;
    }
    
    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    /**
     * Obtiene el tipo de moneda.
     *
     * @return moneda de la cuenta
     */
    public Coin getCoin() {
        return coin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    /**
     * Establece una nueva lista de movimientos.
     *
     * @param movements lista de movimientos
     */
    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }
}