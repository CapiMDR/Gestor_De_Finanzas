package recurrings.recurring_model;

import config.AppConfig;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link RecurringsModel}.
 * Validates the core business logic for adding, removing, and updating
 * recurring movements. It uses Mockito (MockedStatic) to safely isolate
 * JSON file access during test execution.
 */
class RecurringsModelTest {

    private MockedStatic<AppConfig> mockedAppConfig;

    @TempDir
    Path tempDir;

    private RecurringsModel model;

    /**
     * Initializes the model with an isolated temporary JSON path
     * via Mockito static mocking on AppConfig.
     */
    @BeforeEach
    void setUp() {
        String tempFilePath = tempDir.resolve("recurrings_model_test.json").toString();
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getRecurringsFilePath).thenReturn(tempFilePath);
        
        model = new RecurringsModel();
    }

    @AfterEach
    void tearDown() {
        if (mockedAppConfig != null) {
            mockedAppConfig.close();
        }
    }

    /**
     * Verifies that adding a new recurring movement successfully
     * stores it inside the TreeSet and updates the state.
     */
    @Test
    void testAddRecurring() {
        MovementCategory cat = new MovementCategory("Cat", MovementType.EXPENSE);
        model.addRecurring("Test1", new BigDecimal("10"), "Desc", LocalDateTime.now(), RecurrenceType.Mensual, cat);
        
        assertEquals(1, model.getRecurrings().size());
        assertEquals("Test1", model.getRecurrings().first().getConcept());
    }

    /**
     * Verifies that an existing movement can be effectively deleted
     * from the internal collection.
     */
    @Test
    void testDeleteRecurring() {
        MovementCategory cat = new MovementCategory("Cat", MovementType.EXPENSE);
        RecurringMove move = new RecurringMove("Test2", new BigDecimal("10"), "Desc", LocalDateTime.now(), RecurrenceType.Mensual, cat);
        
        model.addRecurring(move);
        assertEquals(1, model.getRecurrings().size());
        
        model.deleteRecurring(move);
        assertEquals(0, model.getRecurrings().size());
    }

    /**
     * Tests the edit process which replaces the old movement instance
     * with the new updated version.
     */
    @Test
    void testEditRecurring() {
        MovementCategory cat = new MovementCategory("Cat", MovementType.EXPENSE);
        RecurringMove moveOld = new RecurringMove("Old", new BigDecimal("10"), "Desc", LocalDateTime.now(), RecurrenceType.Mensual, cat);
        RecurringMove moveNew = new RecurringMove("New", new BigDecimal("20"), "Desc", LocalDateTime.now(), RecurrenceType.Mensual, cat);
        
        model.addRecurring(moveOld);
        model.editRecurring(moveOld, moveNew);
        
        assertEquals(1, model.getRecurrings().size());
        assertEquals("New", model.getRecurrings().first().getConcept());
        assertEquals(new BigDecimal("20"), model.getRecurrings().first().getAmount());
    }

    /**
     * Validates the implementation of the Observer pattern.
     * Ensures registered observers are correctly notified when changes happen.
     */
    @Test
    void testObserverNotification() {
        AtomicBoolean notified = new AtomicBoolean(false);
        RecurringObserver observer = new RecurringObserver() {
            @Override
            public void observeRecurrings(TreeSet<RecurringMove> recurrings) {
                notified.set(true);
            }
        };
        
        model.addObserver(observer);
        MovementCategory cat = new MovementCategory("Cat", MovementType.EXPENSE);
        model.addRecurring("Test3", new BigDecimal("10"), "Desc", LocalDateTime.now(), RecurrenceType.Mensual, cat);
        
        assertTrue(notified.get(), "Observer should have been notified");
        
        model.removeObserver(observer);
        notified.set(false);
        model.addRecurring("Test4", new BigDecimal("10"), "Desc", LocalDateTime.now(), RecurrenceType.Mensual, cat);
        assertFalse(notified.get(), "Observer should NOT be notified after removal");
    }

    /**
     * Smoke test to ensure save operations delegate correctly to
     * the JSON handler without throwing unexpected errors.
     */
    @Test
    void testSaveRecurrings() {
        // Just verify it doesn't throw errors when saving to the temp file
        MovementCategory cat = new MovementCategory("Cat", MovementType.EXPENSE);
        model.addRecurring("SaveTest", new BigDecimal("10"), "Desc", LocalDateTime.now(), RecurrenceType.Mensual, cat);
        assertDoesNotThrow(() -> model.saveRecurrings());
    }
}
