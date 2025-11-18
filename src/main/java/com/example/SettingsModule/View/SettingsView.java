package com.example.SettingsModule.View;

import com.example.SettingsModule.Model.Settings;
import java.awt.event.ActionListener;
import javax.swing.JButton; 

/**
 * Dummy class for testing
 * @author Jose Pablo Martinez
 */

public class SettingsView {

    public JButton getBtnTheme() { return new JButton(); }
    public JButton getCheckNotifications() { return new JButton(); }
    public boolean getThemeSelected() { return true; }
    public boolean isNotificationSelected() { return true; }
    public void updateView(Settings config) {
        
    }
}
