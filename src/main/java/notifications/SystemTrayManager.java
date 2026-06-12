package notifications;

import com.mycompany.construccion.MainShell;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Manages the AWT SystemTray integration for background mode.
 * Keeps the application alive when all JavaFX windows are closed,
 * and allows reopening the main shell from the tray icon.
 */
public class SystemTrayManager {

    private static final Logger logger = LoggerFactory.getLogger(SystemTrayManager.class);

    private static final SystemTrayManager INSTANCE = new SystemTrayManager();

    private TrayIcon trayIcon;
    private boolean enabled = false;

    private SystemTrayManager() {}

    public static SystemTrayManager getInstance() {
        return INSTANCE;
    }

    /**
     * Installs the tray icon and configures JavaFX to NOT exit when windows close.
     */
    public void enableTray() {
        if (enabled || !SystemTray.isSupported()) {
            return;
        }

        // Prevent JavaFX from shutting down when the main window closes
        Platform.setImplicitExit(false);

        try {
            SystemTray tray = SystemTray.getSystemTray();

            // Load an icon (using a dummy fallback if none found)
            Image image = Toolkit.getDefaultToolkit().createImage(
                    getClass().getResource("/icon.png") // Ensure this file exists in resources
            );

            // Fallback if null
            if (image == null) {
                image = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            }

            PopupMenu popup = new PopupMenu();

            MenuItem openItem = new MenuItem("Abrir Gestor de Finanzas");
            openItem.addActionListener(e -> reopenApplication());
            popup.add(openItem);

            popup.addSeparator();

            MenuItem exitItem = new MenuItem("Salir");
            exitItem.addActionListener(e -> {
                logger.info("El usuario cerró la aplicación desde la bandeja del sistema.");
                // Execute proper shutdown
                Platform.runLater(() -> {
                    if (MainShell.getInstance() != null) {
                        MainShell.getInstance().alCerrar();
                    }
                    Platform.exit();
                    System.exit(0);
                });
            });
            popup.add(exitItem);

            trayIcon = new TrayIcon(image, "Gestor de Finanzas", popup);
            trayIcon.setImageAutoSize(true);
            
            // Double click reopens
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        reopenApplication();
                    }
                }
            });

            tray.add(trayIcon);
            enabled = true;
            logger.info("Icono de bandeja del sistema instalado. Modo segundo plano activo.");

        } catch (AWTException e) {
            logger.error("No se pudo instalar el icono de la bandeja del sistema.", e);
        }
    }

    /**
     * Removes the tray icon and reverts JavaFX behavior to exit when windows close.
     */
    public void disableTray() {
        if (!enabled) return;

        Platform.setImplicitExit(true);

        if (trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
            trayIcon = null;
        }
        enabled = false;
        logger.info("Icono de bandeja del sistema removido. Modo segundo plano desactivado.");
    }

    /**
     * Shows a balloon tip notification from the tray icon.
     */
    public void showBalloonNotification(String title, String message, TrayIcon.MessageType type) {
        if (enabled && trayIcon != null) {
            trayIcon.displayMessage(title, message, type);
        }
    }

    private void reopenApplication() {
        Platform.runLater(() -> {
            // Re-show the main stage if it was hidden
            javafx.stage.Stage primaryStage = (javafx.stage.Stage) 
                    javafx.stage.Window.getWindows().stream()
                    .filter(w -> w instanceof javafx.stage.Stage)
                    .findFirst()
                    .orElse(null);

            if (primaryStage != null) {
                primaryStage.show();
                primaryStage.toFront();
            } else {
                // Should not happen if we just hide the stage, but handle if needed
                logger.warn("No se encontró el Stage principal para reabrir.");
            }
        });
    }
}
