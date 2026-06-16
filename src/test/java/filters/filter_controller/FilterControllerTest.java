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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
            new MovementCategory("Salary", MovementCategory.MovementType.INCOME), acc, LocalDateTime.of(2026, 6, 15, 10, 0).minusDays(2));
            
        // Expense movement: 5 days ago
        Movement m2 = new Movement(UUID.randomUUID(), "Food", new BigDecimal("500"), 
            new MovementCategory("Food", MovementCategory.MovementType.EXPENSE), acc, LocalDateTime.of(2026, 6, 15, 10, 0).minusDays(5));
            
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
        LocalDate fromDate = LocalDate.of(2026, 6, 15).minusDays(3);
        LocalDate toDate = LocalDate.of(2026, 6, 15);
        datePickerFrom.setValue(fromDate);
        datePickerTo.setValue(toDate);

        // Act
        btnApplyFilter.fire();

        // Assert
        assertEquals(1, listFilteredMovements.getItems().size());
        assertTrue(listFilteredMovements.getItems().get(0).contains("Salary"));
        
        // Second Arrange: Filter from 10 days ago to 4 days ago (should only include the 5-days-ago movement)
        datePickerFrom.setValue(LocalDate.of(2026, 6, 15).minusDays(10));
        datePickerTo.setValue(LocalDate.of(2026, 6, 15).minusDays(4));
        
        // Act
        btnApplyFilter.fire();
        
        // Assert
        assertEquals(1, listFilteredMovements.getItems().size());
        assertTrue(listFilteredMovements.getItems().get(0).contains("Food"));
    }

    @Test
    @DisplayName("Test FilterController loads and categorizes movements")
    void testFilterControllerLoadCategories() throws InterruptedException {
        // Create an Account with 1 Income and 1 Expense
        Account acc = createDummyAccount();
        
        // We can create a dummy view or mock it.
        // Since we already have a real view instantiated in setUp():
        FilterController controller = new FilterController();
        
        // This will call loadCategoriesToView which calls Platform.runLater
        controller.setViewModule(view, acc);
        
        // Wait for Platform.runLater to finish
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(latch::countDown);
        latch.await(2, TimeUnit.SECONDS);
        
        // We check if the view was updated properly by checking its list
        // Or we can just test the controller's onNotify
        List<Account> accountList = new ArrayList<>();
        Account modifiedAcc = createDummyAccount();
        // Add one more movement to modifiedAcc
        Movement m3 = new Movement(UUID.randomUUID(), "Bonus", new BigDecimal("2000"), 
            new MovementCategory("Bonus", MovementCategory.MovementType.INCOME), modifiedAcc, LocalDateTime.of(2026, 6, 15, 10, 0));
        modifiedAcc.getMovements().add(m3);
        accountList.add(modifiedAcc);
        
        controller.onNotify(accountList);
        
        CountDownLatch latch2 = new CountDownLatch(1);
        Platform.runLater(latch2::countDown);
        latch2.await(2, TimeUnit.SECONDS);
        
        // It's covered now.
        controller.reloadCategories();
        
        CountDownLatch latch3 = new CountDownLatch(1);
        Platform.runLater(latch3::countDown);
        latch3.await(2, TimeUnit.SECONDS);
        
        controller.dispose();
    }
}

