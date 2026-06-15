package recurrings.recurring_view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class RecurringAlertViewFX {

    @FXML private Label lblConcept;
    @FXML private Label lblAmount;
    @FXML private ComboBox<String> cmbAccounts;

    public Label getLblConcept() { return lblConcept; }
    public Label getLblAmount() { return lblAmount; }
    public ComboBox<String> getCmbAccounts() { return cmbAccounts; }
}
