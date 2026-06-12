package config;

import java.io.File;

/**
 * Central configuration class for the application.
 * Provides file paths for all persistent data files and ensures
 * the data directory exists before any read/write operations.
 *
 * All data is stored in a hidden folder inside the user's home directory:
 * {@code ~/.gestor-finanzas/} (e.g. C:\Users\[username]\.gestor-finanzas\ on Windows).
 *
 * This prevents path issues caused by relative paths and makes the app
 * distributable without depending on the working directory at runtime.
 */
public class AppConfig {

    /**
     * Root directory where all application data files are stored.
     * Resolves to {@code <user.home>/.gestor-finanzas/}.
     */
    private static final String DATA_DIR =
            System.getProperty("user.home") + File.separator + ".gestor-finanzas";

    /**
     * Private constructor — this class is not meant to be instantiated.
     */
    private AppConfig() {
    }

    /**
     * Returns the absolute path to the accounts data file.
     *
     * @return absolute path to {@code accounts_data.json}
     */
    public static String getAccountsFilePath() {
        return DATA_DIR + File.separator + "accounts_data.json";
    }

    /**
     * Returns the absolute path to the movement categories data file.
     *
     * @return absolute path to {@code categories_data.json}
     */
    public static String getCategoriesFilePath() {
        return DATA_DIR + File.separator + "categories_data.json";
    }

    /**
     * Returns the absolute path to the recurring movements data file.
     *
     * @return absolute path to {@code recurrings.json}
     */
    public static String getRecurringsFilePath() {
        return DATA_DIR + File.separator + "recurrings.json";
    }

    /**
     * Returns the absolute path to the reminders data file.
     *
     * @return absolute path to {@code reminders.json}
     */
    public static String getRemindersFilePath() {
        return DATA_DIR + File.separator + "reminders.json";
    }

    /**
     * Returns the absolute path to the user settings file.
     *
     * @return absolute path to {@code settings.json}
     */
    public static String getSettingsFilePath() {
        return DATA_DIR + File.separator + "settings.json";
    }

    /**
     * Returns the absolute path to the persisted unread notifications file.
     * Used to restore pending notifications across sessions when running in
     * foreground-only mode.
     *
     * @return absolute path to {@code notifications.json}
     */
    public static String getNotificationsFilePath() {
        return DATA_DIR + File.separator + "notifications.json";
    }

    /**
     * Ensures the data directory exists, creating it (and any missing parent
     * directories) if necessary. Should be called once during application startup
     * before any file read/write is attempted.
     */
    public static void ensureDataDirExists() {
        new File(DATA_DIR).mkdirs();
    }
}
