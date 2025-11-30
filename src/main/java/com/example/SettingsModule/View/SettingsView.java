package com.example.SettingsModule.View;

import com.example.SettingsModule.Model.Settings;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Main view for the Settings Module.
 * Displays application-wide configurations like Theme, Currency, and Notifications.
 *
 * @author Jose Pablo Martinez
 */
public class SettingsView extends JPanel {

 
    private final JComboBox<String> comboTheme;
    private final JCheckBox checkNotifications;
    private final JComboBox<String> comboCurrency; 
    private JButton btnBack;
    
    private static final Color BG_COLOR = new Color(245, 247, 251); 
    private static final Color HEADER_BG = new Color(255, 140, 0);  
    private static final Color HEADER_TEXT = Color.WHITE;
    private static final Color SECTION_BG = Color.WHITE;            
    private static final Color TEXT_PRIMARY = new Color(51, 51, 51);

    private static final Color BTN_BACK_BG = new Color(52, 73, 94); // Azul Grisáceo Oscuro
    private static final Color BTN_BACK_TEXT = Color.WHITE;

    public SettingsView() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG_COLOR);

        // 1. HEADER
        add(buildHeader(), BorderLayout.NORTH);

        // 2. MAIN CONTENT
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 50, 30, 50)); 

        // --- Section 1: General Preferences ---
        JPanel generalSection = buildSectionPanel("Preferencias Generales");
        
        comboTheme = new JComboBox<>(new String[]{"Claro", "Oscuro"});
        addSettingRow(generalSection, "Tema de la Aplicación:", comboTheme);
        
        comboCurrency = new JComboBox<>(new String[]{"MXN ($)", "USD ($)", "EUR (€)"});
        addSettingRow(generalSection, "Moneda Principal:", comboCurrency);
        
        contentPanel.add(generalSection);
        contentPanel.add(Box.createVerticalStrut(20)); 

        JPanel notifySection = buildSectionPanel("Notificaciones");
        
        checkNotifications = new JCheckBox("Habilitar Notificaciones Push");
        checkNotifications.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkNotifications.setBackground(SECTION_BG);
        checkNotifications.setFocusPainted(false);
        
        notifySection.add(checkNotifications); 
        
        contentPanel.add(notifySection);
        contentPanel.add(Box.createVerticalGlue()); 

        add(contentPanel, BorderLayout.CENTER);


        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        headerPanel.setBackground(HEADER_BG);
        
        JLabel lblTitle = new JLabel("CONFIGURACIONES");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(HEADER_TEXT);
        headerPanel.add(lblTitle);
        return headerPanel;
    }

    private JPanel buildFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        footerPanel.setBackground(BG_COLOR);

        btnBack = new JButton("← Volver a Cuentas");
        

        btnBack.setBackground(BTN_BACK_BG);
        btnBack.setForeground(BTN_BACK_TEXT);
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setFocusPainted(false);

        btnBack.setContentAreaFilled(false);
        btnBack.setOpaque(true);
        btnBack.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        footerPanel.add(btnBack);
        return footerPanel;
    }

    private JPanel buildSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECTION_BG);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel lblSection = new JLabel(title);
        lblSection.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSection.setForeground(new Color(41, 128, 185)); 
        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lblSection);
        panel.add(Box.createVerticalStrut(15));
        return panel;
    }

    private void addSettingRow(JPanel container, String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(SECTION_BG);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        
        component.setPreferredSize(new Dimension(200, 30));
        component.setFont(new Font("SansSerif", Font.PLAIN, 14));

        row.add(label, BorderLayout.WEST);
        row.add(component, BorderLayout.EAST);
        
        container.add(row);
        container.add(Box.createVerticalStrut(10));
    }


    public void updateView(Settings config) {
        if (config == null) return;

        if ("DARK".equals(config.getTheme())) {
            comboTheme.setSelectedItem("Oscuro");
        } else {
            comboTheme.setSelectedItem("Claro");
        }

        checkNotifications.setSelected(config.isNotificationsEnabled());
        repaint();
    }

    public void addThemeListener(ActionListener listener) {
        comboTheme.addActionListener(listener);
    }

    public void addNotificationListener(ActionListener listener) {
        checkNotifications.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        btnBack.addActionListener(listener);
    }

    public boolean getThemeSelected() {
        return "Oscuro".equals(comboTheme.getSelectedItem());
    }

    public boolean isNotificationSelected() {
        return checkNotifications.isSelected();
    }
    
    //Getters for Testing
    public JComboBox<String> getComboTheme() { return comboTheme; } 
    public JCheckBox getCheckNotifications() { return checkNotifications; }
}