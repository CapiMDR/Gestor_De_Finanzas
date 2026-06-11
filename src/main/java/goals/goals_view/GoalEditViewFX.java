package goals.goals_view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * JavaFX View Controller for the Goal Edit dialog.
 * Handles pure UI components and provides accessors for the actual Controller.
 */
public class GoalEditViewFX {

    @FXML private TextField txtGoalName;
    @FXML private TextField txtTargetAmount;
    @FXML private TextField txtDescription;

    // ── Getters for Controller to read/write data ──────────────────────────

    public TextField getTxtGoalName() { return txtGoalName; }
    public TextField getTxtTargetAmount() { return txtTargetAmount; }
    public TextField getTxtDescription() { return txtDescription; }
}
