package movements.movement_controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import javafx.application.Platform;
import javafx.scene.control.Button;
import movements.movement_model.CategoryManager;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import movements.movement_view.MovementsViewFX;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link MovementController}.
 * Verifies adding movements, validating inputs, and observing category changes.
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("MovementController Test")
class MovementControllerTest {

    @Mock
    private CategoryManager model;

    @Mock
    private MovementsViewFX view;

    @Mock
    private Account account;

    @Mock
    private Button btnAddIncome;
    @Mock
    private Button btnAddExpense;
    @Mock
    private Button btnAddCategoryIncome;
    @Mock
    private Button btnAddCategoryExpense;

    private MovementController controller;
    private MockedStatic<AccountManager> mockedAccountManager;
    private MockedStatic<AccountManagerSubject> mockedAccountManagerSubject;

    /**
     * Initializes the JavaFX Toolkit before testing to prevent NoClassDefFoundError.
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
     * Sets up the mocks and creates the controller instance.
     */
    @BeforeEach
    void setUp() {
        // Mock UI components returned by the view to prevent NullPointerException in AssignEvents
        lenient().when(view.getBtnAddIncome()).thenReturn(btnAddIncome);
        lenient().when(view.getBtnAddExpense()).thenReturn(btnAddExpense);
        lenient().when(view.getBtnAddCategoryIncome()).thenReturn(btnAddCategoryIncome);
        lenient().when(view.getBtnAddCategoryExpense()).thenReturn(btnAddCategoryExpense);

        // Mock Account name for initial data load
        lenient().when(account.getName()).thenReturn("Test Account");

        // Mock categories for initial data load
        lenient().when(model.getCategories()).thenReturn(new HashMap<>());

        mockedAccountManager = mockStatic(AccountManager.class);
        mockedAccountManagerSubject = mockStatic(AccountManagerSubject.class);

        controller = new MovementController(model, view, account);
    }

    /**
     * Cleans up static mocks.
     */
    @AfterEach
    void tearDown() {
        mockedAccountManager.close();
        mockedAccountManagerSubject.close();
    }

    /**
     * Tests that a valid movement is added and the model is updated and saved.
     */
    @Test
    @DisplayName("should add movement to account and notify observers")
    void testAddMovement() {
        // Arrange
        String description = "Salario";
        BigDecimal amount = new BigDecimal("5000.00");
        MovementCategory category = new MovementCategory("Salario", MovementType.INCOME);
        LocalDateTime date = LocalDateTime.now();

        // Act
        controller.addMovement(description, amount, category, account, date);

        // Assert
        verify(account).addMovement(any(Movement.class));
        mockedAccountManager.verify(AccountManager::saveAccountsData);
        mockedAccountManagerSubject.verify(() -> AccountManagerSubject.notifyObservers(any()));
        verify(model).notifyObservers();
    }

    /**
     * Tests that the handleAddMovement internal method properly processes a valid income input from the view.
     */
    @Test
    @DisplayName("should handle add movement valid income input")
    void testHandleAddMovementValidIncome() throws Exception {
        // Arrange
        when(view.getDescriptionIncome()).thenReturn("Bono");
        when(view.getAmountIncomeText()).thenReturn("1000.50");
        when(view.getSelectedCategoryIncome()).thenReturn("Premios");
        when(view.getIncomeDateAsLocalDateTime()).thenReturn(LocalDateTime.now());

        MovementCategory mockCategory = new MovementCategory("Premios", MovementType.INCOME);
        when(model.getCategoryByName("Premios")).thenReturn(mockCategory);

        // Act
        Method handleAddMovement = MovementController.class.getDeclaredMethod("handleAddMovement", MovementType.class);
        handleAddMovement.setAccessible(true);
        handleAddMovement.invoke(controller, MovementType.INCOME);

        // Assert
        verify(account).addMovement(any(Movement.class));
        verify(view).clearIncomeFields();
    }

    /**
     * Tests that invalid inputs (empty fields) trigger validation failure without saving.
     */
    @Test
    @DisplayName("should not add movement if validation fails")
    void testHandleAddMovementInvalid() throws Exception {
        // Arrange - empty description
        when(view.getDescriptionExpense()).thenReturn("");
        when(view.getAmountExpenseText()).thenReturn("500");
        when(view.getSelectedCategoryExpense()).thenReturn("Comida");
        when(view.getExpenseDateAsLocalDateTime()).thenReturn(LocalDateTime.now());

        // Act
        Method handleAddMovement = MovementController.class.getDeclaredMethod("handleAddMovement", MovementType.class);
        handleAddMovement.setAccessible(true);
        handleAddMovement.invoke(controller, MovementType.EXPENSE);

        // Assert - shouldn't interact with account or save
        verify(account, never()).addMovement(any());
        mockedAccountManager.verify(AccountManager::saveAccountsData, never());
    }
}
