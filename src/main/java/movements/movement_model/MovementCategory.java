package movements.movement_model;

/**
 * Representa una categoría de movimiento financiero, utilizada para clasificar
 * ingresos y gastos dentro del sistema.
 * @author Martín Jesús Pool Chuc
 */
public class MovementCategory {

    /**
     * Tipos posibles de movimiento: INCOME (ingreso) o EXPENSE (gasto).
     */
    public enum MovementType {
        INCOME, EXPENSE
    }
    
    private String name;
    private MovementType type;

    /**
     * Crea una nueva categoría de movimiento.
     *
     * @param name nombre de la categoría
     * @param type tipo de movimiento (INCOME o EXPENSE)
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