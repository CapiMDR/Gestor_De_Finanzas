package com.example.goals;

import com.example.account.account_cotroller.AccountController;
import com.example.account.account_model.Account;
import com.example.account.account_model.AccountManager;
import com.example.account.account_model.AccountManagerSubject;
import com.example.account.account_model.JsonDataHandler;
import com.example.account.account_view.AccountView;

import com.example.goals.goals_controller.GoalDetailController;
import com.example.goals.goals_controller.GoalsController;
import com.example.goals.goals_view.GoalDetailView;
import com.example.goals.goals_view.GoalEditView;
import com.example.goals.goals_view.GoalsView;

import javax.swing.*;
import java.awt.CardLayout;

/**
 * Punto de entrada principal de la aplicación "Financial Manager".
 * Integra los módulos de Cuentas y Metas.
 *
 * @author Integración del Equipo
 */

public class Main {

    private static final String VIEW_ACCOUNTS = "AccountsView";
    private static final String VIEW_GOALS = "GoalsView";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::setupAndRun);
    }

    private static void setupAndRun() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Main view
        JFrame mainFrame = new JFrame("Gestor Financiero");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1200, 800);
        mainFrame.setLocationRelativeTo(null);

        //Persistence
        JsonDataHandler dataHandler = new JsonDataHandler();
        AccountManagerSubject accountSubject = new AccountManagerSubject();
        AccountManager accountManager = new AccountManager(accountSubject, dataHandler);

        AccountView accountView = new AccountView();
        AccountController accountController = new AccountController(accountManager, accountView);
        
        // load dates in JSON
        accountManager.loadInitialData(); 

        //GoalsModule
        GoalsView goalsView = new GoalsView(); 
        
        GoalEditView goalEditView = new GoalEditView(mainFrame);
        GoalDetailView goalDetailView = new GoalDetailView(mainFrame);

        GoalDetailController goalDetailController = new GoalDetailController(goalDetailView);
        GoalsController goalsController = new GoalsController(
                goalsView, 
                goalEditView, 
                goalDetailController, 
                accountManager
        );

        
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Agregar las vistas al contenedor
        mainPanel.add(accountView.getContentPane(), VIEW_ACCOUNTS); 
        mainPanel.add(goalsView, VIEW_GOALS);
        accountView.getBtnAccessAccount().addActionListener(e -> {
            int selectedIndex = accountView.getListAccounts().getSelectedIndex();
            
            if (selectedIndex >= 0) {
                // Obtener la cuenta seleccionada del modelo
                Account selectedAccount = accountManager.getAccountByIndex(selectedIndex);
                
                if (selectedAccount != null) {
                    // Pasar la cuenta al módulo de Metas
                    goalsController.setAccount(selectedAccount);
                    
                    // Cambiar vista
                    cardLayout.show(mainPanel, VIEW_GOALS);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Seleccione una cuenta para continuar.");
            }
        });

        // 2. Menú para volver a Cuentas (Ya que GoalsView no tiene botón de volver)
        JMenuBar menuBar = new JMenuBar();
        JMenu menuOpciones = new JMenu("Navegación");
        JMenuItem itemInicio = new JMenuItem("Ir a Lista de Cuentas");
        
        itemInicio.addActionListener(e -> {
            cardLayout.show(mainPanel, VIEW_ACCOUNTS);
        });

        menuOpciones.add(itemInicio);
        menuBar.add(menuOpciones);
        mainFrame.setJMenuBar(menuBar);

        //Run
        mainFrame.add(mainPanel);
        cardLayout.show(mainPanel, VIEW_ACCOUNTS); // Mostrar inicio
        mainFrame.setVisible(true);
    }
}