package accounts.account_model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import config.AppConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.mockStatic;

import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

class AccountManagerTest {

    private MockedStatic<AppConfig> mockedAppConfig;
    private Path tempAccountsFile;

    @BeforeEach
    void setUp() throws IOException {
        tempAccountsFile = Files.createTempFile("test_accounts_", ".json");
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getAccountsFilePath).thenReturn(tempAccountsFile.toString());
        AccountManager.clearForTesting();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockedAppConfig.close();
        Files.deleteIfExists(tempAccountsFile);
    }

    @Test
    void testAddAccount(){
        AccountManager.addAccount("Banco Test", AccountType.DIGITAL, Coin.USD, new BigDecimal("100"));

        List<Account> accounts = AccountManager.getAccounts();
        assertEquals(1, accounts.size(), "Debería haber 1 cuenta en la lista");
        assertEquals("Banco Test", accounts.get(0).getName(), "El nombre de la cuenta guardada es incorrecto");
    }

    @Test
    void testRemoveAccount(){
        AccountManager.addAccount("A borrar", AccountType.CASH, Coin.MXN, BigDecimal.TEN);
        int idToDelete = AccountManager.getAccounts().get(0).getId();

        AccountManager.removeAccount(idToDelete);

        assertEquals(0, AccountManager.getAccounts().size(), "La lista debería estar vacía después de eliminar");
    }

    @Test
    void testEditAccount(){
        AccountManager.addAccount("Original", AccountType.CASH, Coin.MXN, BigDecimal.ZERO);
        Account accountToEdit = AccountManager.getAccounts().get(0);

        AccountManager.editAccount(accountToEdit, "Editado", AccountType.DIGITAL, Coin.USD);

        Account updatedAccount = AccountManager.getAccountById(accountToEdit.getId());
        assertEquals("Editado", updatedAccount.getName());
        assertEquals(AccountType.DIGITAL, updatedAccount.getType());
        assertEquals(Coin.USD, updatedAccount.getCoin());
    }

    @Test
    void testGetAccountByIndexInvalid(){
        Account result = AccountManager.getAccountByIndex(999);
        assertNull(result, "Debería devolver null si el índice no existe");
        
        Account resultNegative = AccountManager.getAccountByIndex(-1);
        assertNull(resultNegative, "Debería devolver null si el índice es negativo");
    }
    
    @Test
    void testUniqueIds(){
        AccountManager.addAccount("A1", AccountType.CASH, Coin.MXN, BigDecimal.ZERO);
        AccountManager.addAccount("A2", AccountType.CASH, Coin.MXN, BigDecimal.ZERO);
        
        Account a1 = AccountManager.getAccounts().get(0);
        Account a2 = AccountManager.getAccounts().get(1);
        
        assertNotEquals(a1.getId(), a2.getId(), "Las cuentas deben tener IDs diferentes");
    }

    @Test
    void testGetAccountByIdInvalid(){
        Account result = AccountManager.getAccountById(999);
        assertNull(result, "Debería devolver null si el ID no existe");
    }

    @Test
    void testInitAndLoadData(){
        // initAccountManager calls dataHandler.loadAccounts() which will load the temp file we created (empty)
        AccountManager.initAccountManager();
        assertEquals(0, AccountManager.getAccounts().size());

        // loadInitialData just notifies observers
        AccountManager.loadInitialData();
    }

    @Test
    void testAddRemoveObserver(){
        AccountObserver mockObserver = org.mockito.Mockito.mock(AccountObserver.class);
        
        AccountManager.addObserver(mockObserver);
        AccountManager.addAccount("Test Obs", AccountType.CASH, Coin.MXN, BigDecimal.ZERO);
        org.mockito.Mockito.verify(mockObserver, org.mockito.Mockito.times(1)).onNotify(org.mockito.Mockito.anyList());
        
        AccountManager.removeObserver(mockObserver);
        AccountManager.addAccount("Test Obs 2", AccountType.CASH, Coin.MXN, BigDecimal.ZERO);
        org.mockito.Mockito.verify(mockObserver, org.mockito.Mockito.times(1)).onNotify(org.mockito.Mockito.anyList()); // should not have incremented
    }

    @Test
    void testPrivateConstructor() throws Exception {
        java.lang.reflect.Constructor<AccountManager> constructor = AccountManager.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}