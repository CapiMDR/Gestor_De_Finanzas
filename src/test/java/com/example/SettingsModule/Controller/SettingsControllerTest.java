package com.example.SettingsModule.Controller;

import com.example.SettingsModule.Model.Settings;
import com.example.SettingsModule.Model.SettingsManager;
import com.example.SettingsModule.View.SettingsView;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
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

    private SettingsController controller;

    @BeforeEach
    void setUp() {
        when(manager.getCurrentSettings()).thenReturn(new Settings("LIGHT", true));
        controller = new SettingsController(view, manager);
    }

    @Test
    @DisplayName("Constructor should load settings into view")
    void testLoadSettingsOnStart() {
        //Verified that upon creation, it requested the data and updated the view     
        verify(manager).getCurrentSettings();
        verify(view).updateView(any(Settings.class));
    }
    
    @Test
    @DisplayName("HandleThemeChange (Dark) should call manager")
    void testHandleThemeChangeDark() {
        //Arrange
        //captured the listener that the controller passed to the view
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(view).addThemeListener(captor.capture()); // Capturamos el listener registrado en el constructor
        when(view.getThemeSelected()).thenReturn(true); 

        captor.getValue().actionPerformed(event);
        verify(manager).changeTheme("DARK");
    }

    @Test
    @DisplayName("HandleThemeChange (Light) should call manager")
    void testHandleThemeChangeLight() {
        //Arrange
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(view).addThemeListener(captor.capture());
        when(view.getThemeSelected()).thenReturn(false); 
        captor.getValue().actionPerformed(event);
        verify(manager).changeTheme("LIGHT");
    }

    @Test
    @DisplayName("HandleNotificationChange should call manager")
    void testHandleNotificationChange() {
        //Arrange
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(view).addNotificationListener(captor.capture());
        when(view.isNotificationSelected()).thenReturn(false);
        captor.getValue().actionPerformed(event);
        verify(manager).changeNotification(false);
    }
}