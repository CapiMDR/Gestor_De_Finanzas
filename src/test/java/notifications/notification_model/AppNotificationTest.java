package notifications.notification_model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppNotification Test")
class AppNotificationTest {

    @Test
    @DisplayName("should initialize correctly and format timestamp")
    void testInitializationAndFormatting() {
        LocalDateTime dt = LocalDateTime.of(2025, java.time.Month.JANUARY, 1, 14, 30);
        AppNotification notification = new AppNotification(AppNotification.Tipo.RECORDATORIO, "Title", "Body", dt);

        assertEquals(AppNotification.Tipo.RECORDATORIO, notification.getTipo());
        assertEquals("Title", notification.getTitulo());
        assertEquals("Body", notification.getCuerpo());
        assertEquals(dt, notification.getTimestamp());
        assertFalse(notification.isLeida());

        assertEquals("01/01/2025 14:30", notification.getTimestampFormateado());
    }

    @Test
    @DisplayName("should mark as read")
    void testMarcar() {
        AppNotification notification = new AppNotification(AppNotification.Tipo.RECORDATORIO, "Title", "Body", LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0));
        assertFalse(notification.isLeida());
        notification.marcar();
        assertTrue(notification.isLeida());
    }

    @Test
    @DisplayName("should return correct icon literal")
    void testGetIconoLiteral() {
        AppNotification n1 = new AppNotification(AppNotification.Tipo.RECORDATORIO, "", "", LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0));
        AppNotification n2 = new AppNotification(AppNotification.Tipo.META_CUMPLIDA, "", "", LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0));
        AppNotification n3 = new AppNotification(AppNotification.Tipo.RECURRENTE_VENCIDO, "", "", LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0));

        assertEquals("mdi2b-bell-ring", n1.getIconoLiteral());
        assertEquals("mdi2t-trophy", n2.getIconoLiteral());
        assertEquals("mdi2c-calendar-clock", n3.getIconoLiteral());
    }

    @Test
    @DisplayName("should format toString correctly")
    void testToString() {
        LocalDateTime dt = LocalDateTime.of(2025, java.time.Month.JANUARY, 1, 14, 30);
        AppNotification notification = new AppNotification(AppNotification.Tipo.RECORDATORIO, "Test", "Body", dt);
        assertEquals("[RECORDATORIO] Test — 01/01/2025 14:30", notification.toString());
    }
}

