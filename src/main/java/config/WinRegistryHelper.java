package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Utility class to interact with the Windows Registry to add/remove
 * the application from the startup items.
 */
public class WinRegistryHelper {

    private static final Logger logger = LoggerFactory.getLogger(WinRegistryHelper.class);
    
    // The registry key where autostart programs for the current user are stored
    private static final String REG_RUN_KEY = "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    private static final String APP_NAME = "GestorFinanzas";
    
    /**
     * Registers the application to start automatically with Windows.
     * Uses the javaw executable to run the JAR in the background if packaged,
     * or adds a shortcut path. For development, we'll try to find the executable.
     */
    public static void registerAutostart() {
        String appPath = getExecutablePath();
        if (appPath == null) {
            logger.error("No se pudo determinar la ruta del ejecutable para el Autostart.");
            return;
        }

        try {
            // reg add HKCU\Software\Microsoft\Windows\CurrentVersion\Run /v GestorFinanzas /t REG_SZ /d "path\to\app.exe" /f
            ProcessBuilder pb = new ProcessBuilder("reg", "add", REG_RUN_KEY, "/v", APP_NAME, "/t", "REG_SZ", "/d", appPath, "/f");
            Process process = pb.start();
            process.waitFor();
            logger.info("Aplicación registrada en el inicio de Windows.");
        } catch (IOException | InterruptedException e) {
            logger.error("Error al registrar en el inicio de Windows.", e);
        }
    }

    /**
     * Removes the application from the Windows startup registry.
     */
    public static void unregisterAutostart() {
        try {
            // reg delete HKCU\Software\Microsoft\Windows\CurrentVersion\Run /v GestorFinanzas /f
            ProcessBuilder pb = new ProcessBuilder("reg", "delete", REG_RUN_KEY, "/v", APP_NAME, "/f");
            Process process = pb.start();
            process.waitFor();
            logger.info("Aplicación removida del inicio de Windows.");
        } catch (IOException | InterruptedException e) {
            logger.error("Error al remover del inicio de Windows.", e);
        }
    }
    
    /**
     * Attempts to find the path to the current executable or JAR.
     */
    private static String getExecutablePath() {
        try {
            // Get the location of the JAR or compiled classes
            String path = WinRegistryHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            
            // Clean up the path format for Windows (remove leading slash)
            if (path.startsWith("/") && path.length() > 2 && path.charAt(2) == ':') {
                path = path.substring(1);
            }
            
            // If running from a Jar, format the command to launch the jar using javaw
            if (path.endsWith(".jar")) {
                // To keep it simple, we just assume `javaw -jar path/to/app.jar`
                return "javaw -jar " + path;
            }
            
            // If packaged as an EXE using jpackage/Launch4j, the system property might have it
            String sunJavaCommand = System.getProperty("sun.java.command");
            if (sunJavaCommand != null && sunJavaCommand.toLowerCase().endsWith(".exe")) {
                return sunJavaCommand;
            }
            
            // Fallback for development: return null so we don't pollute the registry with IDE paths
            logger.warn("El programa no parece estar empaquetado como JAR o EXE. No se configurará el autostart de forma segura.");
            return null;
        } catch (Exception e) {
            logger.error("Error al obtener la ruta del ejecutable.", e);
            return null;
        }
    }
}
