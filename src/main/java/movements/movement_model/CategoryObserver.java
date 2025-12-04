package movements.movement_model;

import java.util.List;

public interface CategoryObserver {
    public void onNotify(List<MovementCategory> categories);
}
