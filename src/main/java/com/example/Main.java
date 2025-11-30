package com.example;

import com.example.AccountsModule.Model.Account;
import com.example.AccountsModule.Model.AccountManager;
import com.example.AccountsModule.Model.Coin;

import com.example.GoalsModule.Controller.GoalDetailController;
import com.example.GoalsModule.Controller.GoalsController;
import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalDetailView;
import com.example.GoalsModule.View.GoalEditView;
import com.example.GoalsModule.View.GoalsView;

import com.example.SettingsModule.Controller.SettingsController;
import com.example.SettingsModule.Model.SettingsManager;
import com.example.SettingsModule.View.SettingsView;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Main for only testing the functions of SettingsModule and GoalsModule
 * @author Jose Pablo Martinez
 */

public class Main {

    private static final String VIEW_GOALS = "GoalsView";
    private static final String VIEW_SETTINGS = "SettingsView";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setupAndRun();
        });
    }

    private static void setupAndRun() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Financial Manager - Sistema Integrado");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        AccountManager accountManager = new AccountManager();
        Account myAccount = new Account();
        myAccount.setName("Cuenta Principal");
        myAccount.setCoin(Coin.USD);
        
        //Dummy dates
        myAccount.getGoals().add(new Goal("PC Gamer", new BigDecimal("25000.00"), "RTX 4090"));
        myAccount.getGoals().add(new Goal("Viaje Japón", new BigDecimal("80000.00"), "2026"));

        GoalsView goalsView = new GoalsView();
        GoalEditView editView = new GoalEditView(frame);
        GoalDetailView detailView = new GoalDetailView(frame);

        GoalDetailController detailController = new GoalDetailController(detailView);
        GoalsController goalsController = new GoalsController(
                goalsView, editView, detailController, accountManager
        );
        
        goalsController.setAccount(myAccount);

        SettingsManager settingsManager = new SettingsManager();
        SettingsView settingsView = new SettingsView();
        SettingsController settingsController = new SettingsController(settingsView, settingsManager);

        //Navigation between settings and Goals
        CardLayout cardLayout = new CardLayout();
        JPanel mainContainer = new JPanel(cardLayout);
        mainContainer.add(goalsView, VIEW_GOALS);
        mainContainer.add(settingsView, VIEW_SETTINGS);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("Opciones");
        JMenuItem itemConfig = new JMenuItem("Configuración ⚙️");
        
        itemConfig.addActionListener(e -> {
            cardLayout.show(mainContainer, VIEW_SETTINGS);
        });

        menuOpciones.add(itemConfig);
        menuBar.add(menuOpciones);
        frame.setJMenuBar(menuBar);

        settingsView.addBackListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainContainer, VIEW_GOALS);
            }
        });

        frame.add(mainContainer);
        frame.setVisible(true);
    }
}