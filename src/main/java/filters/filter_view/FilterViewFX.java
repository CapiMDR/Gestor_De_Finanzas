package filters.filter_view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory.MovementType;

import java.time.LocalDate;
import java.util.List;

/**
 * JavaFX controller for the Filter view.
 * Replaces {@code CategoriesView.java} (Swing).
 *
 * <p>
 * Layout is defined declaratively in {@code /fxml/filter.fxml}.
 * Uses the native JavaFX {@link DatePicker} for date range selection.
 * Business logic is delegated to {@code FilterController}.
 *
 * @see filters.filter_controller.FilterController
 */
public class FilterViewFX {

    @FXML
    private ComboBox<String> cmbCategory;
    @FXML
    private DatePicker datePickerFrom;
    @FXML
    private DatePicker datePickerTo;
    @FXML
    private ListView<String> listFilteredMovements;
    @FXML
    private Button btnApplyFilter;
    @FXML
    private Button btnClearFilter;

    @FXML
    private Label lblTotalIncome;
    @FXML
    private Label lblTotalExpense;

    @FXML private Label lblAccountName;
    @FXML private Button btnVolver;

    private Runnable onBack;

    private List<Movement> currentIncomeMovements;
    private List<Movement> currentExpenseMovements;

    @FXML
    public void initialize() {
        if (btnVolver != null) {
            btnVolver.setOnAction(e -> {
                if (onBack != null) onBack.run();
            });
        }
        cmbCategory.getItems().addAll("TODAS", MovementType.INCOME.name(), MovementType.EXPENSE.name());
        cmbCategory.getSelectionModel().selectFirst();

        btnApplyFilter.setOnAction(e -> applyFilter());
        btnClearFilter.setOnAction(e -> clearFilter());

        listFilteredMovements.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    getStyleClass().removeAll("text-income", "text-expense");
                } else {
                    setText(item);
                    getStyleClass().removeAll("text-income", "text-expense");
                    if (item.contains("(INCOME)")) {
                        getStyleClass().add("text-income");
                    } else if (item.contains("(EXPENSE)")) {
                        getStyleClass().add("text-expense");
                    }
                }
            }
        });
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    public void setAccountName(String accountName) {
        if (lblAccountName != null && accountName != null) {
            lblAccountName.setText(accountName);
        }
    }

    public void updateCategories(List<Movement> income, List<Movement> expense, double totalIn, double totalOut) {
        this.currentIncomeMovements = income;
        this.currentExpenseMovements = expense;

        lblTotalIncome.setText(String.format("$%.2f", totalIn));
        lblTotalExpense.setText(String.format("$%.2f", totalOut));

        applyFilter(); // Re-apply existing filter to the new data
    }

    private void applyFilter() {
        if (currentIncomeMovements == null || currentExpenseMovements == null)
            return;

        String selectedCategory = cmbCategory.getValue();
        LocalDate fromDate = datePickerFrom.getValue();
        LocalDate toDate = datePickerTo.getValue();

        List<Movement> filtered = new java.util.ArrayList<>();

        if ("TODAS".equals(selectedCategory) || MovementType.INCOME.name().equals(selectedCategory)) {
            filtered.addAll(currentIncomeMovements);
        }
        if ("TODAS".equals(selectedCategory) || MovementType.EXPENSE.name().equals(selectedCategory)) {
            filtered.addAll(currentExpenseMovements);
        }

        // Apply date filters
        List<Movement> finalFiltered = filtered.stream()
                .filter(m -> {
                    LocalDate moveDate = m.getDate().toLocalDate();
                    boolean afterFrom = (fromDate == null) || !moveDate.isBefore(fromDate);
                    boolean beforeTo = (toDate == null) || !moveDate.isAfter(toDate);
                    return afterFrom && beforeTo;
                })
                .toList();

        listFilteredMovements.getItems().clear();
        for (Movement m : finalFiltered) {
            String catName = m.getCategory() != null ? m.getCategory().getType().name() : "N/A";
            listFilteredMovements.getItems().add(
                    String.format("[%s] %s: $%.2f (%s)", m.getDate().toLocalDate().toString(), m.getDescription(),
                            m.getAmount(), catName));
        }
    }

    private void clearFilter() {
        cmbCategory.getSelectionModel().selectFirst();
        datePickerFrom.setValue(null);
        datePickerTo.setValue(null);
        applyFilter();
    }
}
