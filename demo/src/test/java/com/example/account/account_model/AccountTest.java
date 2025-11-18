package com.example.account.account_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.account.account_model.Account.AccountType;
import com.example.account.account_model.Account.Coin;
import com.example.movement.movement_model.Movement;
import com.example.movement.movement_model.MovementCategory;
import com.example.movement.movement_model.MovementCategory.MovementType;

public class AccountTest {

    private Account account;
    private final BigDecimal initialBalance = new BigDecimal("1000.00");

    @BeforeEach
    void setUp() {
        account = new Account(1, "Test Account", AccountType.CASH, Coin.USD, initialBalance);
    }

    @Test
    void testAccountCreation() {
        assertEquals(1, account.getId());
        assertEquals("Test Account", account.getName());
        assertEquals(AccountType.CASH, account.getType());
        assertEquals(Coin.USD, account.getCoin());
        assertEquals(initialBalance, account.getInitialBalance());
        assertEquals(initialBalance, account.getCurrentBalance());
        assertTrue(account.getMovements().isEmpty());
    }

    @Test
    void testAddIncomeMovement() {
        MovementCategory incomeCategory = new MovementCategory("Salary", MovementType.INCOME);
        BigDecimal incomeAmount = new BigDecimal("500.00");
        Movement income = new Movement(1, "Monthly Salary", incomeAmount, incomeCategory, account, LocalDateTime.now());

        //add movement
        account.addMovement(income);

        //verify balance: 1000 + 500 = 1500
        BigDecimal expectedBalance = initialBalance.add(incomeAmount); //1500

        assertEquals(expectedBalance, account.getCurrentBalance());
        assertEquals(1, account.getMovements().size());
        assertEquals(income, account.getMovements().get(0));
    }

    @Test
    void testAddExpenseMovement() {
        MovementCategory expenseCategory = new MovementCategory("Groceries", MovementType.EXPENSE);
        BigDecimal expenseAmount = new BigDecimal("150.50");
        Movement expense = new Movement(2, "Weekly Groceries", expenseAmount, expenseCategory, account, LocalDateTime.now());

        //add mov.
        account.addMovement(expense);

        //verify balance: 1000 - 150.50 = 849.50
        BigDecimal expectedBalance = initialBalance.subtract(expenseAmount); //849.50
        assertEquals(expectedBalance, account.getCurrentBalance());
        assertEquals(1, account.getMovements().size());
    }

    @Test
    void testAddMixedMovements() {
        //income: +200.00
        MovementCategory incomeCategory = new MovementCategory("Bonus", MovementType.INCOME);
        BigDecimal incomeAmount = new BigDecimal("200.00");
        Movement income = new Movement(1, "Q1 Bonus", incomeAmount, incomeCategory, account, LocalDateTime.now());

        //expense: -75.00
        MovementCategory expenseCategory = new MovementCategory("Dinner", MovementType.EXPENSE);
        BigDecimal expenseAmount = new BigDecimal("75.00");
        Movement expense = new Movement(2, "Restaurant Bill", expenseAmount, expenseCategory, account, LocalDateTime.now());

        account.addMovement(income);
        account.addMovement(expense);

        //expected balance: 1000+200-75 = 1125.00
        BigDecimal expectedBalance = new BigDecimal("1125.00");
        assertEquals(expectedBalance, account.getCurrentBalance());
        assertEquals(2, account.getMovements().size());
    }
}