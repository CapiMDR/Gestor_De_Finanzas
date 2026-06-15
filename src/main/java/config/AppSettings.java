package config;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Persists user preferences to {@code ~/.gestor-finanzas/settings.json}.
 *
 * Settings managed:
 * - {@code modoNotificaciones} — SOLO_PRIMER_PLANO (default) or SEGUNDO_PLANO
 * - {@code autostart}          — whether to register the app in the Windows startup registry
 * - {@code tutorialMostrado}   — whether the first-run tutorial has already been shown
 */
public class AppSettings {

    private static final Logger logger = LoggerFactory.getLogger(AppSettings.class);
    private static final String KEY_MODO_NOTIFICACIONES = "modoNotificaciones";

    /**
     * Controls when the background reminder scheduler keeps the JVM alive.
     * SOLO_PRIMER_PLANO (default): daemon thread — exits with the window.
     * SEGUNDO_PLANO: non-daemon thread — keeps running after the window closes.
     */
    public enum ModoNotificaciones {
        SOLO_PRIMER_PLANO,
        SEGUNDO_PLANO
    }

    // ── Singleton ─────────────────────────────────────────────────────────────

    private static final AppSettings INSTANCE = new AppSettings();

    private AppSettings() {
        cargar();
    }

    /** @return the single application-wide instance */
    public static AppSettings getInstance() {
        return INSTANCE;
    }

    // ── Fields (with defaults) ────────────────────────────────────────────────

    private ModoNotificaciones modoNotificaciones = ModoNotificaciones.SOLO_PRIMER_PLANO;
    private boolean autostart = false;
    private boolean tutorialMostrado = false;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public ModoNotificaciones getModoNotificaciones() { return modoNotificaciones; }

    public void setModoNotificaciones(ModoNotificaciones modo) {
        this.modoNotificaciones = modo;
        guardar();
    }

    public boolean isAutostart() { return autostart; }

    public void setAutostart(boolean autostart) {
        this.autostart = autostart;
        guardar();
    }

    public boolean isTutorialMostrado() { return tutorialMostrado; }

    public void setTutorialMostrado(boolean mostrado) {
        this.tutorialMostrado = mostrado;
        guardar();
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    /** Saves current settings to {@code settings.json} using an atomic write. */
    public void guardar() {
        JSONObject obj = new JSONObject();
        obj.put(KEY_MODO_NOTIFICACIONES, modoNotificaciones.name());
        obj.put("autostart", autostart);
        obj.put("tutorialMostrado", tutorialMostrado);

        Path target = Paths.get(AppConfig.getSettingsFilePath());
        Path temp = Paths.get(AppConfig.getSettingsFilePath() + ".tmp");
        try {
            Files.writeString(temp, obj.toString(2), StandardCharsets.UTF_8);
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            logger.debug("Configuración guardada.");
        } catch (IOException e) {
            logger.error("Error al guardar configuración.", e);
        }
    }

    /** Loads settings from {@code settings.json}. Silently uses defaults if the file does not exist. */
    private void cargar() {
        Path path = Paths.get(AppConfig.getSettingsFilePath());
        if (!Files.exists(path)) return;

        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject(json);

            if (obj.has(KEY_MODO_NOTIFICACIONES)) {
                modoNotificaciones = ModoNotificaciones.valueOf(obj.getString(KEY_MODO_NOTIFICACIONES));
            }
            autostart = obj.optBoolean("autostart", false);
            tutorialMostrado = obj.optBoolean("tutorialMostrado", false);
            logger.debug("Configuración cargada.");
        } catch (Exception e) {
            logger.error("Error al cargar configuración, usando valores por defecto.", e);
        }
    }
}
