package accounts.account_controller;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountObserver;
import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;
import accounts.account_view.AccountEditView;
import accounts.account_view.AccountView;
import reports.ReportsModule;

public class AccountController implements AccountObserver {
    private AccountView view;

    public AccountController(AccountView view) {
        this.view = view;
        AccountManager.addObserver(this);
        AssignEvents();
    }

    private void AssignEvents() {

        this.view.getBtnAccessAccount().addActionListener(e -> {
            accessAccount();
        });

        this.view.getBtnAddAccount().addActionListener(e -> {

            String name = view.getAccountName();
            String balanceStr = view.getInitialBalanceText();
            String typeString = view.getSelectedAccountType();
            String coinStr = view.getSelectedCurrency();

            if (name.isEmpty() || balanceStr.isEmpty() || typeString == null || coinStr == null) {
                JOptionPane.showMessageDialog(view,
                        "Todos los campos deben estar llenos y las opciones deben estar seleccionadas.",
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                BigDecimal balance = new BigDecimal(balanceStr);
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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view,
                        "Error al procesar la cuenta: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.view.getBtnDeleteAccount().addActionListener(e -> {

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
        });

        this.view.getBtnEditAccount().addActionListener(e -> {
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
        });
    }

    public void addAccount(String name, AccountType type, Coin coin, BigDecimal balance) {
        AccountManager.addAccount(name, type, coin, balance);
    }

    public void removeAccount(int idAccount) {
        AccountManager.removeAccount(idAccount);
    }

    public void editAccount(Account account, String name, AccountType type, Coin coin) {
        AccountManager.editAccount(account, name, type, coin);
    }

    private void clearInputFields() {
        view.txtNameAccount.setText("");
        view.txtInitialBalance.setText("");
        view.listAccountType.clearSelection();
        view.listCurrency.clearSelection();
    }

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

    public void accessAccount() {
        int selectedIndex = view.getListAccounts().getSelectedIndex();
        Account selectedAccount = AccountManager.getAccountByIndex(selectedIndex);
        if (selectedAccount == null) {
            System.out.println("Seleccione una cuenta");
            return;
        }
        System.out.println("Accediendo a la cuenta " + selectedAccount.getName());
        ReportsModule.initReportsModule(selectedAccount);
    }

    @Override
    public void onNotify(List<Account> accountsList) {
        this.view.updateAccountList(accountsList);

    }
}
