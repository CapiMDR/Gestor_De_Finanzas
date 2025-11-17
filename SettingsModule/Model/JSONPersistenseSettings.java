package com.example.SettingsModule.Model;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles the persistence of Settings data using JSON files.
 *
 * @author Jose Pablo Martinez
 */

public class JSONPersistenceSettings {

    private static final String FILE_PATH = "settings.json";

    public void saveSettings(Settings config) {
        JSONObject json = new JSONObject();
        json.put("theme", config.getTheme());
        json.put("notificationsEnabled", config.isNotificationsEnabled());

        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(json.toString(4)); // Indentation for readability
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    public Settings loadSettings() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            JSONObject json = new JSONObject(content);

            String theme = json.getString("theme");
            boolean notifications = json.getBoolean("notificationsEnabled");

            return new Settings(theme, notifications);
        } catch (IOException | org.json.JSONException e) {
            System.out.println("Settings file not found or invalid. Using defaults.");
            return new Settings(); // Return default settings
        }
    }
}