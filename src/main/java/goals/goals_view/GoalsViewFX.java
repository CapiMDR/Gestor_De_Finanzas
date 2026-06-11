package goals.goals_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the Goals view.
 * Replaces {@code GoalsView.java} (Swing).
 *
 * <p>Layout is defined declaratively in {@code /fxml/goals.fxml}.
 * Business logic is delegated to {@code GoalsController}.
 *
 * @see goals.goals_controller.GoalsController
 */
public class GoalsViewFX {

    @FXML private ListView<String> listGoals;
    @FXML private TextField txtGoalName;
    @FXML private TextField txtTargetAmount;
    @FXML private TextField txtDescription;
    @FXML private ProgressBar progressBarGoal;
    @FXML private Button btnAddGoal;
    @FXML private Button btnEditGoal;
    @FXML private Button btnDeleteGoal;

    // TODO: Implement — Phase 3.4 (Goals module migration)
}
