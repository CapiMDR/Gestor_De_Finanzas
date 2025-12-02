package movement.movement_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import accounts.account_model.Account;
import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementManager;
import movements.movement_model.MovementManagerSubject;
import movements.movement_model.MovementCategory.MovementType;

public class MovementManagerTest {

    private MovementManager movementManager;
    private MovementManagerSubject subject;
    private Account testAccount;
    private MovementCategory incomeCategory;
    private MovementCategory expenseCategory;

    @BeforeEach
    void setUp() {
        subject = new MovementManagerSubject();
        movementManager = new MovementManager(subject, null);

        testAccount = new Account(1, "Test Account", AccountType.CASH, Coin.USD, new BigDecimal("100.00"));
        incomeCategory = new MovementCategory("Salary", MovementType.INCOME);
        expenseCategory = new MovementCategory("Groceries", MovementType.EXPENSE);
    }

    @Test
    void testAddMovementAndGenerateId() {
        // Test ID generation starting from 0 or empty list
        Movement movement1 = new Movement(0, "M1", new BigDecimal("50.00"), incomeCategory, testAccount);
        movementManager.addMovement(movement1);

        // Assuming the manager assigns IDs incrementally or based on size
        // If your manager assigns ID based on list size, the first ID depends on your
        // logic.
        // Here assuming standard behavior:
        assertEquals(1, movementManager.getMovements().size());

        Movement movement2 = new Movement(0, "M2", new BigDecimal("20.00"), expenseCategory, testAccount);
        movementManager.addMovement(movement2);

        assertEquals(2, movementManager.getMovements().size());
    }

    @Test
    void testAddCategory() {
        movementManager.addCategory(incomeCategory);
        movementManager.addCategory(expenseCategory);

        // Verify categories are stored
        // Note: Check if your Manager stores categories in a List or Map.
        // If Map, size matches unique keys.
        assertEquals(2, movementManager.getCategories().size());
        assertNotNull(movementManager.getCategoryByName("Salary"), "The Salary category must exist.");
        assertNotNull(movementManager.getCategoryByName("Groceries"), "The Groceries category must exist.");
    }

    @Test
    void testRemoveCategory() {
        movementManager.addCategory(incomeCategory);
        movementManager.addCategory(expenseCategory);
        assertEquals(2, movementManager.getCategories().size());

        // Act
        movementManager.removeCategory(expenseCategory); // Assuming remove takes the object or ID

        // Assert
        assertEquals(1, movementManager.getCategories().size());
        assertNull(movementManager.getCategoryByName("Groceries"), "The Groceries category must have been deleted");
        assertNotNull(movementManager.getCategoryByName("Salary"), "The Salary category must still exist.");
    }
}