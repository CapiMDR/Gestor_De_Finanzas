package goals.goals_view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * JavaFX View Controller for the Goal Detail dialog.
 * Handles pure UI components and provides accessors for the actual Controller.
 */
public class GoalDetailViewFX {

    @FXML private Label lblName;
    @FXML private Label lblDescription;
    @FXML private Label lblTargetAmount;
    @FXML private Label lblCurrentAmount;
    @FXML private ProgressBar progressBar;
    @FXML private Label lblRemainingAmount;

    // ── Getters for Controller to read/write data ──────────────────────────

    public Label getLblName() { return lblName; }
    public Label getLblDescription() { return lblDescription; }
    public Label getLblTargetAmount() { return lblTargetAmount; }
    public Label getLblCurrentAmount() { return lblCurrentAmount; }
    public ProgressBar getProgressBar() { return progressBar; }
    public Label getLblRemainingAmount() { return lblRemainingAmount; }
}
