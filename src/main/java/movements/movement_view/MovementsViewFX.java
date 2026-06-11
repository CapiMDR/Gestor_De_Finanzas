package movements.movement_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the Movements view.
 * Replaces {@code MovementManagerView.java} (Swing).
 *
 * <p>Layout is defined declaratively in {@code /fxml/movements.fxml}.
 * Business logic is delegated to {@code MovementController}.
 *
 * @see movements.movement_controller.MovementController
 */
public class MovementsViewFX {

    @FXML private ListView<String> listMovements;
    @FXML private TextField txtDescription;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private Button btnAddMovement;
    @FXML private Button btnDeleteMovement;

    // TODO: Implement — Phase 3.4 (Movements module migration)
}
