package goals.goals_model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Goal Test")
class GoalTest {

    @Test
    @DisplayName("should initialize with default constructor")
    void testDefaultConstructor() {
        Goal goal = new Goal();
        assertEquals(BigDecimal.ZERO, goal.getCurrentAmount());
        assertFalse(goal.isNotificadaCompleta());
    }

    @Test
    @DisplayName("should set and get fields correctly")
    void testSettersAndGetters() {
        Goal goal = new Goal();
        goal.setName("Car");
        goal.setTargetAmount(new BigDecimal("50000"));
        goal.setCurrentAmount(new BigDecimal("1000"));
        goal.setDescription("Save for a car");
        goal.setNotificadaCompleta(true);

        assertEquals("Car", goal.getName());
        assertEquals(new BigDecimal("50000"), goal.getTargetAmount());
        assertEquals(new BigDecimal("1000"), goal.getCurrentAmount());
        assertEquals("Save for a car", goal.getDescription());
        assertTrue(goal.isNotificadaCompleta());
    }

    @Test
    @DisplayName("should format toString correctly")
    void testToString() {
        Goal goal = new Goal("House", new BigDecimal("100000"), "Save for house");
        assertEquals("House - Target: 100000", goal.toString());
    }
}
