package config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link AppSettings}.
 * Verifies default values, setters, and JSON file persistence.
 */

@DisplayName("AppSettings Test")
class AppSettingsTest {

    private AppSettings settings;
    private MockedStatic<AppConfig> mockedAppConfig;
    private Path tempSettingsFile;

    @BeforeEach
    void setUp() throws Exception {
        // Mock the file path to use a temporary file for persistence testing
        tempSettingsFile = Files.createTempFile("test_settings", ".json");
        Files.writeString(tempSettingsFile, "{}", StandardCharsets.UTF_8);
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getSettingsFilePath)
                       .thenReturn(tempSettingsFile.toAbsolutePath().toString());

        settings = AppSettings.getInstance();
        
        // Reset defaults just in case another test polluted the Singleton
        settings.setAutostart(false);
        settings.setModoNotificaciones(AppSettings.ModoNotificaciones.SOLO_PRIMER_PLANO);
        settings.setTutorialMostrado(false);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockedAppConfig.close();
        Files.deleteIfExists(tempSettingsFile);
    }

    @Test
    @DisplayName("should change values and persist to JSON file")
    void testSettersAndPersistence() throws Exception {
        // Act
        settings.setAutostart(true);
        settings.setTutorialMostrado(true);
        settings.setModoNotificaciones(AppSettings.ModoNotificaciones.SEGUNDO_PLANO);

        // Assert - verify the file was written with the new values
        assertTrue(Files.exists(tempSettingsFile));
        String json = Files.readString(tempSettingsFile, StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(json);

        assertTrue(obj.getBoolean("autostart"));
        assertTrue(obj.getBoolean("tutorialMostrado"));
        assertEquals("SEGUNDO_PLANO", obj.getString("modoNotificaciones"));
    }

    @Test
    @DisplayName("should load values from existing JSON file")
    void testLoadExistingSettings() throws Exception {
        // Arrange - Write a custom JSON to the temp file
        JSONObject obj = new JSONObject();
        obj.put("autostart", true);
        obj.put("tutorialMostrado", false);
        obj.put("modoNotificaciones", "SOLO_PRIMER_PLANO");
        Files.writeString(tempSettingsFile, obj.toString(), StandardCharsets.UTF_8);

        // Act - Invoke private 'cargar' method to reload from file
        Method cargarMethod = AppSettings.class.getDeclaredMethod("cargar");
        cargarMethod.setAccessible(true);
        cargarMethod.invoke(settings);

        // Assert
        assertTrue(settings.isAutostart());
        assertFalse(settings.isTutorialMostrado());
        assertEquals(AppSettings.ModoNotificaciones.SOLO_PRIMER_PLANO, settings.getModoNotificaciones());
    }

    @Test
    @DisplayName("guardar handles IOException")
    void testGuardarIOException() {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.writeString(org.mockito.Mockito.any(), org.mockito.Mockito.anyString(), org.mockito.Mockito.any()))
                    .thenThrow(new java.io.IOException("Simulated write error"));
            
            // Should not throw, should be caught and logged
            assertDoesNotThrow(() -> settings.guardar());
        }
    }

    @Test
    @DisplayName("cargar handles Exception")
    void testCargarException() throws Exception {
        Files.writeString(tempSettingsFile, "invalid json {", StandardCharsets.UTF_8);

        Method cargarMethod = AppSettings.class.getDeclaredMethod("cargar");
        cargarMethod.setAccessible(true);
        
        // Should catch JSONException and use defaults
        cargarMethod.invoke(settings);

        assertFalse(settings.isAutostart());
    }
}
