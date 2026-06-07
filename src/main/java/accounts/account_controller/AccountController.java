package accounts.account_controller;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountObserver;
import accounts.account_view.AccountEditView;
import accounts.account_view.AccountInterestView;
import accounts.account_view.AccountView;
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
    private AccountView view;

    /**
     * Constructor of the controller that initializes the view and registers the
     * observer.
     *
     * @param view Main view used to interact with the user.
     */
    public AccountController(AccountView view) {
        this.view = view;
        AccountManager.addObserver(this);
        AssignEvents();
    }

    /**
     * Assigns listeners to the view's buttons.
     */
    private void AssignEvents() {
        this.view.getBtnAccessAccount().addActionListener(e -> {
            accessAccount();
        });

        this.view.getBtnAddAccount().addActionListener(e -> {
            addAccount();
        });

        this.view.getBtnDeleteAccount().addActionListener(e -> {
            deleteAccount();
        });

        this.view.getBtnEditAccount().addActionListener(e -> {
            editAccount();
        });

        this.view.getBtnCalculateInterest().addActionListener(e -> {
            calculateInterest();
        });
    }

    /**
     * Accesses the selected account and opens the reports module.
     */
    public void accessAccount(){
        int selectedIndex = view.getListAccounts().getSelectedIndex();
        Account selectedAccount = AccountManager.getAccountByIndex(selectedIndex);
        if (selectedAccount == null) {
            return;
        }
        ReportsModule.initReportsModule(selectedAccount);
    }

    /**
     * Adds a new account using the data from the view. Validates the input,
     * creates the instance and updates the account list.
     */
    private void addAccount(){
        String name = view.getAccountName();
        String balanceStr = view.getInitialBalanceText();
        String typeString = view.getSelectedAccountType();
        String coinStr = view.getSelectedCurrency();

        boolean isEmpty = name.isEmpty() || balanceStr.isEmpty() || typeString == null || coinStr == null;

        if (isEmpty) {
            JOptionPane.showMessageDialog(view,
                    "Todos los campos deben estar llenos y las opciones deben estar seleccionadas.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            BigDecimal balance = new BigDecimal(balanceStr);

            if (balance.compareTo(BigDecimal.ZERO) < 0){
                throw new IllegalArgumentException("El saldo no puede ser negativo");
            }

            Account.AccountType type = Account.AccountType.valueOf(typeString.toUpperCase());
            Account.Coin coin = Account.Coin.valueOf(coinStr.toUpperCase());

            AccountManager.addAccount(name, type, coin, balance);

            JOptionPane.showMessageDialog(view,
                    "Cuenta agregada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            clearInputFields();

        } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view,
                        "El Saldo Inicial debe ser un número válido.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view,
                    ex.getMessage(),
                    "Error de Validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view,
                        "Error al procesar la cuenta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
        }  
    }

    /**
     * Deletes the selected account after confirming the action with the user.
     */
    private void deleteAccount(){
        int selectedIndex = view.getListAccounts().getSelectedIndex();

        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(view,
                    "Debe seleccionar una cuenta para eliminarla",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account selectedAccount = AccountManager.getAccountByIndex(selectedIndex);

        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(view,
                    "Error al obtener la cuenta seleccionada. Intente de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int dialogResult = JOptionPane.showConfirmDialog(view,
                "¿Estás seguro de que desea eliminar la cuenta '" + selectedAccount.getName() + "'?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            AccountManager.removeAccount(selectedAccount.getId());

            JOptionPane.showMessageDialog(view,
                    "Cuenta '" + selectedAccount.getName() + "' eliminada exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Starts the editing process of the selected account by opening the
     * corresponding view.
     */
    private void editAccount(){
        int selectedIndex = view.getListAccounts().getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(view,
                    "Debe seleccionar una cuenta para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account accountToEdit = AccountManager.getAccountByIndex(selectedIndex);
        if (accountToEdit != null) {
            showEditForm(accountToEdit);
        } else {
            JOptionPane.showMessageDialog(view,
                    "Error al obtener la cuenta seleccionada.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the input fields in the view.
     */
    private void clearInputFields() {
        view.txtNameAccount.setText("");
        view.txtInitialBalance.setText("");
        view.listAccountType.clearSelection();
        view.listCurrency.clearSelection();
    }

    /**
     * Shows the edit form for the selected account.
     *
     * @param accountToEdit Account to edit.
     */
    private void showEditForm(Account accountToEdit) {
        AccountEditView editView = new AccountEditView();
        editView.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        editView.getTxtNameAccount().setText(accountToEdit.getName());

        String currentType = accountToEdit.getType().toString();
        editView.getListAccountType().setSelectedValue(currentType, true);

        String currentCoin = accountToEdit.getCoin().toString();
        editView.getListCurrency().setSelectedValue(currentCoin, true);

        editView.getBtnConfirm().addActionListener(e -> {
            handleConfirmEdit(editView, accountToEdit);
        });

        editView.getBtnCancel().addActionListener(e -> {
            editView.dispose();
        });

        editView.setVisible(true);
    }

    /**
     * Verifies and applies the editing changes of an account.
     *
     * @param editView View used to edit the account.
     * @param accountToEdit Account being edited.
     */
    private void handleConfirmEdit(AccountEditView editView, Account accountToEdit) {
        String newName = editView.getTxtNameAccount().getText();
        String newTypeStr = editView.getListAccountType().getSelectedValue();
        String newCoinStr = editView.getListCurrency().getSelectedValue();

        if (newName.isEmpty() || newTypeStr == null || newCoinStr == null) {
            JOptionPane.showMessageDialog(editView,
                    "Debe completar todos los campos.",
                    "Error de Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Account.AccountType newType = Account.AccountType.valueOf(newTypeStr.toUpperCase());
            Account.Coin newCoin = Account.Coin.valueOf(newCoinStr.toUpperCase());

            AccountManager.editAccount(accountToEdit, newName, newType, newCoin);

            editView.dispose();
            JOptionPane.showMessageDialog(view,
                    "Cuenta '" + newName + "' actualizada.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(editView,
                    "Error de mapeo. Verifique que Tipo o Moneda seleccionados sean válidos.",
                    "Error de Configuración", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editView,
                    "Error inesperado al editar la cuenta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Calculates the compound interest of the selected account and shows the result
     * in the corresponding view.
     */
    private void calculateInterest(){
        int selectedIndex = view.getListAccounts().getSelectedIndex();
        
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(view,
                "Debe seleccionar una cuenta para calcular el interés",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Account selectedAccount = AccountManager.getAccountByIndex(selectedIndex);

        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(view,
                "No se pudo obtener la cuenta seleccionada",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AccountInterestView interestView = new AccountInterestView();
        interestView.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        interestView.getTxtInitialBalance().setText(selectedAccount.getCurrentBalance().toString());

        interestView.getBtnCalculate().addActionListener(e -> {
            try {
                BigDecimal initialBalance = selectedAccount.getCurrentBalance();

                String interestStr = interestView.getTxtInterest().getText();
                String timeStr = interestView.getTxtTime().getText();

                if (interestStr.isEmpty() || timeStr.isEmpty()) {
                    JOptionPane.showMessageDialog(interestView,
                        "Debe ingresar una tasa y un tiempo",
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                BigDecimal interestRate = new BigDecimal(interestStr);
                BigDecimal timeYears = new BigDecimal(timeStr);

                if (interestRate.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(interestView,
                        "La tasa debe ser mayor que cero",
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (timeYears.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(interestView,
                        "El tiempo debe ser mayor que cero",
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                BigDecimal r = interestRate.divide(BigDecimal.valueOf(100));
                BigDecimal onePlusR = r.add(BigDecimal.ONE);

                double exponent = timeYears.doubleValue();
                BigDecimal futureBalance = initialBalance.multiply(onePlusR.pow((int) exponent));

                interestView.getTxtFutureBalance().setText(futureBalance.toString());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(interestView,
                    "Error al calcular: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        interestView.setVisible(true);
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
