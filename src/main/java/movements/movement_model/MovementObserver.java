package movements.movement_model;

import java.util.List;

public interface MovementObserver {
    void onNotify(List<MovementCategory> categories);
}
