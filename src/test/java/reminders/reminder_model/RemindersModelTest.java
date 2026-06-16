package reminders.reminder_model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RemindersModel Test")
class RemindersModelTest {

    @Mock
    private ReminderObserver mockObserver;

    @Captor
    private ArgumentCaptor<SortedSet<Reminder>> remindersCaptor;

    private RemindersModel remindersModel;
    private SortedSet<Reminder> initialReminders;

    @BeforeEach
    void setUp() {
        initialReminders = new TreeSet<>(ReminderJSONHandler.reminderComparator);
        // We must mock the static load method before creating the model
        try (MockedStatic<ReminderJSONHandler> mockedHandler = mockStatic(ReminderJSONHandler.class)) {
            mockedHandler.when(ReminderJSONHandler::loadReminders).thenReturn(initialReminders);
            remindersModel = new RemindersModel();
        }
        remindersModel.addObserver(mockObserver);
    }

    @Test
    @DisplayName("addReminder(String, String, LocalDateTime) should add to set and notify")
    void testAddReminderByFields() {
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        remindersModel.addReminder("Test Name", "Test Msg", date);

        verify(mockObserver).observeReminders(remindersCaptor.capture());
        SortedSet<Reminder> captured = remindersCaptor.getValue();
        assertEquals(1, captured.size());
        Reminder r = captured.first();
        assertEquals("Test Name", r.getName());
        assertEquals("Test Msg", r.getMessage());
        assertEquals(date, r.getDate());
    }

    @Test
    @DisplayName("addReminder(Reminder) should add to set and notify")
    void testAddReminderByObject() {
        Reminder r = new Reminder("A", "B", LocalDateTime.now());
        remindersModel.addReminder(r);

        verify(mockObserver).observeReminders(remindersCaptor.capture());
        assertTrue(remindersCaptor.getValue().contains(r));
        assertEquals(1, remindersModel.getReminders().size());
    }

    @Test
    @DisplayName("deleteReminder should remove and notify if present")
    void testDeleteReminder() {
        Reminder r = new Reminder("A", "B", LocalDateTime.now());
        remindersModel.addReminder(r);
        clearInvocations(mockObserver);

        remindersModel.deleteReminder(r);
        verify(mockObserver).observeReminders(remindersCaptor.capture());
        assertTrue(remindersCaptor.getValue().isEmpty());
    }

    @Test
    @DisplayName("deleteReminder should notify even if not present, but state is unchanged")
    void testDeleteReminderNotPresent() {
        Reminder r = new Reminder("A", "B", LocalDateTime.now());
        remindersModel.deleteReminder(r);
        verify(mockObserver).observeReminders(remindersCaptor.capture());
        assertTrue(remindersCaptor.getValue().isEmpty());
    }

    @Test
    @DisplayName("editReminder should remove old, add new, and notify")
    void testEditReminder() {
        Reminder oldR = new Reminder("Old", "Msg", LocalDateTime.now());
        Reminder newR = new Reminder("New", "Msg", LocalDateTime.now());
        remindersModel.addReminder(oldR);
        clearInvocations(mockObserver);

        remindersModel.editReminder(oldR, newR);
        verify(mockObserver, times(3)).observeReminders(remindersCaptor.capture()); // delete, add, and edit explicitly
        SortedSet<Reminder> captured = remindersCaptor.getValue();
        assertFalse(captured.contains(oldR));
        assertTrue(captured.contains(newR));
    }

    @Test
    @DisplayName("editReminder should do nothing if old reminder is not present")
    void testEditReminderNotPresent() {
        Reminder oldR = new Reminder("Old", "Msg", LocalDateTime.now());
        Reminder newR = new Reminder("New", "Msg", LocalDateTime.now());
        
        remindersModel.editReminder(oldR, newR);
        verifyNoInteractions(mockObserver);
    }

    @Test
    @DisplayName("removeObserver should stop notifications for that observer")
    void testRemoveObserver() {
        remindersModel.removeObserver(mockObserver);
        remindersModel.addReminder("N", "M", LocalDateTime.now());
        verifyNoInteractions(mockObserver);
    }

    @Test
    @DisplayName("removeObserver should not crash if observer not registered")
    void testRemoveObserverNotPresent() {
        ReminderObserver tempObs = mock(ReminderObserver.class);
        remindersModel.removeObserver(tempObs); // Should do nothing
    }

    @Test
    @DisplayName("saveReminders should call JSON handler static method")
    void testSaveReminders() {
        try (MockedStatic<ReminderJSONHandler> mockedHandler = mockStatic(ReminderJSONHandler.class)) {
            // Need to stub loadReminders again because it gets called when we instantiated the model in setUp? 
            // No, model is already instantiated.
            remindersModel.saveReminders();
            mockedHandler.verify(() -> ReminderJSONHandler.saveReminders(remindersModel.getReminders()));
        }
    }
}
