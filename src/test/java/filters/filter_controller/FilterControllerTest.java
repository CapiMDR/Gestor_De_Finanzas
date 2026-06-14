package filters.filter_controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import accounts.account_model.Account;
import filters.filter_view.FilterViewFX;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the filtering logic inside the Filter module.
 * Tests applying filters, clearing filters, and filtering by date range.
 */
@DisplayName("Filter Logic Test")
@SuppressWarnings("java:S5973")
class FilterControllerTest {

    private FilterViewFX view;

    private ComboBox<String> cmbCategory;
    private DatePicker datePickerFrom;
    private DatePicker datePickerTo;
    private ListView<String> listFilteredMovements;
    private Button btnApplyFilter;
    private Button btnClearFilter;
    private Label lblTotalIncome;
    private Label lblTotalExpense;

    /**
     * Initializes the JavaFX Toolkit before tests.
     */
    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    /**
     * Sets up the real JavaFX components, injects them into the view,
     * and sets up the controller with sample data.
     */
    @BeforeEach
    void setUp() throws Exception {
        view = new FilterViewFX();

        // Instantiate JavaFX controls
        cmbCategory = new ComboBox<>();
        datePickerFrom = new DatePicker();
        datePickerTo = new DatePicker();
        listFilteredMovements = new ListView<>();
        btnApplyFilter = new Button();
        btnClearFilter = new Button();
        lblTotalIncome = new Label();
        lblTotalExpense = new Label();

        // Inject them into the view using reflection
        injectField(view, "cmbCategory", cmbCategory);
        injectField(view, "datePickerFrom", datePickerFrom);
        injectField(view, "datePickerTo", datePickerTo);
        injectField(view, "listFilteredMovements", listFilteredMovements);
        injectField(view, "btnApplyFilter", btnApplyFilter);
        injectField(view, "btnClearFilter", btnClearFilter);
        injectField(view, "lblTotalIncome", lblTotalIncome);
        injectField(view, "lblTotalExpense", lblTotalExpense);

        // Call initialize to set up bindings and items
        view.initialize();

        // Create a dummy account with movements
        Account account = createDummyAccount();
        
        // This will call view.updateCategories() on the JavaFX thread, so we run it on the same thread
        // Wait, testing JavaFX threaded code requires waiting for Platform.runLater
        // To bypass threading issues, we can directly supply movements to view
        List<Movement> income = new ArrayList<>();
        List<Movement> expense = new ArrayList<>();
        
        for (Movement m : account.getMovements()) {
            if (m.getCategory().getType() == MovementCategory.MovementType.INCOME) {
                income.add(m);
            } else {
                expense.add(m);
            }
        }
        
        // Populate the view synchronously for testing instead of going through Platform.runLater
        view.updateCategories(income, expense, 1000, 500);
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Account createDummyAccount() {
        Account acc = new Account(1, "Test", Account.AccountType.CASH, Account.Coin.MXN, BigDecimal.ZERO);
        List<Movement> moves = new ArrayList<>();
        
        // Income movement: 2 days ago
        Movement m1 = new Movement(UUID.randomUUID(), "Salary", new BigDecimal("1000"), 
            new MovementCategory("Salary", MovementCategory.MovementType.INCOME), acc, LocalDateTime.now().minusDays(2));
            
        // Expense movement: 5 days ago
        Movement m2 = new Movement(UUID.randomUUID(), "Food", new BigDecimal("500"), 
            new MovementCategory("Food", MovementCategory.MovementType.EXPENSE), acc, LocalDateTime.now().minusDays(5));
            
        moves.add(m1);
        moves.add(m2);
        acc.setMovements(moves);
        return acc;
    }

    /**
     * Tests that applying a category filter correctly updates the movements list.
     */
    @Test
    @DisplayName("testApplyFiltersUpdatesMovementsList should filter by category")
    void testApplyFiltersUpdatesMovementsList() {
        // Arrange: Select INCOME category
        cmbCategory.setValue(MovementCategory.MovementType.INCOME.name());

        // Act
        btnApplyFilter.fire();

        // Assert
        assertEquals(1, listFilteredMovements.getItems().size());
        assertTrue(listFilteredMovements.getItems().get(0).contains("Salary"));
    }

    /**
     * Tests that clearing the filters restores all original movements.
     */
    @Test
    @DisplayName("testClearFiltersRestoresAllMovements should reset all filters")
    void testClearFiltersRestoresAllMovements() {
        // Arrange: Apply a filter first
        cmbCategory.setValue(MovementCategory.MovementType.INCOME.name());
        btnApplyFilter.fire();
        assertEquals(1, listFilteredMovements.getItems().size());

        // Act: Clear filters
        btnClearFilter.fire();

        // Assert: Should restore all items (TODAS)
        assertEquals(2, listFilteredMovements.getItems().size());
        assertEquals("TODAS", cmbCategory.getValue());
    }

    /**
     * Tests that filtering by a specific date range accurately narrows down the list.
     */
    @Test
    @DisplayName("testFilterByDateRange should filter movements between dates")
    void testFilterByDateRange() {
        // Arrange: Filter from 3 days ago to today (should only include the 2-days-ago movement)
        LocalDate fromDate = LocalDate.now().minusDays(3);
        LocalDate toDate = LocalDate.now();
        datePickerFrom.setValue(fromDate);
        datePickerTo.setValue(toDate);

        // Act
        btnApplyFilter.fire();

        // Assert
        assertEquals(1, listFilteredMovements.getItems().size());
        assertTrue(listFilteredMovements.getItems().get(0).contains("Salary"));
        
        // Second Arrange: Filter from 10 days ago to 4 days ago (should only include the 5-days-ago movement)
        datePickerFrom.setValue(LocalDate.now().minusDays(10));
        datePickerTo.setValue(LocalDate.now().minusDays(4));
        
        // Act
        btnApplyFilter.fire();
        
        // Assert
        assertEquals(1, listFilteredMovements.getItems().size());
        assertTrue(listFilteredMovements.getItems().get(0).contains("Food"));
    }
}
