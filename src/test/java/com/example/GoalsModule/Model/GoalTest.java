package com.example.GoalsModule.Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Goal model.
 * Verifies data encapsulation and constructor logic.
 * @author Jose Pablo Martinez
 */

class GoalTest {

    @Test
    @DisplayName("Should initialize correctly with default constructor")
    void testDefaultConstructor() {
        Goal goal = new Goal();
        assertEquals(BigDecimal.ZERO, goal.getCurrentAmount(), "Current amount should initialize to zero");
        assertNull(goal.getName());
    }

    @Test
    @DisplayName("Should initialize correctly with parameterized constructor")
    void testParameterizedConstructor() {
        BigDecimal target = new BigDecimal("5000.00");
        Goal goal = new Goal("PC Gamer", target, "Saving for RTX");

        assertEquals("PC Gamer", goal.getName());
        assertEquals(target, goal.getTargetAmount());
        assertEquals("Saving for RTX", goal.getDescription());
        assertEquals(BigDecimal.ZERO, goal.getCurrentAmount());
    }

    @Test
    @DisplayName("Should set and get values correctly")
    void testSettersAndGetters() {
        Goal goal = new Goal();
        BigDecimal target = new BigDecimal("100.00");
        BigDecimal current = new BigDecimal("20.00");

        goal.setName("Test");
        goal.setTargetAmount(target);
        goal.setCurrentAmount(current);
        goal.setDescription("Desc");

        assertEquals("Test", goal.getName());
        assertEquals(target, goal.getTargetAmount());
        assertEquals(current, goal.getCurrentAmount());
        assertEquals("Desc", goal.getDescription());
    }
    
    @Test
    @DisplayName("ToString should return formatted string")
    void testToString() {
        Goal goal = new Goal("Test", new BigDecimal("100"), "Desc");
        String result = goal.toString();
        assertTrue(result.contains("Test"));
        assertTrue(result.contains("100"));
    }
}