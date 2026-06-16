package reminders.reminder_model;

import config.AppConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.SortedSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link ReminderJSONHandler}.
 * Leverages Mockito's MockedStatic to intercept file paths from AppConfig,
 * guaranteeing that tests run on a safe, temporary JSON file.
 */
class ReminderJSONHandlerTest {

    private MockedStatic<AppConfig> mockedAppConfig;

    @TempDir
    Path tempDir;

    private String tempFilePath;

    /**
     * Set up temporary directory and mock static configuration paths.
     */
    @BeforeEach
    void setUp() {
        tempFilePath = tempDir.resolve("reminders_test.json").toString();
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getRemindersFilePath).thenReturn(tempFilePath);
    }

    @AfterEach
    void tearDown() {
        if (mockedAppConfig != null) {
            mockedAppConfig.close();
        }
    }

    /**
     * Validates that a TreeSet of Reminders can be saved to disk
     * and accurately re-loaded back into memory.
     *
     * @throws IOException if read/write fails
     */
    @Test
    void testSaveAndLoadReminders() throws IOException {
        TreeSet<Reminder> reminders = new TreeSet<>(ReminderJSONHandler.reminderComparator);
        Reminder r1 = new Reminder("Cita medica", "Ir al doctor", LocalDateTime.of(2026, 6, 15, 10, 0));
        r1.setTriggered(true);
        reminders.add(r1);

        // Save
        ReminderJSONHandler.saveReminders(reminders);
        assertTrue(new File(tempFilePath).exists());

        // Load
        SortedSet<Reminder> loaded = ReminderJSONHandler.loadReminders();
        assertEquals(1, loaded.size());

        Reminder loadedReminder = loaded.first();
        assertEquals("Cita medica", loadedReminder.getName());
        assertEquals("Ir al doctor", loadedReminder.getMessage());
        assertEquals(LocalDateTime.of(2026, 6, 15, 10, 0), loadedReminder.getDate());
        assertTrue(loadedReminder.isTriggered());
    }

    /**
     * Ensures robustness by checking that loading an empty or non-existent
     * JSON file safely returns an empty set instead of an exception.
     */
    @Test
    void testLoadEmptyFile() {
        SortedSet<Reminder> loaded = ReminderJSONHandler.loadReminders();
        assertTrue(loaded.isEmpty(), "Loading a non-existent file should return an empty set");
    }

    @Test
    void testLoadCorruptedFile() throws IOException {
        java.nio.file.Files.writeString(Path.of(tempFilePath), "invalid json {");
        SortedSet<Reminder> loaded = ReminderJSONHandler.loadReminders();
        assertTrue(loaded.isEmpty());
        assertTrue(new File(tempFilePath + ".bak").exists());
    }
}
