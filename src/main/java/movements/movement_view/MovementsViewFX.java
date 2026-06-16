package movements.movement_view;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for the Movements Management view.
 * Replaces {@code MovementManagerView.java} (Swing).
 *
 * <p>Manages both income (INCOME) and expense (EXPENSE) form sections.
 * Business logic is fully delegated to {@link MovementController}.
 *
 * <p>Uses {@link DatePicker} (JavaFX native) instead of the {@code JDateChooser}
 * Swing component that was used before.
 *
 * @see movements.movement_controller.MovementController
 */
public class MovementsViewFX {

    // ── Header ────────────────────────────────────────────────────────────────
    @FXML private Label lblAccountName;

    // ── Income form ───────────────────────────────────────────────────────────
    @FXML private TextField txtAccountIncome;
    @FXML private TextField txtAmountIncome;
    @FXML private ListView<String> listCategoriesIncome;
    @FXML private DatePicker datePickerIncome;
    @FXML private TextArea txtDescriptionIncome;
    @FXML private Button btnAddIncome;
    @FXML private Button btnAddCategoryIncome;
    @FXML private Button btnFilter;
    @FXML private Button btnVolver;

    private Runnable onBack;

    // ── Expense form ──────────────────────────────────────────────────────────
    @FXML private TextField txtAccountExpense;
    @FXML private TextField txtAmountExpense;
    @FXML private ListView<String> listCategoriesExpense;
    @FXML private DatePicker datePickerExpense;
    @FXML private TextArea txtDescriptionExpense;
    @FXML private Button btnAddExpense;
    @FXML private Button btnAddCategoryExpense;

    /**
     * Called automatically by JavaFX after all {@code @FXML} fields are injected.
     * Defaults both date pickers to today.
     */
    @FXML
    public void initialize() {
        datePickerIncome.setValue(LocalDate.now(java.time.ZoneId.systemDefault()));
        datePickerExpense.setValue(LocalDate.now(java.time.ZoneId.systemDefault()));
        if (btnVolver != null) {
            btnVolver.setOnAction(e -> {
                if (onBack != null) onBack.run();
            });
        }
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    public void setAccountName(String accountName) {
        Platform.runLater(() -> {
            if (lblAccountName != null) {
                lblAccountName.setText(accountName);
            }
            txtAccountIncome.setText(accountName);
            txtAccountExpense.setText(accountName);
        });
    }

    // ── Public methods used by MovementController ─────────────────────────────

    /**
     * Updates the income category list shown in the view.
     *
     * @param categories list of categories with type INCOME
     */
    public void updateIncomeCategories(List<String> categories) {
        Platform.runLater(() ->
            listCategoriesIncome.setItems(FXCollections.observableArrayList(categories)));
    }

    /**
     * Updates the expense category list shown in the view.
     *
     * @param categories list of categories with type EXPENSE
     */
    public void updateExpenseCategories(List<String> categories) {
        Platform.runLater(() ->
            listCategoriesExpense.setItems(FXCollections.observableArrayList(categories)));
    }

    /**
     * Clears the income form fields after a successful addition.
     * Mirrors {@code MovementManagerView#clearIncomeFields()}.
     */
    public void clearIncomeFields() {
        Platform.runLater(() -> {
            txtAmountIncome.clear();
            txtDescriptionIncome.clear();
            listCategoriesIncome.getSelectionModel().clearSelection();
            datePickerIncome.setValue(LocalDate.now(java.time.ZoneId.systemDefault()));
        });
    }

    /**
     * Clears the expense form fields after a successful addition.
     * Mirrors {@code MovementManagerView#clearExpenseFields()}.
     */
    public void clearExpenseFields() {
        Platform.runLater(() -> {
            txtAmountExpense.clear();
            txtDescriptionExpense.clear();
            listCategoriesExpense.getSelectionModel().clearSelection();
            datePickerExpense.setValue(LocalDate.now(java.time.ZoneId.systemDefault()));
        });
    }

    // ── Form data accessors (read by MovementController) ──────────────────────

    /** @return description text from the income form */
    public String getDescriptionIncome()  { return txtDescriptionIncome.getText().trim(); }

    /** @return raw amount text from the income form */
    public String getAmountIncomeText()   { return txtAmountIncome.getText().trim(); }

    /** @return selected category name from the income list, or {@code null} */
    public String getSelectedCategoryIncome() {
        return listCategoriesIncome.getSelectionModel().getSelectedItem();
    }

    /**
     * Returns the income date as a {@link LocalDateTime} (start of selected day).
     *
     * @return selected date at midnight, or {@code null} if no date is selected
     */
    public LocalDateTime getIncomeDateAsLocalDateTime() {
        LocalDate date = datePickerIncome.getValue();
        return date != null ? date.atStartOfDay() : null;
    }

    /** @return description text from the expense form */
    public String getDescriptionExpense() { return txtDescriptionExpense.getText().trim(); }

    /** @return raw amount text from the expense form */
    public String getAmountExpenseText()  { return txtAmountExpense.getText().trim(); }

    /** @return selected category name from the expense list, or {@code null} */
    public String getSelectedCategoryExpense() {
        return listCategoriesExpense.getSelectionModel().getSelectedItem();
    }

    /**
     * Returns the expense date as a {@link LocalDateTime} (start of selected day).
     *
     * @return selected date at midnight, or {@code null} if no date is selected
     */
    public LocalDateTime getExpenseDateAsLocalDateTime() {
        LocalDate date = datePickerExpense.getValue();
        return date != null ? date.atStartOfDay() : null;
    }

    // ── Button getters (used by MovementController#AssignEvents) ──────────────
    // MovementController wires event handlers via setOnAction() to preserve the
    // original Observer architecture where the controller owns all business logic.

    /** @return the "Agregar Ingreso" button */
    public Button getBtnAddIncome()         { return btnAddIncome; }

    /** @return the "Agregar Egreso" button */
    public Button getBtnAddExpense()         { return btnAddExpense; }

    /** @return the income category management button */
    public Button getBtnAddCategoryIncome()  { return btnAddCategoryIncome; }

    /** @return the expense category management button */
    public Button getBtnAddCategoryExpense() { return btnAddCategoryExpense; }
}
