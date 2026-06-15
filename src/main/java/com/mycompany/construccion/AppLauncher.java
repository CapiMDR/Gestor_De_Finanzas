package com.mycompany.construccion;

/**
 * Wrapper class to launch the JavaFX application.
 * In Java 11+, running a class that extends javafx.application.Application directly
 * from some IDEs can cause "JavaFX runtime components are missing" errors.
 * Launching through this separate class bypasses that issue.
 */

public class AppLauncher {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AppLauncher.class);

    public static void main(String[] args) {
        if (!config.SingleInstanceGuard.checkAndLock()) {
            logger.warn("La aplicación ya está en ejecución.");
            System.exit(0);
        }
        Main.main(args);
    }
}
