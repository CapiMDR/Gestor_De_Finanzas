package reminders.reminder_model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reminder Test")
class ReminderTest {

    @Test
    @DisplayName("Constructor with date assigns fields correctly")
    void testConstructorWithDate() {
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        Reminder reminder = new Reminder("Name", "Message", date);

        assertEquals("Name", reminder.getName());
        assertEquals("Message", reminder.getMessage());
        assertEquals(date, reminder.getDate());
        assertFalse(reminder.isTriggered());
    }

    @Test
    @DisplayName("Constructor without date assigns current date")
    void testConstructorWithoutDate() {
        Reminder reminder = new Reminder("Name", "Message");

        assertEquals("Name", reminder.getName());
        assertEquals("Message", reminder.getMessage());
        assertNotNull(reminder.getDate());
        assertFalse(reminder.isTriggered());
    }

    @Test
    @DisplayName("Setters update fields correctly")
    void testSetters() {
        Reminder reminder = new Reminder("Old", "OldMsg");
        reminder.setName("New");
        reminder.setMessage("NewMsg");
        reminder.setTriggered(true);

        assertEquals("New", reminder.getName());
        assertEquals("NewMsg", reminder.getMessage());
        assertTrue(reminder.isTriggered());
    }

    @Test
    @DisplayName("shouldTrigger returns true if date is past and not triggered")
    void testShouldTriggerTrue() {
        LocalDateTime past = LocalDateTime.now().minusMinutes(5);
        Reminder reminder = new Reminder("Name", "Message", past);
        
        assertTrue(reminder.shouldTrigger());
    }

    @Test
    @DisplayName("shouldTrigger returns false if date is future")
    void testShouldTriggerFalseFuture() {
        LocalDateTime future = LocalDateTime.now().plusMinutes(5);
        Reminder reminder = new Reminder("Name", "Message", future);
        
        assertFalse(reminder.shouldTrigger());
    }

    @Test
    @DisplayName("shouldTrigger returns false if already triggered")
    void testShouldTriggerFalseAlreadyTriggered() {
        LocalDateTime past = LocalDateTime.now().minusMinutes(5);
        Reminder reminder = new Reminder("Name", "Message", past);
        reminder.setTriggered(true);
        
        assertFalse(reminder.shouldTrigger());
    }

    @Test
    @DisplayName("toString format is correct")
    void testToString() {
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 10, 30);
        Reminder reminder = new Reminder("T", "M", date);

        String result = reminder.toString();
        assertEquals("Name: T Message: M Date 2025-01-01T10:30", result);
    }
}
