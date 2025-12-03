package accounts.account_model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

class AccountTest {

    private Account account;
    private BigDecimal initialBalance;

    @BeforeEach
    void setUp() {
        initialBalance = new BigDecimal("1000.00");
        account = new Account(1, "Test Wallet", AccountType.CASH, Coin.MXN, initialBalance);
    }

    @Test
    void testInitialBalanceIsCorrect() {
        assertEquals(initialBalance, account.getCurrentBalance(), 
            "El saldo actual debería ser igual al saldo inicial al crear la cuenta");
    }

    @Test
    void testAddIncomeUpdatesBalance() {
        MovementCategory salaryCategory = new MovementCategory("Salary", MovementType.INCOME);
        
        BigDecimal incomeAmount = new BigDecimal("500.00");
        Movement income = new Movement(
            UUID.randomUUID(), 
            "Pago quincena", 
            incomeAmount, 
            salaryCategory, 
            account, 
            LocalDateTime.now()
        );

        account.addMovement(income);

        BigDecimal expected = new BigDecimal("1500.00");
        assertEquals(expected, account.getCurrentBalance(), 
            "El saldo debería aumentar al agregar un ingreso");
    }

    @Test
    void testAddExpenseUpdatesBalance() {
        MovementCategory foodCategory = new MovementCategory("Food", MovementType.EXPENSE);
        
        BigDecimal expenseAmount = new BigDecimal("200.00");
        Movement expense = new Movement(
            UUID.randomUUID(), 
            "Tacos", 
            expenseAmount, 
            foodCategory, 
            account, 
            LocalDateTime.now()
        );

        account.addMovement(expense);

        BigDecimal expected = new BigDecimal("800.00");
        assertEquals(expected, account.getCurrentBalance(), 
            "El saldo debería disminuir al agregar un gasto");
    }

    @Test
    void testListsAreInitialized() {
        assertNotNull(account.getMovements(), "La lista de movimientos no debería ser nula");
        assertNotNull(account.getGoals(), "La lista de metas no debería ser nula");
    }
}