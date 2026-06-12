package notifications.notification_controller;

import notifications.notification_model.AppNotification;

import config.AppConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton that centralizes all in-app notifications.
 *
 * Responsibilities:
 * - Receives new {@link AppNotification} objects from any module
 *   (reminders, goals, recurring payments).
 * - Fires a UI callback so the bell badge count updates in real-time.
 * - Persists unread notifications to {@code notifications.json} on shutdown
 *   and reloads them on next launch.
 *
 * Usage:
 * <pre>
 *     NotificationManager nm = NotificationManager.getInstance();
 *     nm.setCallbackNuevaNotificacion(() -> Platform.runLater(this::updateBadge));
 *     nm.agregarNotificacion(new AppNotification(...));
 * </pre>
 */
public class NotificationManager {

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static final NotificationManager INSTANCE = new NotificationManager();

    private NotificationManager() {}

    /** @return the single application-wide instance */
    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    // ── State ─────────────────────────────────────────────────────────────────

    private final List<AppNotification> pendientes = new ArrayList<>();
    private Runnable callbackNuevaNotificacion;

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Adds a new notification to the pending list and fires the UI callback.
     *
     * @param notificacion the notification to add
     */
    public synchronized void agregarNotificacion(AppNotification notificacion) {
        pendientes.add(notificacion);
        logger.info("Nueva notificación: {} — {}", notificacion.getTipo(), notificacion.getTitulo());
        if (callbackNuevaNotificacion != null) {
            callbackNuevaNotificacion.run();
        }
    }

    /**
     * Returns an unmodifiable view of all pending (unread) notifications,
     * sorted by timestamp descending (newest first).
     *
     * @return unmodifiable list of pending notifications
     */
    public synchronized List<AppNotification> getPendientes() {
        List<AppNotification> copia = new ArrayList<>(pendientes);
        copia.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return Collections.unmodifiableList(copia);
    }

    /**
     * Returns the count of unread notifications, used to update the bell badge.
     *
     * @return number of unread notifications
     */
    public synchronized int getConteoNoLeidas() {
        return (int) pendientes.stream().filter(n -> !n.isLeida()).count();
    }

    /**
     * Marks all pending notifications as read and fires the UI callback
     * so the badge resets to zero.
     */
    public synchronized void marcarTodasLeidas() {
        pendientes.forEach(AppNotification::marcar);
        if (callbackNuevaNotificacion != null) {
            callbackNuevaNotificacion.run();
        }
    }

    public synchronized void eliminarNotificacion(AppNotification notificacion) {
        pendientes.remove(notificacion);
        if (callbackNuevaNotificacion != null) {
            callbackNuevaNotificacion.run();
        }
    }

    public synchronized void eliminarTodas() {
        pendientes.clear();
        if (callbackNuevaNotificacion != null) {
            callbackNuevaNotificacion.run();
        }
    }

    /**
     * Registers the Runnable that the UI will use to refresh the bell badge
     * whenever a new notification arrives or all are marked read.
     *
     * @param callback a {@code Runnable} that updates the badge label;
     *                 should dispatch to the JavaFX thread via {@code Platform.runLater}
     */
    public void setCallbackNuevaNotificacion(Runnable callback) {
        this.callbackNuevaNotificacion = callback;
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    /**
     * Saves all unread notifications to {@code notifications.json} so they
     * survive application restarts when running in foreground-only mode.
     * Should be called during the application shutdown hook.
     */
    public synchronized void guardarPendientes() {
        JSONArray array = new JSONArray();
        for (AppNotification n : pendientes) {
            if (!n.isLeida()) {
                JSONObject obj = new JSONObject();
                obj.put("tipo", n.getTipo().name());
                obj.put("titulo", n.getTitulo());
                obj.put("cuerpo", n.getCuerpo());
                obj.put("timestamp", n.getTimestamp().toString());
                array.put(obj);
            }
        }

        Path target = Paths.get(AppConfig.getNotificationsFilePath());
        Path temp = Paths.get(AppConfig.getNotificationsFilePath() + ".tmp");
        try {
            Files.writeString(temp, array.toString(2), StandardCharsets.UTF_8);
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            logger.info("Notificaciones pendientes guardadas ({} items).", array.length());
        } catch (IOException e) {
            logger.error("Error al guardar notificaciones pendientes.", e);
        }
    }

    /**
     * Loads previously saved unread notifications from {@code notifications.json}.
     * Should be called once during application startup.
     */
    public synchronized void cargarPendientes() {
        Path path = Paths.get(AppConfig.getNotificationsFilePath());
        if (!Files.exists(path)) return;

        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                AppNotification.Tipo tipo = AppNotification.Tipo.valueOf(obj.getString("tipo"));
                String titulo = obj.getString("titulo");
                String cuerpo = obj.getString("cuerpo");
                LocalDateTime timestamp = LocalDateTime.parse(obj.getString("timestamp"));
                pendientes.add(new AppNotification(tipo, titulo, cuerpo, timestamp));
            }
            logger.info("Notificaciones previas cargadas: {} items.", array.length());
        } catch (Exception e) {
            logger.error("Error al cargar notificaciones previas.", e);
        }
    }
}
