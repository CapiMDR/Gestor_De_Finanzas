package com.example.movement.movement_controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.account.account_model.Account;
import com.example.account.account_model.Account.AccountType;
import com.example.account.account_model.Account.Coin;
import com.example.account.account_model.AccountManager;
import com.example.account.account_model.AccountManagerSubject;
import com.example.account.account_model.JsonDataHandler;
import com.example.movement.movement_model.Movement;
import com.example.movement.movement_model.MovementCategory;
import com.example.movement.movement_model.MovementCategory.MovementType;
import com.example.movement.movement_model.MovementManager;
import com.example.movement.movement_model.MovementManagerSubject;

class TestDataHandlerStub extends JsonDataHandler{
    public List<Account> inMemoryAccounts = new ArrayList<>();
    public int saveCallCount = 0;

    @Override
    public List<Account> loadAccounts() {
        return new ArrayList<>(inMemoryAccounts);
    }
    
    @Override
    public void saveAccounts(List<Account> accounts) {
        this.inMemoryAccounts = new ArrayList<>(accounts);
        this.saveCallCount++;
    }
    
    public int getSaveCallCount() {
        return saveCallCount;
    }
}

class TestMovementManagerStub extends MovementManager{
    private int addMovementCallCount = 0;
    private List<Movement> movements = new ArrayList<>();
    
    public TestMovementManagerStub(MovementManagerSubject subject){
        super(subject);
    }

    @Override
    public void addMovement(Movement movement){
        this.movements.add(movement);
        this.addMovementCallCount++;
    }
    
    @Override
    public List<Movement> getMovements(){
        return this.movements;
    }
    
    public int getAddMovementCallCount(){
        return addMovementCallCount;
    }
}

public class MovementControllerTest {
    
    private TestDataHandlerStub dataHandlerStub;
    private AccountManagerSubject accountSubject;
    private AccountManager accountManager;
    
    private MovementManagerSubject movementSubject;
    private TestMovementManagerStub movementManagerStub;
    
    private MovementController controller;
    private Account testAccount;
    private MovementCategory incomeCategory;

    @BeforeEach
    void setUp(){
        dataHandlerStub = new TestDataHandlerStub();
        accountSubject = new AccountManagerSubject();

        accountManager = new AccountManager(accountSubject, dataHandlerStub); 
        
        movementSubject = new MovementManagerSubject();
        movementManagerStub = new TestMovementManagerStub(movementSubject);
        
        controller = new MovementController(movementManagerStub, accountManager);
        
        testAccount = new Account(1, "Test Account", AccountType.CASH, Coin.USD, new BigDecimal("100.00"));
        accountManager.getAccounts().add(testAccount);
        incomeCategory = new MovementCategory("Salary", MovementType.INCOME);
    }

    @Test
    void testAddMovement_Orchestration() {
        BigDecimal movementAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("600.00");
        
        dataHandlerStub.saveCallCount = 0; 
        
        controller.addMovement("Test Income", movementAmount, incomeCategory, testAccount);

        assertEquals(expectedBalance, testAccount.getCurrentBalance(), "balance must be 600.00");
        assertEquals(1, testAccount.getMovements().size(), "The account must have 1 associated transaction");
        
        assertEquals(1, movementManagerStub.getAddMovementCallCount(), "The MovementController must call addMovement once in the MovementManager");
            
        assertEquals(1, dataHandlerStub.getSaveCallCount(), "There must be 1 call to saveAccountsData() in AccountManager");
        
    }
}