/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filters.controllerFilter;

import accounts.account_model.Account;
import accounts.account_model.AccountObserver;
import filters.viewFilter.CategoriesView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

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

            List<Movement> incomeMovements = new ArrayList<>();
            List<Movement> expenseMovements = new ArrayList<>();

            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;

            for (Movement movement : movements) {

                MovementCategory categoryObj = movement.getCategory();
                BigDecimal amount = movement.getAmount();

                if (categoryObj != null && categoryObj.getType() == MovementType.INCOME) {

                    totalIncome = totalIncome.add(amount);
                    incomeMovements.add(movement);

                } else if (categoryObj != null && categoryObj.getType() == MovementType.EXPENSE) {

                    BigDecimal absAmount = amount.abs();
                    totalExpense = totalExpense.add(absAmount);
                    expenseMovements.add(movement);
                }
            }

            final double totalIncomeValue = totalIncome.doubleValue();
            final double totalExpenseValue = totalExpense.doubleValue();

            SwingUtilities.invokeLater(() -> {
                view.updateCategories(
                    incomeMovements,
                expenseMovements,
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