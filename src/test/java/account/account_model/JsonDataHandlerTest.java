package account.account_model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

import accounts.account_model.Account;
import accounts.account_model.JsonDataHandler;
import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;

public class JsonDataHandlerTest {

    private static final String FILE_PATH = "accounts_data.json";
    private static final String BACKUP_FILE_PATH = "accounts_data.json.bak";

    private JsonDataHandler dataHandler;

    @BeforeEach
    void setUp() {
        dataHandler = new JsonDataHandler();
        // Create Backup
        backupOriginalFile();
    }

    @AfterEach
    void tearDown() {

        restoreOriginalFile();
        cleanUp(BACKUP_FILE_PATH);
    }

    private void backupOriginalFile() {
        File original = new File(FILE_PATH);
        if (original.exists()) {
            try {
                Path originalPath = Paths.get(FILE_PATH);
                Path backupPath = Paths.get(BACKUP_FILE_PATH);
                Files.copy(originalPath, backupPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("CRITICAL: The backup file could not be created", e);
            }
        }
    }

    private void restoreOriginalFile() {
        File backup = new File(BACKUP_FILE_PATH);
        if (backup.exists()) {
            try {
                Path originalPath = Paths.get(FILE_PATH);
                Path backupPath = Paths.get(BACKUP_FILE_PATH);
                Files.copy(backupPath, originalPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("CRITICAL: Failure to restore the original file. CHECK: " + FILE_PATH);
                e.printStackTrace();
            }
        } else {
            cleanUp(FILE_PATH);
        }
    }

    private void cleanUp(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                System.err.println("The temporary file could not be deleted: " + path);
            }
        }
    }

    @Test
    void testSaveAndLoadFullAccountData_Integration() {
        Account account = new Account(1, "Billetera", AccountType.CASH, Coin.MXN, new BigDecimal("500.00"));
        MovementCategory incomeCat = new MovementCategory("Sueldo", MovementType.INCOME);
        Movement movement = new Movement(101, "Pago", new BigDecimal("1500.00"), incomeCat, account);

        account.addMovement(movement);

        List<Account> originalAccounts = new ArrayList<>();
        originalAccounts.add(account);

        cleanUp(FILE_PATH);

        dataHandler.saveAccounts(originalAccounts);

        List<Account> loadedAccounts = dataHandler.loadAccounts();

        assertEquals(1, loadedAccounts.size(), "must be 1 account loaded");

        Account loadedAccount = loadedAccounts.get(0);

        assertEquals(account.getName(), loadedAccount.getName());
        assertTrue(account.getCurrentBalance().compareTo(loadedAccount.getCurrentBalance()) == 0,
                "500 + 1500 = 2000");

        assertEquals(1, loadedAccount.getMovements().size(), "The account must have 1 movement loaded");

        Movement loadedMovement = loadedAccount.getMovements().get(0);
        assertEquals(movement.getDescription(), loadedMovement.getDescription());
        assertTrue(movement.getAmount().compareTo(loadedMovement.getAmount()) == 0,
                "The amount of the transaction must match");
        assertEquals(movement.getCategory().getName(), loadedMovement.getCategory().getName());

        assertEquals(loadedAccount.getId(), loadedMovement.getAccount().getId(),
                "The account referenced in the transaction must be the account loaded");
    }
}