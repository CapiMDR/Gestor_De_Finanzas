package accounts.account_model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the system's accounts, allowing them to be loaded, saved,
 * added, edited, deleted and notifying observers when there are changes.
 *
 * @author Martín Jesús Pool Chuc
 */
public class AccountManager {
    private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);
    /** Static list that contains all accounts. */
    private static List<Account> accounts = new ArrayList<>();
    /** Handler to load and save data in JSON format. */
    private static JsonDataHandler dataHandler = new JsonDataHandler();

    /**
     * Private constructor to prevent instantiation.
     */
    private AccountManager() {
        
    }

    /**
     * Initializes the manager by loading the accounts from the data source.
     */
    public static void initAccountManager() {
        accounts = dataHandler.loadAccounts();
        logger.info("AccountManager initialized — {} account(s) loaded.", accounts.size());
    }

    /**
     * Loads the initial data and notifies the observers.
     */
    public static void loadInitialData() {
        notifyObservers();
    }

    /**
     * Saves all current accounts using the JSON handler.
     */
    public static void saveAccountsData() {
        dataHandler.saveAccounts(accounts);
    }

    /**
     * Adds a new account with the specified parameters.
     *
     * @param name account name
     * @param type account type {@link AccountType}
     * @param coin currency used {@link Coin}
     * @param initialBalance initial balance
     */
    public static void addAccount(String name, AccountType type, Coin coin, BigDecimal initialBalance) {
        Account newAccount = new Account(generateUniqueId(), name, type, coin, initialBalance);

        accounts.add(newAccount);
        saveAccountsData();
        notifyObservers();
        logger.info("Account added: '{}' (id={}, type={}).", name, newAccount.getId(), type);
    }

    /**
     * Deletes an account according to the provided id.
     *
     * @param id unique identifier of the account
     */
    public static void removeAccount(int id) {
        accounts.removeIf(account -> account.getId() == id);
        saveAccountsData();
        notifyObservers();
        logger.info("Account removed: id={}.", id);
    }

    /**
     * Edits an existing account changing its name, type and currency.
     *
     * @param account account to edit
     * @param name new name
     * @param type new account type
     * @param coin new currency
     */
    public static void editAccount(Account account, String name, AccountType type, Coin coin) {
        account.setName(name);
        account.setType(type);
        account.setCoin(coin);
        saveAccountsData();
        notifyObservers();
        logger.info("Account edited: id={}, new name='{}'.", account.getId(), name);
    }

    /**
     * Generates a unique ID based on the highest current ID.
     *
     * @return a new unique ID
     */
    private static int generateUniqueId() {
        int maxId = 0;
        for (Account account : accounts) {
            if (account.getId() > maxId) {
                maxId = account.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Returns an account according to its index in the list.
     *
     * @param index index of the account
     * @return the found account or null if it is out of range
     */
    public static Account getAccountByIndex(int index) {
        logger.debug("getAccountByIndex called - accounts size: {}", accounts.size());
        if (index >= 0 && index < accounts.size()) {
            return accounts.get(index);
        }
        return null;
    }

    /**
     * Finds an account by its ID.
     *
     * @param id account identifier
     * @return the found account or null if it doesn't exist
     */
    public static Account getAccountById(int id) {
        return accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds an observer of the account manager.
     *
     * @param observer observer to add
     */
    public static void addObserver(AccountObserver observer) {
        AccountManagerSubject.addObserver(observer);
    }

    /**
     * Removes a previously registered observer.
     *
     * @param observer observer to remove
     */
    public static void removeObserver(AccountObserver observer) {
        AccountManagerSubject.removeObserver(observer);
    }

    /**
     * Notifies all observers by sending the current list of accounts.
     */
    private static void notifyObservers() {
        AccountManagerSubject.notifyObservers(accounts);
    }

    /**
     * Gets the list of stored accounts.
     *
     * @return list of accounts
     */
    public static List<Account> getAccounts() {
        return accounts;
    }


}
