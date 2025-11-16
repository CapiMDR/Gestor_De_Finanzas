package SettingsModule.Model;

/**
 * Represents the configuration state of the application.
 * Includes user preferences such as UI theme and notification settings.
 *
 * @author Jose Pablo Martinez
 */

public class Settings {

    private String theme;
    private boolean notificationsEnabled;

    //Default constructor: set default values.
    public Settings() {
        this.theme = "LIGHT";
        this.notificationsEnabled = true;
    }

    /**
     * Parameterized constructor.
     *
     * @param theme The selected UI theme (e.g., "DARK", "LIGHT").
     * @param notificationsEnabled True if notifications are active, false otherwise.
     */
    
    public Settings(String theme, boolean notificationsEnabled) {
        this.theme = theme;
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    @Override
    public String toString() {
        return "Settings{" + "theme=" + theme + ", notifications=" + notificationsEnabled + '}';
    }
}