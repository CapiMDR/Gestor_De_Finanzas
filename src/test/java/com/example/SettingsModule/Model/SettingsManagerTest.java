package com.example.SettingsModule.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for the SettingsManager (Subject).
 * Verifies state changes, persistence delegation, and observer notification calls.
 *
 * @author Jose Pablo Martinez
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Settings Manager Test")
class SettingsManagerTest {

    @Mock
    private JSONPersistenceSettings persistence;
    @Mock
    private SettingsObserver observer1;
    @Mock
    private SettingsObserver observer2;
    
    //Use a Spy on a real Settings object to track internal state
    @Spy
    private Settings currentSettings = new Settings("LIGHT", true);

    private SettingsManager manager;

    @BeforeEach
    void setUp() {
 
        when(persistence.loadSettings()).thenReturn(currentSettings);
   
        manager = new SettingsManager(persistence); 
        
        manager.addObserver(observer1);
        manager.addObserver(observer2);
        
        //Clear initial interactions
        reset(persistence, observer1, observer2); 
    }

    @Test
    @DisplayName("Should change theme, delegate save, and notify all observers")
    void testChangeThemeSuccess() {
        //ACT
        manager.changeTheme("DARK");

        //ASSERT
        //Verify state changed
        assertEquals("DARK", manager.getCurrentSettings().getTheme(), "State must be DARK");

        verify(persistence, times(1)).saveSettings(manager.getCurrentSettings());
        
        verify(observer1, times(1)).onSettingsChanged(manager.getCurrentSettings());
        verify(observer2, times(1)).onSettingsChanged(manager.getCurrentSettings());
    }

    @Test
    @DisplayName("Should not save or notify if theme is already the same")
    void testChangeThemeNoOp() {
        manager.changeTheme("LIGHT"); // Attempt to change to LIGHT

        //Verify: No interaction with persistence or observers occurred
        verify(persistence, never()).saveSettings(any(Settings.class));
        verify(observer1, never()).onSettingsChanged(any(Settings.class));
    }
    
    @Test
    @DisplayName("Should change notification state, delegate save, and notify")
    void testChangeNotification() {
        
        //ACT
        manager.changeNotification(false);
        assertFalse(manager.getCurrentSettings().isNotificationsEnabled(), "Notifications must be FALSE");
        
        verify(persistence, times(1)).saveSettings(manager.getCurrentSettings());

        verify(observer1, times(1)).onSettingsChanged(manager.getCurrentSettings());
    }
}