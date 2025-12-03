package accounts.account_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa el patrón Subject del patrón Observer para manejar
 * observadores relacionados con los cambios en la lista de cuentas.
 * @author Martín Jesús Pool Chuc
 */
public class AccountManagerSubject {

    /** Lista estática de observadores registrados. */
    private static List<AccountObserver> observers = new ArrayList<>();

    /**
     * Agrega un observador a la lista de observadores registrados.
     *
     * @param observer el observador que será agregado
     */
    public static void addObserver(AccountObserver observer) {
        observers.add(observer);
    }

    /**
     * Elimina un observador previamente registrado.
     *
     * @param observer el observador que será removido
     */
    public static void removeObserver(AccountObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica a todos los observadores enviando la lista de cuentas actualizada.
     *
     * @param accountsList lista de cuentas que será enviada a los observadores
     */
    public static void notifyObservers(List<Account> accountsList) {
        for (AccountObserver observer : observers) {
            observer.onNotify(accountsList);
        }
    }

}
