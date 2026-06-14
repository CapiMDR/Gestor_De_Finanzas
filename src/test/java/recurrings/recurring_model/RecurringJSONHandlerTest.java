package recurrings.recurring_model;

import config.AppConfig;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link RecurringJSONHandler}.
 * Uses Mockito (MockedStatic) to intercept calls to AppConfig and redirect
 * the JSON file persistence to a temporary directory for safe testing.
 */
class RecurringJSONHandlerTest {

    private MockedStatic<AppConfig> mockedAppConfig;

    @TempDir
    Path tempDir;

    private String tempFilePath;

    /**
     * Sets up a temporary file path and mocks AppConfig statically
     * before each test execution to prevent writing to the real user data.
     */
    @BeforeEach
    void setUp() {
        tempFilePath = tempDir.resolve("recurrings_test.json").toString();
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getRecurringsFilePath).thenReturn(tempFilePath);
    }

    @AfterEach
    void tearDown() {
        if (mockedAppConfig != null) {
            mockedAppConfig.close();
        }
    }

    /**
     * Tests the serialization and deserialization of recurring movements.
     * Validates that the loaded data exactly matches the saved data.
     *
     * @throws IOException if the file operations fail
     */
    @Test
    void testSaveAndLoadRecurrings() throws IOException {
        TreeSet<RecurringMove> moves = new TreeSet<>(RecurringJSONHandler.REMINDER_COMPARATOR);
        MovementCategory cat = new MovementCategory("Suscripciones", MovementType.EXPENSE);
        RecurringMove move1 = new RecurringMove("Netflix", new BigDecimal("15.99"), "Pago mensual",
                LocalDateTime.of(2026, 1, 1, 10, 0), RecurrenceType.Mensual, cat);
        
        moves.add(move1);

        // Save
        RecurringJSONHandler.saveReminders(moves);
        assertTrue(new File(tempFilePath).exists());

        // Load
        TreeSet<RecurringMove> loaded = RecurringJSONHandler.loadRecurrings();
        assertEquals(1, loaded.size());

        RecurringMove loadedMove = loaded.first();
        assertEquals("Netflix", loadedMove.getConcept());
        assertEquals(new BigDecimal("15.99"), loadedMove.getAmount());
        assertEquals("Pago mensual", loadedMove.getDescription());
        assertEquals(RecurrenceType.Mensual, loadedMove.getRecurrence());
        assertEquals("Suscripciones", loadedMove.getCategory().getName());
        assertEquals(MovementType.EXPENSE, loadedMove.getCategory().getType());
    }

    /**
     * Tests that attempting to load from a non-existent JSON file
     * gracefully returns an empty collection instead of crashing.
     */
    @Test
    void testLoadEmptyFile() {
        TreeSet<RecurringMove> loaded = RecurringJSONHandler.loadRecurrings();
        assertTrue(loaded.isEmpty(), "Loading a non-existent file should return an empty set");
    }
}
