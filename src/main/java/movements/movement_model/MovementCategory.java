package movements.movement_model;

public class MovementCategory {
    public enum MovementType {
        INCOME, EXPENSE
    }

    private String name;
    private MovementType type;

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

    @Override
    public String toString() {
        return "MovementCategory [name=" + name + ", type=" + type + "]";
    }
}
