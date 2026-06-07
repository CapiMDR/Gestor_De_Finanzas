package goals.goals_controller;

import goals.goals_model.Goal;
import goals.goals_view.GoalDetailView;

/**
 * Controller specifically in charge of managing the Detail View of a Goal.
 * Handles the logic to show the progress of a specific goal.
 * 
 * @author Jose Pablo Martinez
 */

public class GoalDetailController {

    private final GoalDetailView view;

    public GoalDetailController(GoalDetailView view) {
        this.view = view;
    }

    /**
     * Receives a request to show the details of a goal and updates the view.
     * 
     * @param objGoal The Goal object to view.
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