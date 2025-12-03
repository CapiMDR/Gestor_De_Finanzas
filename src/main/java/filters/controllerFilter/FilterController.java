/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filters.controllerFilter;

import accounts.account_model.Account;
import accounts.account_model.AccountObserver;
import filters.viewFilter.CategoriesView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;

import javax.swing.SwingUtilities;

public class FilterController implements AccountObserver {
    
    private CategoriesView view;
    private Account account;
    
    public void setViewModule(CategoriesView view, Account selectedAccount) {
        this.view = view;
        this.account = selectedAccount;
        loadCategoriesToView();
    }
    
    private void loadCategoriesToView() {
        if (account != null) {
            List<Movement> movements = account.getMovements();
            
            // Mapa para almacenar categorías y sus totales
            Map<String, BigDecimal> incomeCategories = new HashMap<>();
            Map<String, BigDecimal> expenseCategories = new HashMap<>();
            
            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;
            
            // Procesar movimientos
            for (Movement movement : movements) {
                
                String categoryName = "Sin categoría";
                MovementCategory categoryObj = movement.getCategory();
                if (categoryObj != null) {
                    categoryName = categoryObj.getName();
                }
                
                BigDecimal amount = movement.getAmount();
                
                if (amount.compareTo(BigDecimal.ZERO) > 0) {

                    totalIncome = totalIncome.add(amount);
                    BigDecimal currentTotal = incomeCategories.getOrDefault(categoryName, BigDecimal.ZERO);
                    incomeCategories.put(categoryName, currentTotal.add(amount));

                } else if (amount.compareTo(BigDecimal.ZERO) < 0) {

                    BigDecimal absAmount = amount.abs();
                    totalExpense = totalExpense.add(absAmount);
                    BigDecimal currentTotal = expenseCategories.getOrDefault(categoryName, BigDecimal.ZERO);
                    expenseCategories.put(categoryName, currentTotal.add(absAmount));
                }
            }

            // Hacer los valores effectively final
            final double totalIncomeValue = totalIncome.doubleValue();
            final double totalExpenseValue = totalExpense.doubleValue();

            // Actualizar la vista
            SwingUtilities.invokeLater(() -> {
                
                Map<String, Double> incomeCategoriesDouble = new HashMap<>();
                Map<String, Double> expenseCategoriesDouble = new HashMap<>();
                
                for (Map.Entry<String, BigDecimal> entry : incomeCategories.entrySet()) {
                    incomeCategoriesDouble.put(entry.getKey(), entry.getValue().doubleValue());
                }
                
                for (Map.Entry<String, BigDecimal> entry : expenseCategories.entrySet()) {
                    expenseCategoriesDouble.put(entry.getKey(), entry.getValue().doubleValue());
                }
                
                view.updateCategories(
                    incomeCategoriesDouble,
                    expenseCategoriesDouble,
                    totalIncomeValue,
                    totalExpenseValue
                );
            });
        }
    }
    
    @Override
    public void onNotify(List<Account> accountsList) {
        if (account != null) {
            for (Account acc : accountsList) {
                if (acc.getId() == account.getId()) {
                    account = acc;
                    loadCategoriesToView();
                    break;
                }
            }
        }
    }
    
    public void reloadCategories() {
        loadCategoriesToView();
    }
}