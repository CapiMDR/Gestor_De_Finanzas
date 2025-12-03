package movements.movement_model;

import java.util.List;

public interface CategoryObserver {
    void onNotify(List<MovementCategory> categories);
}
