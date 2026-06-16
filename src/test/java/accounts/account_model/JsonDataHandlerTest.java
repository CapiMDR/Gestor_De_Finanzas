package accounts.account_model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import config.AppConfig;
import movements.movement_model.MovementCategory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link JsonDataHandler}.
 * Validates serialization and deserialization of application data to JSON files.
 */
@DisplayName("JsonDataHandler Test")
class JsonDataHandlerTest {

    private JsonDataHandler handler;
    private Path tempAccountsFile;
    private MockedStatic<AppConfig> mockedAppConfig;

    /**
     * Initializes the handler and redirects the JSON paths to a temporary file 
     * to avoid overriding real user data.
     *
     * @throws IOException if creating the temporary file fails
     */
    @BeforeEach
    void setUp() throws IOException {
        handler = new JsonDataHandler();
        
        // Create a temporary file for tests
        tempAccountsFile = Files.createTempFile("test_accounts_", ".json");
        
        // Mock the static AppConfig methods to return our temp file path
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getAccountsFilePath).thenReturn(tempAccountsFile.toString());
    }

    /**
     * Cleans up the temporary files and removes the static mock after each test.
     *
     * @throws IOException if deleting the temporary file fails
     */
    @AfterEach
    void tearDown() throws IOException {
        mockedAppConfig.close();
        Files.deleteIfExists(tempAccountsFile);
    }

    /**
     * Tests that a list of accounts can be saved and subsequently loaded
     * while retaining their correct state.
     */
    @Test
    @DisplayName("saveAccounts and loadAccounts should serialize and deserialize correctly")
    void testSaveAndLoadAccounts() {
        // Arrange
        List<Account> accounts = new ArrayList<>();
        Account acc = new Account(1, "Test Account", Account.AccountType.CASH, Account.Coin.MXN, new BigDecimal("100.50"));
        
        List<movements.movement_model.Movement> movList = new ArrayList<>();
        MovementCategory catExpense = new MovementCategory("Food", MovementCategory.MovementType.EXPENSE);
        MovementCategory catIncome = new MovementCategory("Salary", MovementCategory.MovementType.INCOME);
        movements.movement_model.Movement mov1 = new movements.movement_model.Movement(
            java.util.UUID.randomUUID(), "Tacos", new BigDecimal("50.00"), catExpense, acc, java.time.LocalDateTime.now()
        );
        movements.movement_model.Movement mov2 = new movements.movement_model.Movement(
            java.util.UUID.randomUUID(), "Paycheck", new BigDecimal("500.00"), catIncome, acc, java.time.LocalDateTime.now()
        );
        movList.add(mov1);
        movList.add(mov2);
        acc.setMovements(movList);

        List<goals.goals_model.Goal> goalsList = new ArrayList<>();
        goals.goals_model.Goal goal = new goals.goals_model.Goal("Save", new BigDecimal("1000.00"), "For car");
        goal.setCurrentAmount(new BigDecimal("100.00"));
        goalsList.add(goal);
        acc.setGoals(goalsList);

        accounts.add(acc);

        // Act
        handler.saveAccounts(accounts);
        List<Account> loadedAccounts = handler.loadAccounts();

        // Assert
        assertEquals(1, loadedAccounts.size(), "Should load exactly 1 account");
        Account loadedAcc = loadedAccounts.get(0);
        assertEquals(acc.getId(), loadedAcc.getId());
        assertEquals(acc.getName(), loadedAcc.getName());
        assertEquals(acc.getType(), loadedAcc.getType());
        assertEquals(acc.getCoin(), loadedAcc.getCoin());
        
        assertEquals(2, loadedAcc.getMovements().size());
        assertEquals(1, loadedAcc.getGoals().size());
        assertEquals("Tacos", loadedAcc.getMovements().get(0).getDescription());
        assertEquals("Paycheck", loadedAcc.getMovements().get(1).getDescription());
        assertEquals("Save", loadedAcc.getGoals().get(0).getName());
    }

    @Test
    @DisplayName("saveCategories and loadCategories should serialize and deserialize correctly")
    void testSaveAndLoadCategories() throws IOException {
        Path tempCatFile = Files.createTempFile("test_cats_", ".json");
        mockedAppConfig.when(AppConfig::getCategoriesFilePath).thenReturn(tempCatFile.toString());

        Map<String, MovementCategory> categories = new java.util.HashMap<>();
        categories.put("Food", new MovementCategory("Food", MovementCategory.MovementType.EXPENSE));
        
        handler.saveCategories(categories);
        Map<String, MovementCategory> loadedCats = handler.loadCategories();
        
        assertEquals(1, loadedCats.size());
        assertTrue(loadedCats.containsKey("Food"));
        assertEquals(MovementCategory.MovementType.EXPENSE, loadedCats.get("Food").getType());
        
        Files.deleteIfExists(tempCatFile);
    }

    @Test
    @DisplayName("loadAccounts should return an empty list if file is missing")
    void testHandleEmptyOrMissingFile() throws IOException {
        Files.deleteIfExists(tempAccountsFile);
        List<Account> loadedAccounts = handler.loadAccounts();
        assertTrue(loadedAccounts.isEmpty(), "Should return an empty list when file does not exist");
    }
    
    @Test
    @DisplayName("Should backup file and return empty when accounts file is corrupted")
    void testLoadAccountsCorrupted() throws IOException {
        Files.writeString(tempAccountsFile, "invalid json {");
        List<Account> loaded = handler.loadAccounts();
        assertTrue(loaded.isEmpty());
        assertTrue(Files.exists(Paths.get(tempAccountsFile.toString() + ".bak")));
        Files.deleteIfExists(Paths.get(tempAccountsFile.toString() + ".bak"));
    }

    @Test
    @DisplayName("Should backup file and return empty when categories file is corrupted")
    void testLoadCategoriesCorrupted() throws IOException {
        Path tempCatFile = Files.createTempFile("test_cats_", ".json");
        mockedAppConfig.when(AppConfig::getCategoriesFilePath).thenReturn(tempCatFile.toString());
        Files.writeString(tempCatFile, "invalid json {");
        Map<String, MovementCategory> loaded = handler.loadCategories();
        assertTrue(loaded.isEmpty());
        assertTrue(Files.exists(Paths.get(tempCatFile.toString() + ".bak")));
        Files.deleteIfExists(tempCatFile);
        Files.deleteIfExists(Paths.get(tempCatFile.toString() + ".bak"));
    }

    @Test
    @DisplayName("saveAccounts handles IOException")
    void testSaveAccountsIOException() {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.writeString(org.mockito.Mockito.any(), org.mockito.Mockito.any(CharSequence.class), org.mockito.Mockito.any()))
                       .thenCallRealMethod();
            mockedFiles.when(() -> Files.move(org.mockito.Mockito.any(), org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                       .thenThrow(new IOException("Simulated write error"));
            
            // Should not throw, should be caught and logged
            assertDoesNotThrow(() -> handler.saveAccounts(new ArrayList<>()));
        }
    }

    @Test
    @DisplayName("saveCategories handles IOException")
    void testSaveCategoriesIOException() throws IOException {
        Path tempCatFile = Files.createTempFile("test_cats_", ".json");
        mockedAppConfig.when(AppConfig::getCategoriesFilePath).thenReturn(tempCatFile.toString());
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.writeString(org.mockito.Mockito.any(), org.mockito.Mockito.any(CharSequence.class), org.mockito.Mockito.any()))
                       .thenCallRealMethod();
            mockedFiles.when(() -> Files.move(org.mockito.Mockito.any(), org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                       .thenThrow(new IOException("Simulated write error"));
            
            // Should not throw, should be caught and logged
            assertDoesNotThrow(() -> handler.saveCategories(new java.util.HashMap<>()));
        }
        Files.deleteIfExists(tempCatFile);
    }

    @Test
    @DisplayName("loadAccounts handles IOException")
    void testLoadAccountsIOException() throws IOException {
        Files.writeString(tempAccountsFile, "[]");
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readString(org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                    .thenThrow(new IOException("Simulated read error"));
            
            List<Account> loaded = handler.loadAccounts();
            assertTrue(loaded.isEmpty());
        }
    }

    @Test
    @DisplayName("loadCategories handles IOException")
    void testLoadCategoriesIOException() throws IOException {
        Path tempCatFile = Files.createTempFile("test_cats_", ".json");
        mockedAppConfig.when(AppConfig::getCategoriesFilePath).thenReturn(tempCatFile.toString());
        Files.writeString(tempCatFile, "[]");

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readString(org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                    .thenThrow(new IOException("Simulated read error"));
            
            Map<String, MovementCategory> loaded = handler.loadCategories();
            assertTrue(loaded.isEmpty());
        }
        Files.deleteIfExists(tempCatFile);
    }
    
    @Test
    @DisplayName("loadAccounts backup fails with IOException")
    void testLoadAccountsBackupIOException() throws IOException {
        Files.writeString(tempAccountsFile, "invalid json {");
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readString(org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                    .thenCallRealMethod();
            mockedFiles.when(() -> Files.copy((Path)org.mockito.Mockito.any(), (Path)org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                    .thenThrow(new IOException("Simulated copy error"));
            
            List<Account> loaded = handler.loadAccounts();
            assertTrue(loaded.isEmpty());
        }
    }

    @Test
    @DisplayName("loadCategories backup fails with IOException")
    void testLoadCategoriesBackupIOException() throws IOException {
        Path tempCatFile = Files.createTempFile("test_cats_", ".json");
        mockedAppConfig.when(AppConfig::getCategoriesFilePath).thenReturn(tempCatFile.toString());
        Files.writeString(tempCatFile, "invalid json {");

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readString(org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                    .thenCallRealMethod();
            mockedFiles.when(() -> Files.copy((Path)org.mockito.Mockito.any(), (Path)org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                    .thenThrow(new IOException("Simulated copy error"));
            
            Map<String, MovementCategory> loaded = handler.loadCategories();
            assertTrue(loaded.isEmpty());
        }
        Files.deleteIfExists(tempCatFile);
    }
}
