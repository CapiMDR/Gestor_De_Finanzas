package recurrings.recurring_model;

import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RecurringMove}.
 * Assures accurate getters, setters, triggering conditions based on
 * initial dates, and logic to spawn the next occurrence based on frequency.
 */
class RecurringMoveTest {

    /**
     * Verifies that the core state configuration persists through all properties.
     */
    @Test
    void testGettersAndSetters() {
        MovementCategory cat = new MovementCategory("Sub", MovementType.EXPENSE);
        RecurringMove move = new RecurringMove("Netflix", new BigDecimal("15.99"), "Mensual",
                LocalDateTime.of(2026, 1, 1, 10, 0), RecurrenceType.Mensual, cat);

        assertEquals("Netflix", move.getConcept());
        assertEquals(new BigDecimal("15.99"), move.getAmount());
        assertEquals("Mensual", move.getDescription());
        assertEquals(LocalDateTime.of(2026, 1, 1, 10, 0), move.getInitialDate());
        assertEquals(RecurrenceType.Mensual, move.getRecurrence());
        assertEquals(cat, move.getCategory());

        move.setConcept("Spotify");
        move.setAmount(new BigDecimal("9.99"));
        move.setDescription("Musica");
        move.setRecurrence(RecurrenceType.Diario);

        assertEquals("Spotify", move.getConcept());
        assertEquals(new BigDecimal("9.99"), move.getAmount());
        assertEquals("Musica", move.getDescription());
        assertEquals(RecurrenceType.Diario, move.getRecurrence());
        
        assertTrue(move.toString().contains("Spotify"));
    }

    /**
     * Validates logic surrounding when a reminder is due to fire based on
     * its start date, and that it doesn't fire repeatedly if already triggered.
     */
    @Test
    void testShouldTrigger() {
        MovementCategory cat = new MovementCategory("Sub", MovementType.EXPENSE);
        // Past date
        RecurringMove move1 = new RecurringMove("Past", BigDecimal.TEN, "Desc",
                LocalDateTime.of(2026, 6, 15, 10, 0).minusDays(1), RecurrenceType.Diario, cat);
        assertTrue(move1.shouldTrigger());

        move1.setTriggered(true);
        assertFalse(move1.shouldTrigger(), "Should not trigger if already triggered");

        // Future date
        RecurringMove move2 = new RecurringMove("Future", BigDecimal.TEN, "Desc",
                LocalDateTime.of(2026, 6, 15, 10, 0).plusDays(1), RecurrenceType.Diario, cat);
        assertFalse(move2.shouldTrigger(), "Should not trigger future dates");
    }

    /**
     * Tests the generation of the next occurrence instance across all
     * available recurrence types (Daily, Weekly, Monthly, etc.).
     */
    @Test
    void testCreateNextOccurrence() {
        MovementCategory cat = new MovementCategory("Sub", MovementType.EXPENSE);
        LocalDateTime baseDate = LocalDateTime.of(2026, 1, 1, 10, 0);

        // Diario
        RecurringMove diario = new RecurringMove("C", BigDecimal.TEN, "D", baseDate, RecurrenceType.Diario, cat);
        RecurringMove nextDiario = diario.createNextOccurrence();
        assertEquals(baseDate.plusDays(1), nextDiario.getInitialDate());

        // Semanal
        RecurringMove semanal = new RecurringMove("C", BigDecimal.TEN, "D", baseDate, RecurrenceType.Semanal, cat);
        assertEquals(baseDate.plusWeeks(1), semanal.createNextOccurrence().getInitialDate());

        // Quincenal
        RecurringMove quincenal = new RecurringMove("C", BigDecimal.TEN, "D", baseDate, RecurrenceType.Quincenal, cat);
        assertEquals(baseDate.plusWeeks(2), quincenal.createNextOccurrence().getInitialDate());

        // Mensual
        RecurringMove mensual = new RecurringMove("C", BigDecimal.TEN, "D", baseDate, RecurrenceType.Mensual, cat);
        assertEquals(baseDate.plusMonths(1), mensual.createNextOccurrence().getInitialDate());

        // Anual
        RecurringMove anual = new RecurringMove("C", BigDecimal.TEN, "D", baseDate, RecurrenceType.Anual, cat);
        assertEquals(baseDate.plusYears(1), anual.createNextOccurrence().getInitialDate());
    }
}

