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
    @FXML private Button btnEditRecurring;
    @FXML private Button btnDeleteRecurring;

    // ── Public Accessors for Controller ──────────────────────────────────────

    public ListView<String> getListRecurrings() { return listRecurrings; }
    public TextField getTxtDescription() { return txtDescription; }
    public TextField getTxtAmount() { return txtAmount; }
    public ComboBox<String> getCmbCategory() { return cmbCategory; }
    public DatePicker getDatePicker() { return datePicker; }
    public Button getBtnAddRecurring() { return btnAddRecurring; }
    public Button getBtnEditRecurring() { return btnEditRecurring; }
    public Button getBtnDeleteRecurring() { return btnDeleteRecurring; }
}
