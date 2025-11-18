package com.example.SettingsModule.Controller;

import com.example.SettingsModule.Model.Settings;
import com.example.SettingsModule.Model.SettingsManager;
import com.example.SettingsModule.View.SettingsView;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the SettingsController.
 * @author Jose Pablo Martinez
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Settings Controller Test")
class SettingsControllerTest {

    @Mock
    private SettingsView view; 
    @Mock
    private SettingsManager manager; 
    @Mock
    private ActionEvent event; 
    
    @InjectMocks
    private SettingsController controller;

    @BeforeEach
    void setUp() {
        when(manager.getCurrentSettings()).thenReturn(new Settings("LIGHT", true));
        
        controller = new SettingsController(view, manager);
    
        reset(view, manager); 
    }

    @Test
    @DisplayName("LoadSettingsOnStart should initialize manager state (Verification of setup)")
    void testLoadSettingsOnStart() {
        // This test only serves to verify that the setup phase completed without errors.
    }
    
    @Test
    @DisplayName("HandleThemeChange (Dark) should call manager logic")
    void testHandleThemeChangeDark() {
    
        JButton mockBtnTheme = mock(JButton.class); 

        //When the view requests the button, return our mocked instance
        when(view.getBtnTheme()).thenReturn(mockBtnTheme);
        
        when(event.getSource()).thenReturn(mockBtnTheme); 

        //Simulate the view returns the state "DARK"
        when(view.getThemeSelected()).thenReturn(true); 

        //Execute the controller's event handler
        controller.actionPerformed(event); 

        verify(manager, times(1)).changeTheme("DARK");
    }

    @Test
    @DisplayName("HandleNotificationChange should call manager logic")
    void testHandleNotificationChange() {
        JButton mockCheckNotifications = mock(JButton.class);
        
        //The view delivers the button instance
        when(view.getCheckNotifications()).thenReturn(mockCheckNotifications);
        
        //The event simulates that it originated from that instance
        when(event.getSource()).thenReturn(mockCheckNotifications); 
        
        //Simulate the view returns the "disabled" state
        when(view.isNotificationSelected()).thenReturn(false);

        controller.actionPerformed(event);
        verify(manager, times(1)).changeNotification(false);
    }
}