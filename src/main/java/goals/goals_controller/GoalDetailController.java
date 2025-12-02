package goals.goals_controller;

import goals.goals_model.Goal;
import goals.goals_view.GoalDetailView;

/**
 * Controller specifically for managing the Goal Detail View.
 * Handles the logic for displaying the progress of a specific goal.
 *
 * @author Jose Pablo Martinez
 */

public class GoalDetailController {

    private final GoalDetailView view;

    public GoalDetailController(GoalDetailView view) {
        this.view = view;
    }

    /**
     * Receives a request to show details for a goal and updates the view.
     * 
     * @param objGoal The goal object to visualize.
     */

    public void showDetails(Goal objGoal) {
        if (objGoal != null) {
            // Update UI Components
            view.showProgress(objGoal);

            if (!view.isVisible()) {
                view.setVisible(true);
            }
        }
    }
}