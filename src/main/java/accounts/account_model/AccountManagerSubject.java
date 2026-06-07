package accounts.account_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the Subject pattern from the Observer pattern to handle
 * observers related to changes in the account list.
 *
 * @author Martín Jesús Pool Chuc
 */
public class AccountManagerSubject {

    /** Static list of registered observers. */
    private static List<AccountObserver> observers = new ArrayList<>();

    /**
     * Adds an observer to the list of registered observers.
     *
     * @param observer the observer to be added
     */
    public static void addObserver(AccountObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes a previously registered observer.
     *
     * @param observer the observer to be removed
     */
    public static void removeObserver(AccountObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers by sending the updated list of accounts.
     *
     * @param accountsList list of accounts that will be sent to the observers
     */
    public static void notifyObservers(List<Account> accountsList) {
        for (AccountObserver observer : observers) {
            observer.onNotify(accountsList);
        }
    }

}
