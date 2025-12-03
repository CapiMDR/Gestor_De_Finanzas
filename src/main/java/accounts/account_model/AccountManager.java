package accounts.account_model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;

public class AccountManager {
    private static List<Account> accounts = new ArrayList<>();
    private static JsonDataHandler dataHandler = new JsonDataHandler();

    private AccountManager() {
        
    }

    public static void initAccountManager() {
        accounts = dataHandler.loadAccounts();
    }

    public static void loadInitialData() {
        notifyObservers();
    }

    public static void saveAccountsData() {
        dataHandler.saveAccounts(accounts);
    }

    public static void addAccount(String name, AccountType type, Coin coin, BigDecimal initialBalace) {
        Account newAccount = new Account(generateUniqueId(), name, type, coin, initialBalace);

        accounts.add(newAccount);
        saveAccountsData();
        notifyObservers();
    }

    public static void removeAccount(int id) {
        accounts.removeIf(account -> account.getId() == id);
        saveAccountsData();
        notifyObservers();
    }

    public static void editAccount(Account account, String name, AccountType type, Coin coin) {
        account.setName(name);
        account.setType(type);
        account.setCoin(coin);
        saveAccountsData();
        notifyObservers();
    }

    private static int generateUniqueId() {
        int maxId = 0;
        for (Account account : accounts) {
            if (account.getId() > maxId) {
                maxId = account.getId();
            }
        }
        return maxId + 1;
    }

    public static Account getAccountByIndex(int index) {
        System.out.println(accounts.size());
        if (index >= 0 && index < accounts.size()) {
            return accounts.get(index);
        }
        return null;
    }

    public static Account getAccountById(int id) {
        return accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static void addObserver(AccountObserver observer) {
        AccountManagerSubject.addObserver(observer);
    }

    public static void removeObserver(AccountObserver observer) {
        AccountManagerSubject.removeObserver(observer);
    }

    private static void notifyObservers() {
        AccountManagerSubject.notifyObservers(accounts);
    }

    public static List<Account> getAccounts() {
        return accounts;
    }


}
