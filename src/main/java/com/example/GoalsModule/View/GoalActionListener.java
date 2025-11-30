package com.example.GoalsModule.View;

import com.example.GoalsModule.Model.Goal;

/**
 * Interface to handle actions triggered from specific Goal Cards.
 * This allows the Controller to know WHICH goal needs to be edited, deleted, or viewed.
 *
 * @author Jose Pablo Martinez
 */

public interface GoalActionListener {
    void onViewDetails(Goal goal);
    void onEdit(Goal goal);
    void onDelete(Goal goal);
}