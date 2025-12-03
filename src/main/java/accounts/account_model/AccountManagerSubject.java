package accounts.account_model;

import java.util.ArrayList;
import java.util.List;

public class AccountManagerSubject {
    private static List<AccountObserver> observers = new ArrayList<>();

    public static void addObserver(AccountObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(AccountObserver observer) {
        observers.remove(observer);
    }

    public static void notifyObservers(List<Account> accountsList) {
        for (AccountObserver observer : observers) {
            observer.onNotify(accountsList);
        }
    }

}
