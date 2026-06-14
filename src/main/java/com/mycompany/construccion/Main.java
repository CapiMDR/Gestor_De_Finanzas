package com.mycompany.construccion;

import accounts.account_model.AccountManager;
import config.AppConfig;
import config.AppSettings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reminders.reminder_view.RemindersModule;

/**
 * Main entry point of the application (JavaFX).
 * Initializes the account manager and launches the JavaFX main shell.
 */
public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        logger.info("Application starting...");
        AppConfig.ensureDataDirExists();
        AccountManager.initAccountManager();
        AccountManager.loadInitialData();
        
        // Initialize global background threads
        RemindersModule.initGlobalReminders();
        recurrings.recurring_view.RecurringsModule.initGlobalRecurrings();

        // Enable System Tray if user settings allow background mode
        if (AppSettings.getInstance().getModoNotificaciones() == AppSettings.ModoNotificaciones.SEGUNDO_PLANO) {
            notifications.SystemTrayManager.getInstance().enableTray();
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fxml/main_shell.fxml"));
            javafx.scene.Parent root = loader.load();
            MainShell shell = loader.getController();

            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
            
            String version = getAppVersion();
            primaryStage.setTitle("Gestor de Finanzas v" + version);
            
            try {
                primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/images/piggy.png")));
            } catch (Exception ex) {
                logger.warn("No se pudo cargar el icono de la ventana", ex);
            }

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            
            primaryStage.setOnCloseRequest(e -> {
                shell.alCerrar();
                if (AppSettings.getInstance().getModoNotificaciones() == AppSettings.ModoNotificaciones.SEGUNDO_PLANO) {
                    e.consume(); // Prevent the window from destroying the JVM
                    primaryStage.hide();
                    logger.info("Aplicación minimizada a la bandeja del sistema.");
                } else {
                    Platform.exit();
                    System.exit(0);
                }
            });

            primaryStage.show();
            logger.info("Main shell displayed successfully.");
        } catch (Exception e) {
            logger.error("Failed to load main shell.", e);
        }
    }

    private String getAppVersion() {
        try (java.io.InputStream is = getClass().getResourceAsStream("/META-INF/maven/io.github.capimdr/gestor-finanzas/pom.properties")) {
            if (is != null) {
                java.util.Properties p = new java.util.Properties();
                p.load(is);
                return p.getProperty("version", "Dev");
            }
        } catch (Exception e) {
            logger.warn("No se pudo leer la versión del pom.properties", e);
        }
        return "Dev";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
