package com.mycompany.construccion;

import accounts.account_model.AccountManager;
import accounts.account_view.AccountsModule;
import config.AppConfig;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point of the application (JavaFX).
 * Initializes the account manager and launches the JavaFX accounts view.
 */

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        logger.info("Application starting...");
        AppConfig.ensureDataDirExists();
        AccountManager.initAccountManager();
        AccountManager.loadInitialData();
        AccountsModule.initAccountsModule();
        logger.info("Main view displayed successfully.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
