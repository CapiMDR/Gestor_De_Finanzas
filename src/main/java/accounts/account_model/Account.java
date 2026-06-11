package accounts.account_model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import goals.goals_model.Goal;
import movements.movement_model.Movement;

/**
 * Represents a financial account that maintains information about its balance,
 * type, currency, movements and associated goals.
 *
 * @author Martín Jesús Pool Chuc
 */
public class Account {

    /**
     * Available account types.
     */
    public enum AccountType {
        /** Fisical account. */
        EFECTIVO,
        /** Digital account. */
        DIGITAL
    }

    /**
     * Supported currency types.
     */
    public enum Coin {
        /** US Dollar. */
        USD,
        /** Mexican peso. */
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
     * Creates a new account with the specified data.
     *
     * @param id             account identifier
     * @param name           account name
     * @param type           account type (EFECTIVO or DIGITAL)
     * @param coin           currency type (USD or MXN)
     * @param initialBalance initial account balance
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
     * Updates the current balance of the account.
     *
     * @param currentBalance new current balance
     */
    public void updateBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * Adds a movement and updates the balance depending on whether it is income or expense.
     *
     * @param movement movement to register in the account
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
     * Gets the list of registered movements.
     *
     * @return list of movements
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
     * Gets the currency type.
     *
     * @return account currency
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
     * Sets a new list of movements.
     *
     * @param movements list of movements
     */
    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }
}