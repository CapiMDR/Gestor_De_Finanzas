package movements.movement_model;

import java.util.List;
/**
 * Interface to observe category changes.
 */
public interface CategoryObserver {
    public void onNotify(List<MovementCategory> categories);
}
