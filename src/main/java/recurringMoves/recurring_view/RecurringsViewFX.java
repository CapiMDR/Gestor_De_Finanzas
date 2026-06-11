package recurringMoves.recurring_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the Recurring Movements view.
 * Replaces {@code RecurringsView.java} (Swing).
 *
 * <p>Layout is defined declaratively in {@code /fxml/recurrings.fxml}.
 * Uses the native JavaFX {@link DatePicker}, replacing the legacy {@code JCalendar} dependency.
 * Business logic is delegated to {@code RecurringsController}.
 *
 * @see recurringMoves.recurring_controller.RecurringsController
 */
public class RecurringsViewFX {

    @FXML private ListView<String> listRecurrings;
    @FXML private TextField txtDescription;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private DatePicker datePicker;
    @FXML private Button btnAddRecurring;
    @FXML private Button btnDeleteRecurring;

    // TODO: Implement — Phase 3.4 (Recurring movements module migration)
}
