package com.example.movement.movement_controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import com.example.account.account_model.Account;
import com.example.account.account_model.AccountManager;
import com.example.movement.movement_model.Movement;
import com.example.movement.movement_model.MovementCategory;
import com.example.movement.movement_model.MovementCategory.MovementType;
import com.example.movement.movement_model.MovementManager;
import com.example.movement.movement_model.MovementObserver;
import com.example.movement.movement_view.MovementCategoriesView;
import com.example.movement.movement_view.MovementManagerView;

public class MovementController implements MovementObserver{
    private MovementManager model;
    private MovementManagerView view;
    private MovementCategoriesView categoriesManagerView;
    private AccountManager accountManager;
    private Account currentAccount;

    public MovementController(MovementManager model, AccountManager accountManager, MovementManagerView view, Account currentAccount){
        this.model = model;
        this.view = view;
        this.categoriesManagerView = null;
        this.accountManager = accountManager;
        this.currentAccount = currentAccount;
        model.addOserver(this);
        AssignEvents();
        loadInitialData();
    }
    private void AssignEvents() {
        this.view.getBtnAddIncome().addActionListener(e -> handleAddMovement(MovementType.INCOME));
        
        this.view.getBtnAddExpense().addActionListener(e -> handleAddMovement(MovementType.EXPENSE));
        
        this.view.getBtnAddCategoryIncome().addActionListener(e -> showCategoriesManagerView());
        this.view.getBtnAddCategoryExpense().addActionListener(e -> showCategoriesManagerView());
    }

    private void handleAddMovement(MovementType type) {
        String description;
        String amountStr;
        String categoryName;
        java.util.Date utilDate;
        
        if (type == MovementType.INCOME) {
            description = view.getTxtDescriptionIncome().getText();
            amountStr = view.getTxtAmountIncome().getText();
            categoryName = view.getListCategoriesIncome().getSelectedValue();
            utilDate = view.getDateIncome().getDate();
        } else{ // EXPENSE
            description = view.getTxtDescriptionExpense().getText();
            amountStr = view.getTxtAmountExpense().getText();
            categoryName = view.getListCategoriesExpense().getSelectedValue();
            utilDate = view.getDateExpense().getDate();
        }
        
        if (description.isEmpty() || amountStr.isEmpty() || categoryName == null || utilDate == null) {
            JOptionPane.showMessageDialog(view, 
                "Todos los campos son obligatorios.", 
                "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            BigDecimal amount = new BigDecimal(amountStr);
            LocalDateTime movementDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            
            MovementCategory category = model.getCategoryByName(categoryName); 

            if (category == null){
                JOptionPane.showMessageDialog(view, 
                    "La categoría '" + categoryName + "' no fue encontrada", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            addMovement(description, amount, category, currentAccount, movementDate);
            
            JOptionPane.showMessageDialog(view, 
                type.toString() + " agregado exitosamente y saldo actualizado", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (type == MovementType.INCOME) {
                view.clearIncomeFields();
            } else {
                view.clearExpenseFields();
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, 
                "El Monto debe ser un número válido.", 
                "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, 
                "Error al agregar movimiento: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addMovement(String description, BigDecimal amount, MovementCategory category, Account account, LocalDateTime date){
        Movement movement  = new Movement(model.getMovements().size(), description, amount, category, account, date);
        
        account.addMovement(movement);
        model.addMovement(movement);
        
        accountManager.saveAccountsData();
        
        accountManager.getSubject().notifyObservers(accountManager.getAccounts());
        model.notifyObservers();
    }

    private void loadInitialData() {
        view.getTxtAccountIncome().setText(currentAccount.getName());
        view.getTxtAccountExpense().setText(currentAccount.getName());
        
        updateCategoriesView(new ArrayList<>(model.getCategories().values())); 
    }

    private void updateCategoriesView(List<MovementCategory> categories){
        DefaultListModel<String> incomeModel = new DefaultListModel<>();
        DefaultListModel<String> expenseModel = new DefaultListModel<>();

        for (MovementCategory category : categories) {
            if (category.getType() == MovementType.INCOME) {
                incomeModel.addElement(category.getName());
            } else {
                expenseModel.addElement(category.getName());
            }
        }

        view.getListCategoriesIncome().setModel(incomeModel);
        view.getListCategoriesExpense().setModel(expenseModel);
    }

    public void showCategoriesManagerView() {

        if (this.categoriesManagerView != null && this.categoriesManagerView.isVisible()) {
            this.categoriesManagerView.toFront();
            return;
        }
        
        MovementCategoriesView categoriesView = new MovementCategoriesView();
        categoriesView.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        this.categoriesManagerView = categoriesView; 
        
        categoriesView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                categoriesManagerView = null;
            }
        });
        
        DefaultListModel<String> typeModel = new DefaultListModel<>();
        for (MovementType type : MovementType.values()) {
            typeModel.addElement(type.name());
        }
        categoriesView.getListCategoryType().setModel(typeModel);

        updateCategoriesList(categoriesView);

        categoriesView.getBtnConfirm().addActionListener(e -> 
            handleAddCategory(categoriesView)
        );
        categoriesView.getBtnDeleteCategory().addActionListener(e -> 
            handleRemoveCategory(categoriesView)
        );
        
        categoriesView.setVisible(true);
    }

    private void handleAddCategory(MovementCategoriesView categoriesView){
        String name = categoriesView.getTxtNewNameCateogory().getText().trim();
        String typeStr = categoriesView.getListCategoryType().getSelectedValue();

        if (name.isEmpty() || typeStr == null) {
            JOptionPane.showMessageDialog(categoriesView, 
                "Debe ingresar el nombre y seleccionar el tipo (Ingreso/Gasto)", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        try {
            MovementType type = MovementType.valueOf(typeStr);
            
            if (model.getCategories().containsKey(name)) {
                JOptionPane.showMessageDialog(categoriesView, "La categoría '" + name + "' ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            MovementCategory newCategory = new MovementCategory(name, type);
            model.addCategory(newCategory);
            
            categoriesView.clearFields();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(categoriesView, 
                "Error al procesar el tipo de categoría.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Archivo: MovementController.java (Dentro de la clase MovementController)

    private void handleRemoveCategory(MovementCategoriesView categoriesView) {
        String categoryName = categoriesView.getListCategories().getSelectedValue();

        if (categoryName == null) {
            JOptionPane.showMessageDialog(categoriesView, 
                "Debe seleccionar una categoría para eliminar", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(categoriesView, 
            "¿Está seguro de eliminar la categoría '" + categoryName + "'?", 
            "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            MovementCategory categoryToRemove = model.getCategoryByName(categoryName);
            
            if (categoryToRemove != null) {
                model.removeCategory(categoryToRemove);
                JOptionPane.showMessageDialog(categoriesView, 
                    "Categoría eliminada: " + categoryName, 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(categoriesView, 
                    "Error al encontrar la categoría en el modelo", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCategoriesList(MovementCategoriesView categoriesView) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        
        List<String> categoryNames = model.getCategories().values().stream()
                                        .map(MovementCategory::getName)
                                        .sorted()
                                        .collect(Collectors.toList());
        
        for (String name : categoryNames) {
            listModel.addElement(name);
        }
        categoriesView.getListCategories().setModel(listModel);
    }

    @Override
    public void onNotify(List<MovementCategory> categories) {
        updateCategoriesView(categories);

        if (this.categoriesManagerView != null && this.categoriesManagerView.isVisible()) {
        updateCategoriesList(this.categoriesManagerView);
    }
    }
}
