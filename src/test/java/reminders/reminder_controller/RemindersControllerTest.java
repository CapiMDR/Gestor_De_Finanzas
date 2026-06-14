package reminders.reminder_controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import reminders.reminder_model.Reminder;
import reminders.reminder_model.RemindersModel;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RemindersController}.
 * Verifies the addition, validation, and triggering of scheduled reminders.
 */
@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
@DisplayName("RemindersController Test")
@SuppressWarnings("java:S5973")
class RemindersControllerTest {

    @Mock
    private RemindersModel remindersModel;

    @Mock
    private ScheduledExecutorService scheduler;

    private RemindersController controller;

    @Captor
    private ArgumentCaptor<Runnable> taskCaptor;

    /**
     * Initializes the controller with the mocked dependencies before each test.
     */
    @BeforeEach
    void setUp() {
        controller = new RemindersController(remindersModel, scheduler);
    }

    /**
     * Tests that a reminder is rejected and not saved if its name is empty.
     */
    @Test
    @DisplayName("should not add reminder if name is empty")
    void testHandleReminderAdditionWithEmptyName() {
        // Act
        controller.handleReminderAddition("", "Mensaje", LocalDateTime.now().plusDays(1));

        // Assert
        verify(remindersModel, never()).addReminder(anyString(), anyString(), any(LocalDateTime.class));
        verify(remindersModel, never()).saveReminders();
    }

    /**
     * Tests that a reminder with valid data is successfully added to the model
     * and that the storage is subsequently saved.
     */
    @Test
    @DisplayName("should add and save reminder with valid data")
    void testHandleReminderAdditionValid() {
        // Arrange
        String name = "Doctor";
        String message = "Cita a las 5";
        LocalDateTime date = LocalDateTime.now().plusDays(1);

        // Act
        controller.handleReminderAddition(name, message, date);

        // Assert
        verify(remindersModel, times(1)).addReminder(name, message, date);
        verify(remindersModel, times(1)).saveReminders();
    }

    /**
     * Tests that the scheduled background task correctly identifies and triggers
     * reminders that have reached or passed their scheduled time.
     */
    @Test
    @DisplayName("scheduler should trigger expired reminders")
    void testWatchRemindersNotifiesExpired() {
        // Arrange
        // Capture the scheduled task (watchReminders) from constructor
        verify(scheduler).scheduleAtFixedRate(taskCaptor.capture(), eq(0L), eq(1L), eq(TimeUnit.SECONDS));
        Runnable watchTask = taskCaptor.getValue();

        // Create a reminder that is already expired (date in the past) and not yet triggered
        Reminder expiredReminder = new Reminder("Expirado", "Ya pasó", LocalDateTime.now().minusMinutes(5));
        expiredReminder.setTriggered(false);

        java.util.TreeSet<Reminder> mockSet = new java.util.TreeSet<>(java.util.Comparator.comparing(Reminder::getDate));
        mockSet.add(expiredReminder);
        when(remindersModel.getReminders()).thenReturn(mockSet);

        // Act
        // Manually run the task that the scheduler would run
        watchTask.run();

        // Assert
        // The reminder should be marked as triggered and saveReminders called
        assertTrue(expiredReminder.isTriggered());
        verify(remindersModel, times(1)).saveReminders();
    }
}
