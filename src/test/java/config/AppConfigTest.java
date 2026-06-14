package config;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link AppConfig}.
 * Verifies that all configuration paths are correctly generated and that
 * the application data directory is successfully created.
 */
class AppConfigTest {

    /**
     * Tests that all file path generator methods return valid paths
     * pointing to the correct JSON files.
     */
    @Test
    void testFilePaths() {
        assertNotNull(AppConfig.getAccountsFilePath());
        assertTrue(AppConfig.getAccountsFilePath().endsWith("accounts_data.json"));

        assertNotNull(AppConfig.getCategoriesFilePath());
        assertTrue(AppConfig.getCategoriesFilePath().endsWith("categories_data.json"));

        assertNotNull(AppConfig.getRecurringsFilePath());
        assertTrue(AppConfig.getRecurringsFilePath().endsWith("recurrings.json"));

        assertNotNull(AppConfig.getRemindersFilePath());
        assertTrue(AppConfig.getRemindersFilePath().endsWith("reminders.json"));

        assertNotNull(AppConfig.getSettingsFilePath());
        assertTrue(AppConfig.getSettingsFilePath().endsWith("settings.json"));

        assertNotNull(AppConfig.getNotificationsFilePath());
        assertTrue(AppConfig.getNotificationsFilePath().endsWith("notifications.json"));
    }

    /**
     * Tests that ensureDataDirExists successfully creates the application
     * data directory in the user's home folder.
     */
    @Test
    void testEnsureDataDirExists() {
        AppConfig.ensureDataDirExists();
        String dataDir = System.getProperty("user.home") + File.separator + ".gestor-finanzas";
        File dir = new File(dataDir);
        assertTrue(dir.exists() && dir.isDirectory());
    }
}
