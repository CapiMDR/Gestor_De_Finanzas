package com.example.GoalsModule.Controller;

import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalDetailView;

/**
 * Manages the navigation and data presentation for the Goal Detail View.
 * * @author Jose Pablo Martinez
 */

public class GoalDetailController {

    private final GoalDetailView view;


    public GoalDetailController(GoalDetailView view) {
        this.view = view;
    }

    /**
     * Configures the view to display the progress of a specific goal and makes it visible.
     * @param objGoal The goal to be visualized.
     */

    public void showDetails(Goal objGoal) {
        if (objGoal != null) {
            //Update UI components
            view.showProgress(objGoal);
        }
    }
}