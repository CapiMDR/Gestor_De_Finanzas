package goals.goals_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * JavaFX controller for the Goals view.
 * Replaces {@code GoalsView.java} (Swing).
 *
 * Layout is defined declaratively in {@code /fxml/goals.fxml}.
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
    @FXML private VBox progressContainer;
    @FXML private Button btnAddGoal;
    @FXML private Button btnEditGoal;
    @FXML private Button btnDeleteGoal;
    @FXML private Button btnViewGoalDetails;

    @FXML
    public void initialize() {
        // Limit goal name to 30 characters
        txtGoalName.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 30) {
                return null;
            }
            return change;
        }));
    }

    // ── Public Accessors for Controller ──────────────────────────────────────

    public ListView<String> getListGoals() { return listGoals; }
    public TextField getTxtGoalName() { return txtGoalName; }
    public TextField getTxtTargetAmount() { return txtTargetAmount; }
    public TextField getTxtDescription() { return txtDescription; }
    public ProgressBar getProgressBarGoal() { return progressBarGoal; }
    public VBox getProgressContainer() { return progressContainer; }
    public Button getBtnAddGoal() { return btnAddGoal; }
    public Button getBtnEditGoal() { return btnEditGoal; }
    public Button getBtnDeleteGoal() { return btnDeleteGoal; }
    public Button getBtnViewGoalDetails() { return btnViewGoalDetails; }
}
