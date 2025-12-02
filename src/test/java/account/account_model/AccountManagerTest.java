package account.account_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import accounts.account_model.JsonDataHandler;
import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;

class TestDataHandlerStub extends JsonDataHandler {
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

class TestAccountObserverSpy implements AccountObserver {
    public int notificationCount = 0;

    @Override
    public void onNotify(List<Account> accountsList) {
        this.notificationCount++;
    }

    public int getNotificationCount() {
        return notificationCount;
    }
}

public class AccountManagerTest {

    private AccountManager accountManager;
    private TestDataHandlerStub dataHandlerStub;
    private AccountManagerSubject subject;
    private TestAccountObserverSpy observerSpy;

    @BeforeEach
    void setUp() {
        // Test Doubles
        dataHandlerStub = new TestDataHandlerStub();
        subject = new AccountManagerSubject();
        observerSpy = new TestAccountObserverSpy();

        accountManager = new AccountManager(subject, dataHandlerStub);

        accountManager.addObserver(observerSpy);

        dataHandlerStub.saveCallCount = 0;
        observerSpy.notificationCount = 0;
    }

    @Test
    void testAddAccount_GeneratesId_Saves_AndNotifies() {
        accountManager.addAccount("Ahorro", AccountType.DIGITAL, Coin.USD, new BigDecimal("100.00"));

        assertEquals(1, accountManager.getAccountByIndex(1).getId(), "genereted ID must be: 1.");
        assertEquals(1, accountManager.getAccounts().size());

        assertEquals(1, dataHandlerStub.getSaveCallCount(), "must be 1 call to saveAccounts.");
        assertEquals(1, observerSpy.getNotificationCount(), "must be 1 notification to observer.");
    }

    @Test
    void testRemoveAccount_RemovesCorrectly_Saves_AndNotifies() {
        Account account1 = new Account(1, "A1", AccountType.DIGITAL, Coin.USD, BigDecimal.ZERO);
        Account account2 = new Account(2, "A2", AccountType.CASH, Coin.MXN, BigDecimal.ZERO);

        accountManager.getAccounts().add(account1);
        accountManager.getAccounts().add(account2);

        accountManager.removeAccount(1); // Remove ID 1

        assertEquals(1, accountManager.getAccounts().size(), "Only one account should remain");
        assertNull(accountManager.getAccountById(1), "Account 1 must have been deleted");

        assertEquals(1, dataHandlerStub.getSaveCallCount(), "must be 1 call to saveAccounts");
        assertEquals(1, observerSpy.getNotificationCount(), "must be 1 notification to observer");
    }

    @Test
    void testEditAccount_UpdatesDetails_Saves_AndNotifies() {
        Account accountToEdit = new Account(1, "Old Name", AccountType.DIGITAL, Coin.USD, new BigDecimal("100.00"));
        accountManager.getAccounts().add(accountToEdit);

        accountManager.editAccount(accountToEdit, "New Name", AccountType.CASH, Coin.MXN);

        assertEquals("New Name", accountToEdit.getName());
        assertEquals(AccountType.CASH, accountToEdit.getType());

        assertEquals(1, dataHandlerStub.getSaveCallCount(), "must be 1 call to saveAccounts");
        assertEquals(1, observerSpy.getNotificationCount(), "must be 1 notification to observer");
    }
}