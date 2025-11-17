package com.example.SettingsModule.Controller;

import com.example.SettingsModule.Model.SettingsManager;
import com.example.SettingsModule.View.SettingsView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller for the Settings module.
 * Processes user actions from the SettingsView and invokes logic in the SettingsManager.
 *
 * @author Jose Pablo Martinez
 */

public class SettingsController implements ActionListener {

    private final SettingsView view;
    private final SettingsManager manager;

  
    public SettingsController(SettingsView view, SettingsManager manager) {
        this.view = view;
        this.manager = manager;
        loadSettings();
    }

    public void loadSettings() {
        if (manager.getCurrentSettings() != null) {
            view.updateView(manager.getCurrentSettings());
        }
    }

    /**
     * Handles action events from the View components.
     *
     * @param e The event triggered by the user.
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        // Identify which component triggered the event
        if (e.getSource() == view.getBtnTheme()) {
            handleThemeChange();
        } else if (e.getSource() == view.getCheckNotifications()) {
            handleNotificationChange();
        }
    }

    public void handleThemeChange() {
        // Assuming getThemeSelected returns true for Dark, false for Light
        boolean isDark = view.getThemeSelected(); 
        String newTheme = isDark ? "DARK" : "LIGHT";
        
        manager.changeTheme(newTheme);
    }

    public void handleNotificationChange() {
        // Assuming isNotificationSelected returns the checkbox state
        boolean isEnabled = view.isNotificationSelected(); 
        
        manager.changeNotification(isEnabled);
    }
}