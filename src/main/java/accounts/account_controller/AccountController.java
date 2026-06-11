package accounts.account_controller;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountObserver;
import accounts.account_view.AccountViewFX;
import reports.ReportsModule;

/**
 * Controller in charge of managing actions related to the account view.
 * Handles the creation, editing, deletion, access and interest calculation
 * of accounts, as well as the notification of changes coming from the
 * AccountManager.
 * 
 * @author Martín Jesús Pool Chuc
 */
public class AccountController implements AccountObserver {
    private AccountViewFX view;

    /**
     * Constructor of the controller that initializes the view and registers the
     * observer.
     *
     * @param view Main JavaFX view used to interact with the user.
     */
    public AccountController(AccountViewFX view) {
        this.view = view;
        AccountManager.addObserver(this);
        AssignEvents();
    }

    /**
     * Assigns listeners to the view's buttons.
     */
    private void AssignEvents() {
        this.view.getBtnAccessAccount().setOnAction(e -> accessAccount());
        this.view.getBtnAddAccount().setOnAction(e -> addAccount());
        this.view.getBtnDeleteAccount().setOnAction(e -> deleteAccount());
        this.view.getBtnEditAccount().setOnAction(e -> editAccount());
        this.view.getBtnCalculateInterest().setOnAction(e -> calculateInterest());
    }

    /**
     * Accesses the selected account and opens the reports module.
     */
    public void accessAccount() {
        int selectedIndex = view.getSelectedAccountIndex();
        Account selectedAccount = AccountManager.getAccountByIndex(selectedIndex);
        if (selectedAccount == null) return;
        ReportsModule.initReportsModule(selectedAccount);
    }

    /**
     * Adds a new account using the data from the view. Validates the input,
     * creates the instance and updates the account list.
     */
    private void addAccount() {
        String name = view.getAccountName();
        String balanceStr = view.getInitialBalanceText();
        String typeString = view.getSelectedAccountType();
        String coinStr = view.getSelectedCurrency();

        boolean isEmpty = name.isEmpty() || balanceStr.isEmpty() || typeString == null || coinStr == null;

        if (isEmpty) {
            JOptionPane.showMessageDialog(null,
                    "Todos los campos deben estar llenos y las opciones deben estar seleccionadas.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BigDecimal balance = new BigDecimal(balanceStr);

            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El saldo no puede ser negativo");
            }

            Account.AccountType type = Account.AccountType.valueOf(typeString.toUpperCase());
            Account.Coin coin = Account.Coin.valueOf(coinStr.toUpperCase());

            AccountManager.addAccount(name, type, coin, balance);

            JOptionPane.showMessageDialog(null,
                    "Cuenta agregada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            clearInputFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "El Saldo Inicial debe ser un número válido.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al procesar la cuenta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes the selected account after confirming the action with the user.
     */
    private void deleteAccount() {
        int selectedIndex = view.getSelectedAccountIndex();

        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar una cuenta para eliminarla",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account selectedAccount = AccountManager.getAccountByIndex(selectedIndex);

        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener la cuenta seleccionada. Intente de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int dialogResult = JOptionPane.showConfirmDialog(null,
                "¿Estás seguro de que desea eliminar la cuenta '" + selectedAccount.getName() + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            AccountManager.removeAccount(selectedAccount.getId());

            JOptionPane.showMessageDialog(null,
                    "Cuenta '" + selectedAccount.getName() + "' eliminada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Starts the editing process of the selected account by opening the
     * corresponding view.
     */
    private void editAccount() {
        int selectedIndex = view.getSelectedAccountIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar una cuenta para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account accountToEdit = AccountManager.getAccountByIndex(selectedIndex);
        if (accountToEdit != null) {
            AccountEditController editController = new AccountEditController(accountToEdit);
            editController.show();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener la cuenta seleccionada.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the input fields in the view.
     */
    private void clearInputFields() {
        view.clearForm();
    }


    /**
     * Calculates the compound interest of the selected account and shows the result
     * in the corresponding view.
     */
    private void calculateInterest() {
        int selectedIndex = view.getSelectedAccountIndex();

        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar una cuenta para calcular el interés",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account selectedAccount = AccountManager.getAccountByIndex(selectedIndex);

        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(null,
                    "No se pudo obtener la cuenta seleccionada",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AccountInterestController interestController = new AccountInterestController(selectedAccount);
        interestController.show();
    }

    /**
     * Method called when the AccountManager notifies a change in the account list.
     *
     * @param accountsList Updated list of accounts.
     */
    @Override
    public void onNotify(List<Account> accountsList) {
        this.view.updateAccountList(accountsList);
    }
}
