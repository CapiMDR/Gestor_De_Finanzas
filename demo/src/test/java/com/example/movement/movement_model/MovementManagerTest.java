package com.example.movement.movement_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.account.account_model.Account;
import com.example.account.account_model.Account.AccountType;
import com.example.account.account_model.Account.Coin;
import com.example.movement.movement_model.MovementCategory.MovementType;

public class MovementManagerTest{

    private MovementManager movementManager;
    private MovementManagerSubject subject;
    private Account testAccount;
    private MovementCategory incomeCategory;
    private MovementCategory expenseCategory;

    @BeforeEach
    void setUp() {
        subject = new MovementManagerSubject();
        movementManager = new MovementManager(subject);
        
        testAccount = new Account(1, "Test Account", AccountType.CASH, Coin.USD, new BigDecimal("100.00"));
        incomeCategory = new MovementCategory("Salary", MovementType.INCOME);
        expenseCategory = new MovementCategory("Groceries", MovementType.EXPENSE);
    }

    @Test
    void testAddMovementAndGenerateId(){
        Movement movement1 = new Movement(0, "M1", new BigDecimal("50.00"), incomeCategory, testAccount);
        movementManager.addMovement(movement1);
        
        assertEquals(1, movement1.getIdMovement(), "ID must be 1");
        assertEquals(1, movementManager.getMovements().size());

        Movement movement2 = new Movement(0, "M2", new BigDecimal("20.00"), expenseCategory, testAccount);
        movementManager.addMovement(movement2);
        
        assertEquals(2, movement2.getIdMovement(), "ID must be 2.");
        assertEquals(2, movementManager.getMovements().size());
    }

    @Test
    void testAddCategory(){
        movementManager.addCategory(incomeCategory);
        movementManager.addCategory(expenseCategory);

        assertEquals(2, movementManager.getCategories().size());
        assertNotNull(movementManager.getCategoryByName("Salary"), "The Salary category must exist.");
        assertNotNull(movementManager.getCategoryByName("Groceries"), "The Groceries category must exist.");
    }
    
    @Test
    void testRemoveCategory() {
        movementManager.addCategory(incomeCategory);
        movementManager.addCategory(expenseCategory);
        assertEquals(2, movementManager.getCategories().size());

        movementManager.removeCategory(expenseCategory);
        
        assertEquals(1, movementManager.getCategories().size());
        assertNull(movementManager.getCategoryByName("Groceries"), "The Groceries category must have been deleted");
        assertNotNull(movementManager.getCategoryByName("Salary"), "The Salary category must exist.");
    }
}