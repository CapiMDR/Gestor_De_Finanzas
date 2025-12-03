package accounts.account_model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;

/**
 * Gestiona las cuentas del sistema, permitiendo cargarlas, guardarlas,
 * agregarlas, editarlas, eliminarlas y notificar observadores cuando hay cambios.
 * @author Martín Jesús Pool Chuc
 */
public class AccountManager {
    /** Lista estática que contiene todas las cuentas. */
    private static List<Account> accounts = new ArrayList<>();
    /** Manejador para cargar y guardar datos en formato JSON. */
    private static JsonDataHandler dataHandler = new JsonDataHandler();

    /**
     * Constructor privado para evitar instanciación.
     */
    private AccountManager() {
        
    }

    /**
     * Inicializa el administrador cargando las cuentas desde el origen de datos.
     */
    public static void initAccountManager() {
        accounts = dataHandler.loadAccounts();
    }

    /**
     * Carga los datos iniciales y notifica a los observadores.
     */
    public static void loadInitialData() {
        notifyObservers();
    }

    /**
     * Guarda todas las cuentas actuales mediante el manejador JSON.
     */
    public static void saveAccountsData() {
        dataHandler.saveAccounts(accounts);
    }

    /**
     * Agrega una nueva cuenta con los parámetros especificados.
     *
     * @param name nombre de la cuenta
     * @param type tipo de cuenta {@link AccountType}
     * @param coin moneda usada {@link Coin}
     * @param initialBalace balance inicial
     */
    public static void addAccount(String name, AccountType type, Coin coin, BigDecimal initialBalace) {
        Account newAccount = new Account(generateUniqueId(), name, type, coin, initialBalace);

        accounts.add(newAccount);
        saveAccountsData();
        notifyObservers();
    }

    /**
     * Elimina una cuenta según el id proporcionado.
     *
     * @param id identificador único de la cuenta
     */
    public static void removeAccount(int id) {
        accounts.removeIf(account -> account.getId() == id);
        saveAccountsData();
        notifyObservers();
    }

    /**
     * Edita una cuenta existente cambiando su nombre, tipo y moneda.
     *
     * @param account cuenta a editar
     * @param name nuevo nombre
     * @param type nuevo tipo de cuenta
     * @param coin nueva moneda
     */
    public static void editAccount(Account account, String name, AccountType type, Coin coin) {
        account.setName(name);
        account.setType(type);
        account.setCoin(coin);
        saveAccountsData();
        notifyObservers();
    }

    /**
     * Genera un ID único basado en el ID más alto actual.
     *
     * @return un nuevo ID único
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
     * Retorna una cuenta según su índice en la lista.
     *
     * @param index índice de la cuenta
     * @return la cuenta encontrada o null si está fuera de rango
     */
    public static Account getAccountByIndex(int index) {
        System.out.println(accounts.size());
        if (index >= 0 && index < accounts.size()) {
            return accounts.get(index);
        }
        return null;
    }

    /**
     * Busca una cuenta por su ID.
     *
     * @param id identificador de la cuenta
     * @return la cuenta encontrada o null si no existe
     */
    public static Account getAccountById(int id) {
        return accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Agrega un observador del administrador de cuentas.
     *
     * @param observer observador a agregar
     */
    public static void addObserver(AccountObserver observer) {
        AccountManagerSubject.addObserver(observer);
    }

    /**
     * Elimina un observador previamente registrado.
     *
     * @param observer observador a eliminar
     */
    public static void removeObserver(AccountObserver observer) {
        AccountManagerSubject.removeObserver(observer);
    }

    /**
     * Notifica a todos los observadores enviando la lista actual de cuentas.
     */
    private static void notifyObservers() {
        AccountManagerSubject.notifyObservers(accounts);
    }

    /**
     * Obtiene la lista de cuentas almacenadas.
     *
     * @return lista de cuentas
     */
    public static List<Account> getAccounts() {
        return accounts;
    }


}
