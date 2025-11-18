package com.example.GoalsModule.Model;

import java.util.List;

//Asume that exists in MovementsModule
import com.example.MovementsModule.Model.Movement;
import com.example.MovementsModule.Model.MovementCategory;

/**
 * Interface defining the contract for the Observer pattern.
 * Classes that implement this interface will be notified when the 
 * MovementManager updates its state (movements or categories).
 *
 * @author Jose Pablo Martinez
 */

public interface MovementObserver {

    /**
     * Triggered when the observable subject notifies a change.
     *
     * @param movements  The updated list of all movements.
     * @param categories The updated list of movement categories.
     */
    
    void onNotify(List<Movement> movements, List<MovementCategory> categories);
}