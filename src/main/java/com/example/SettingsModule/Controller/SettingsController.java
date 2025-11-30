package com.example.SettingsModule.Controller;

import com.example.SettingsModule.Model.SettingsManager;
import com.example.SettingsModule.View.SettingsView;

/**
 * Controller for the Settings module.
 * Manages the interaction between the Settings UI and the application state.
 *
 * @author Jose Pablo Martinez
 */

public class SettingsController {

    private final SettingsView view;
    private final SettingsManager manager;

    public SettingsController(SettingsView view, SettingsManager manager) {
        this.view = view;
        this.manager = manager;

        this.view.addThemeListener(e -> handleThemeChange());
        this.view.addNotificationListener(e -> handleNotificationChange());
        this.view.addBackListener(e -> handleBackNavigation());

        loadSettings();
    }

    public void loadSettings() {
        if (manager.getCurrentSettings() != null) {
            view.updateView(manager.getCurrentSettings());
        }
    }

    private void handleThemeChange() {
        boolean isDark = view.getThemeSelected(); 
        String newTheme = isDark ? "DARK" : "LIGHT";
        manager.changeTheme(newTheme);
    }

    private void handleNotificationChange() {
        boolean isEnabled = view.isNotificationSelected(); 
        manager.changeNotification(isEnabled);
    }

    /**
     * Handles the navigation back to the Account View.
     * Updated: Prints to console instead of showing a popup.
     */

    private void handleBackNavigation() {
        System.out.println("DEBUG: Navegando de vuelta a la vista principal...");
    }
}