package SettingsModule.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the application settings logic.
 * It acts as the Subject in the Observer pattern, notifying registered views
 * whenever a configuration change occurs.
 *
 * @author Jose Pablo Martinez
 */

public class SettingsManager {

    private Settings currentSettings;
    private final JSONPersistenceSettings persistence;
    private final List<SettingsObserver> observers;

    public SettingsManager() {
        this.persistence = new JSONPersistenceSettings();
        this.observers = new ArrayList<>();
        // Load initial state from disk
        this.currentSettings = persistence.loadSettings();
    }

    //Business Logic Methods 

    /**
     * Updates the theme setting, saves it, and notifies observers.
     *
     * @param newTheme The new theme string (e.g., "DARK").
     */

    public void changeTheme(String newTheme) {
        if (!currentSettings.getTheme().equals(newTheme)) {
            currentSettings.setTheme(newTheme);
            saveSettings();
            notifyObservers();
        }
    }

    public void changeNotification(boolean notifications) {
        if (currentSettings.isNotificationsEnabled() != notifications) {
            currentSettings.setNotificationsEnabled(notifications);
            saveSettings();
            notifyObservers();
        }
    }

  
    //Persist the current state to the JSON file
    public void saveSettings() {
        persistence.saveSettings(currentSettings);
    }

    public Settings getCurrentSettings() {
        return currentSettings;
    }

    //Observer pattern

    public void addObserver(SettingsObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            //Immediately notify the new observer of current state
            observer.onSettingsChanged(currentSettings);
        }
    }

    public void removeObserver(SettingsObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (SettingsObserver observer : observers) {
            observer.onSettingsChanged(currentSettings);
        }
    }
}