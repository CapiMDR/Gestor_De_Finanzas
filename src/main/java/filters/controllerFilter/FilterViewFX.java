package filters.controllerFilter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

/**
 * JavaFX controller for the Filter view.
 * Replaces {@code CategoriesView.java} (Swing).
 *
 * <p>Layout is defined declaratively in {@code /fxml/filter.fxml}.
 * Uses the native JavaFX {@link DatePicker} for date range selection.
 * Business logic is delegated to {@code FilterController}.
 *
 * @see filters.controllerFilter.FilterController
 */
public class FilterViewFX {

    @FXML private ComboBox<String> cmbCategory;
    @FXML private DatePicker datePickerFrom;
    @FXML private DatePicker datePickerTo;
    @FXML private ListView<String> listFilteredMovements;
    @FXML private Button btnApplyFilter;
    @FXML private Button btnClearFilter;

    // TODO: Implement — Phase 3.4 (Filter module migration)
}
