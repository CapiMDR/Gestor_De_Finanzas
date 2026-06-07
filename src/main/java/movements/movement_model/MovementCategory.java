package movements.movement_model;

/**
 * Represents a financial movement category, used to classify
 * income and expenses within the system.
 * @author Martín Jesús Pool Chuc
 */
public class MovementCategory {

    /**
     * Possible movement types: INCOME or EXPENSE.
     */
    public enum MovementType {
        INCOME, EXPENSE
    }
    
    private String name;
    private MovementType type;

    /**
     * Creates a new movement category.
     *
     * @param name category name
     * @param type movement type (INCOME or EXPENSE)
     */
    public MovementCategory(String name, MovementType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public MovementType getType() {
        return type;
    }
}