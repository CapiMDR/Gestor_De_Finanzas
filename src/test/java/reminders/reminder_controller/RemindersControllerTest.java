package reminders.reminder_controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.TreeSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import notifications.notification_controller.NotificationManager;
import reminders.reminder_model.Reminder;
import reminders.reminder_model.ReminderJSONHandler;
import reminders.reminder_model.RemindersModel;
import reminders.reminder_view.RemindersViewFX;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RemindersController Test")
class RemindersControllerTest {

    @Mock
    private RemindersModel mockModel;

    @Mock
    private ScheduledExecutorService mockScheduler;

    @Mock
    private RemindersViewFX mockView;

    private RemindersController controller;
    private MockedStatic<NotificationManager> mockedNotificationManager;

    @BeforeAll
    static void initJFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Ignored
        }
    }

    @BeforeEach
    void setUp() {
        mockedNotificationManager = mockStatic(NotificationManager.class);
        controller = new RemindersController(mockModel, mockScheduler);
        controller.setView(mockView);
    }

    @AfterEach
    void tearDown() {
        mockedNotificationManager.close();
    }

    @Test
    @DisplayName("should schedule watch task on initialization")
    void testInitialization() {
        verify(mockScheduler).scheduleAtFixedRate(any(Runnable.class), eq(0L), eq(1L), eq(TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("should add reminder to model and refresh view")
    void testAddReminder() {
        LocalDateTime dt = LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0).plusDays(1);
        controller.handleReminderAddition("Test Add", "Msg", dt);
        
        verify(mockModel).addReminder("Test Add", "Msg", dt);
        verify(mockModel).saveReminders();
    }

    @Test
    @DisplayName("should delete reminder from model and refresh view")
    void testDeleteReminder() {
        Reminder r = new Reminder("Test Delete", "Msg", LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0));
        controller.handleReminderDeletion(r);
        
        verify(mockModel).deleteReminder(r);
        verify(mockModel).saveReminders();
    }

    @Test
    @DisplayName("should edit reminder and refresh view")
    void testEditReminder() {
        Reminder oldR = new Reminder("Old", "Msg", LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0));
        Reminder newR = new Reminder("New", "Msg", LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0).plusDays(1));
        
        controller.handleReminderEdit(oldR, newR);
        
        verify(mockModel).editReminder(oldR, newR);
        verify(mockModel).saveReminders();
    }

    @Test
    @DisplayName("should trigger past reminders when watch task runs")
    @SuppressWarnings("java:S5973")
    void testWatchRemindersTriggersPastReminders() throws Exception {
        Reminder pastReminder = new Reminder("Past", "Msg", LocalDateTime.now().minusMinutes(5)); // NOSONAR
        Reminder futureReminder = new Reminder("Future", "Msg", LocalDateTime.now().plusDays(1)); // NOSONAR
        
        TreeSet<Reminder> set = new TreeSet<>(ReminderJSONHandler.reminderComparator);
        set.add(pastReminder);
        set.add(futureReminder);
        when(mockModel.getReminders()).thenReturn(set);
        
        // Use reflection to run the private watch task
        java.lang.reflect.Method method = RemindersController.class.getDeclaredMethod("watchReminders");
        method.setAccessible(true);
        method.invoke(controller);
        
        assertTrue(pastReminder.isTriggered());
        assertFalse(futureReminder.isTriggered());
        
        verify(mockModel).saveReminders();
    }
}

