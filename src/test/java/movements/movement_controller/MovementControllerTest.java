package movements.movement_controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
import movements.movement_model.CategoryManager;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import movements.movement_view.MovementsViewFX;
import javafx.scene.control.Button;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovementController Test")
class MovementControllerTest {

    @Mock
    private CategoryManager mockModel;

    @Mock
    private MovementsViewFX mockView;

    @Mock
    private Account mockAccount;

    private MovementController controller;
    private MockedStatic<AccountManager> mockedAccountManager;

    @BeforeAll
    static void initJFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Ignored
        }
    }

    @BeforeEach
    void setUp() {
        lenient().when(mockView.getBtnAddIncome()).thenReturn(mock(Button.class));
        lenient().when(mockView.getBtnAddExpense()).thenReturn(mock(Button.class));
        lenient().when(mockView.getBtnAddCategoryIncome()).thenReturn(mock(Button.class));
        lenient().when(mockView.getBtnAddCategoryExpense()).thenReturn(mock(Button.class));

        lenient().when(mockAccount.getName()).thenReturn("Main Account");
        lenient().when(mockAccount.getMovements()).thenReturn(new ArrayList<>());

        mockedAccountManager = mockStatic(AccountManager.class);

        controller = new MovementController(mockModel, mockView, mockAccount);
    }

    @AfterEach
    void tearDown() {
        mockedAccountManager.close();
    }

    @Test
    @DisplayName("should add income successfully")
    void testHandleAddIncomeSuccess() throws Exception {
        when(mockView.getDescriptionIncome()).thenReturn("Salary");
        when(mockView.getAmountIncomeText()).thenReturn("1000");
        when(mockView.getSelectedCategoryIncome()).thenReturn("Job");
        when(mockView.getIncomeDateAsLocalDateTime()).thenReturn(LocalDateTime.now());
        
        MovementCategory mockCat = new MovementCategory("Job", MovementType.INCOME);
        when(mockModel.getCategoryByName("Job")).thenReturn(mockCat);

        // Use reflection to call handleAddMovement(MovementType.INCOME)
        java.lang.reflect.Method method = MovementController.class.getDeclaredMethod("handleAddMovement", MovementType.class);
        method.setAccessible(true);
        method.invoke(controller, MovementType.INCOME);

        verify(mockView).clearIncomeFields();
        mockedAccountManager.verify(AccountManager::saveAccountsData, atLeastOnce());
        
        // Assert movement added to account
        verify(mockAccount).addMovement(any(Movement.class));
    }

    @Test
    @DisplayName("should add expense successfully")
    void testHandleAddExpenseSuccess() throws Exception {
        when(mockView.getDescriptionExpense()).thenReturn("Food");
        when(mockView.getAmountExpenseText()).thenReturn("50");
        when(mockView.getSelectedCategoryExpense()).thenReturn("Restaurant");
        when(mockView.getExpenseDateAsLocalDateTime()).thenReturn(LocalDateTime.now());
        
        MovementCategory mockCat = new MovementCategory("Restaurant", MovementType.EXPENSE);
        when(mockModel.getCategoryByName("Restaurant")).thenReturn(mockCat);

        java.lang.reflect.Method method = MovementController.class.getDeclaredMethod("handleAddMovement", MovementType.class);
        method.setAccessible(true);
        method.invoke(controller, MovementType.EXPENSE);

        verify(mockView).clearExpenseFields();
        mockedAccountManager.verify(AccountManager::saveAccountsData, atLeastOnce());
        verify(mockAccount).addMovement(any(Movement.class));
    }

    @Test
    @DisplayName("should fail validation on empty fields")
    void testHandleAddMovementValidation() throws Exception {
        when(mockView.getDescriptionIncome()).thenReturn("");
        when(mockView.getAmountIncomeText()).thenReturn("");

        java.lang.reflect.Method method = MovementController.class.getDeclaredMethod("handleAddMovement", MovementType.class);
        method.setAccessible(true);
        method.invoke(controller, MovementType.INCOME);

        // Alert is shown internally, we just ensure no movement is added
        verify(mockAccount, never()).addMovement(any());
    }
}
