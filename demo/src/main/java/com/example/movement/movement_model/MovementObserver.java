package com.example.movement.movement_model;

import java.util.List;

public interface MovementObserver {
    void onNotify(List<MovementCategory> categories);
}
