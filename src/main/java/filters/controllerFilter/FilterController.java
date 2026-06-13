/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filters.controllerFilter;

import filters.viewFilter.FilterViewFX;
import accounts.account_model.Account;
import accounts.account_model.AccountObserver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

import javafx.application.Platform;

/**
 * Controller in charge of managing the categories view filter.
 * Handles the calculation of income and expenses based on the account's movements.
 */
public class FilterController implements AccountObserver {

    private FilterViewFX view;
    private Account account;

    /**
     * Initializes the controller with the view and account.
     *
     * @param view            the FilterViewFX to control
     * @param selectedAccount the account from which to pull movements
     */
    public void setViewModule(FilterViewFX view, Account selectedAccount) {
        this.view = view;
        this.account = selectedAccount;
        loadCategoriesToView();
    }

    /**
     * Calculates the total income and expenses and updates the view.
     */
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

            Platform.runLater(() -> {
                view.updateCategories(
                    incomeMovements,
                    expenseMovements,
                    totalIncomeValue,
                    totalExpenseValue
                );
            });
        }
    }

    /**
     * Observer pattern method executed when the account lists change.
     *
     * @param accountsList the updated list of accounts
     */
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

    /**
     * Reloads categories and updates the view manually.
     */
    public void reloadCategories() {
        loadCategoriesToView();
    }

    /**
     * Unregisters this controller as an observer to prevent memory leaks.
     */
    public void dispose() {
        accounts.account_model.AccountManager.removeObserver(this);
    }
}