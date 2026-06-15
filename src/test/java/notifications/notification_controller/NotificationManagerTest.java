package notifications.notification_controller;

import config.AppConfig;
import notifications.notification_model.AppNotification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationManager}.
 * Verifies adding, reading, clearing, and persistence of notifications.
 */
@DisplayName("NotificationManager Test")
@SuppressWarnings("java:S5973")
class NotificationManagerTest {

    private NotificationManager manager;
    private MockedStatic<AppConfig> mockedAppConfig;
    private Path tempNotificationFile;

    private boolean callbackFired;

    @BeforeEach
    void setUp() throws Exception {
        // Reset the singleton state (since it persists across tests)
        manager = NotificationManager.getInstance();
        manager.eliminarTodas();
        manager.setCallbackNuevaNotificacion(null);

        callbackFired = false;

        // Mock the file path to use a temporary file for persistence testing
        tempNotificationFile = Files.createTempFile("test_notifications", ".json");
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getNotificationsFilePath)
                       .thenReturn(tempNotificationFile.toAbsolutePath().toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        mockedAppConfig.close();
        Files.deleteIfExists(tempNotificationFile);
    }

    @Test
    @DisplayName("should add notification and fire callback")
    void testAgregarNotificacion() {
        // Arrange
        manager.setCallbackNuevaNotificacion(() -> callbackFired = true);
        AppNotification notification = new AppNotification(AppNotification.Tipo.RECORDATORIO, "Test", "Test body", LocalDateTime.now());

        // Act
        manager.agregarNotificacion(notification);

        // Assert
        assertEquals(1, manager.getPendientes().size());
        assertEquals(1, manager.getConteoNoLeidas());
        assertTrue(callbackFired);
    }

    @Test
    @DisplayName("should return notifications sorted by newest first")
    void testGetPendientesSorted() throws InterruptedException {
        // Arrange
        AppNotification oldNotification = new AppNotification(AppNotification.Tipo.RECORDATORIO, "Old", "Body", LocalDateTime.now());
        // Ensure timestamp is strictly different
        Thread.sleep(10);
        AppNotification newNotification = new AppNotification(AppNotification.Tipo.META_CUMPLIDA, "New", "Body", LocalDateTime.now());

        // Act
        manager.agregarNotificacion(oldNotification);
        manager.agregarNotificacion(newNotification);

        // Assert
        List<AppNotification> list = manager.getPendientes();
        assertEquals("New", list.get(0).getTitulo());
        assertEquals("Old", list.get(1).getTitulo());
    }

    @Test
    @DisplayName("should correctly count unread notifications")
    void testGetConteoNoLeidas() {
        // Arrange
        AppNotification n1 = new AppNotification(AppNotification.Tipo.RECORDATORIO, "T1", "C1", LocalDateTime.now());
        AppNotification n2 = new AppNotification(AppNotification.Tipo.RECORDATORIO, "T2", "C2", LocalDateTime.now());
        manager.agregarNotificacion(n1);
        manager.agregarNotificacion(n2);

        // Act
        n1.marcar(); // Mark as read

        // Assert
        assertEquals(1, manager.getConteoNoLeidas());
    }

    @Test
    @DisplayName("should mark all as read and reset unread count")
    void testMarcarTodasLeidas() {
        // Arrange
        manager.setCallbackNuevaNotificacion(() -> callbackFired = true);
        manager.agregarNotificacion(new AppNotification(AppNotification.Tipo.RECORDATORIO, "T1", "C1", LocalDateTime.now()));
        manager.agregarNotificacion(new AppNotification(AppNotification.Tipo.RECORDATORIO, "T2", "C2", LocalDateTime.now()));
        callbackFired = false; // Reset after additions

        // Act
        manager.marcarTodasLeidas();

        // Assert
        assertEquals(0, manager.getConteoNoLeidas());
        assertTrue(callbackFired);
    }

    @Test
    @DisplayName("should save and load unread notifications from file")
    void testPersistence() {
        // Arrange
        AppNotification n1 = new AppNotification(AppNotification.Tipo.RECORDATORIO, "Unread", "Body", LocalDateTime.now());
        AppNotification n2 = new AppNotification(AppNotification.Tipo.META_CUMPLIDA, "Read", "Body", LocalDateTime.now());
        n2.marcar(); // Mark n2 as read

        manager.agregarNotificacion(n1);
        manager.agregarNotificacion(n2);

        // Act - Save
        manager.guardarPendientes();

        // Clear in-memory list
        manager.eliminarTodas();
        assertEquals(0, manager.getPendientes().size());

        // Act - Load
        manager.cargarPendientes();

        // Assert - Only the unread notification should be loaded
        assertEquals(1, manager.getPendientes().size());
        assertEquals("Unread", manager.getPendientes().get(0).getTitulo());
    }
}
