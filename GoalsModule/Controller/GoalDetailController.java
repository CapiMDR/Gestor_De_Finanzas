package GoalsModule.Controller;

import GoalsModule.Model.Goal;
import GoalsModule.View.GoalDetailView;

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
            view.setVisible(true);
            view.toFrontfront();
        }
    }
}