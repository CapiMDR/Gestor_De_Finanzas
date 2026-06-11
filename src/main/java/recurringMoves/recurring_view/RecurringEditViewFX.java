package recurringMoves.recurring_view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class RecurringEditViewFX {

    @FXML private TextField txtDescription;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private DatePicker datePicker;

    public TextField getTxtDescription() { return txtDescription; }
    public TextField getTxtAmount() { return txtAmount; }
    public ComboBox<String> getCmbCategory() { return cmbCategory; }
    public DatePicker getDatePicker() { return datePicker; }
}
