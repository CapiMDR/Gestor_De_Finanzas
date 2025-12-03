package movements.movement_model;

import java.util.ArrayList;
import java.util.HashMap;

import accounts.account_model.JsonDataHandler;

/**
 * Administra las categorías de movimientos del sistema, permitiendo
 * agregar, eliminar, obtener y notificar cambios a los observadores.
 * Se encarga de cargar y guardar los datos en formato JSON mediante
 * {@link JsonDataHandler}.
 * @author Martín Jesús Pool Chuc
 */
public class CategoryManager {
    /**
     * Mapa que contiene las categorías registradas, usando el nombre
     * de la categoría como clave.
     */
    private HashMap<String, MovementCategory> categories;

    /**
     * Sujeto responsable de notificar a los observadores cuando existe
     * un cambio en las categorías.
     */
    private MovementManagerSubject subject;
    protected JsonDataHandler dataHandler;

    /**
     * Construye un CategoryManager usando un sujeto para notificación y
     * un manejador de datos para persistencia. Carga las categorías desde
     * almacenamiento si existen.
     *
     * @param subject      Sujeto que se usa para notificar observadores.
     * @param dataHandler  Manejador para cargar/guardar datos JSON.
     */
    public CategoryManager(MovementManagerSubject subject, JsonDataHandler dataHandler) {
        this.subject = subject;
        this.dataHandler = dataHandler;
        this.categories = dataHandler.loadCategories();
        if (this.categories == null) {
            this.categories = new HashMap<>();
        }
    }

    /**
     * Agrega una nueva categoría y actualiza el almacenamiento persistente.
     *
     * @param category Categoría a agregar.
     */
    public void addCategory(MovementCategory category) {
        categories.put(category.getName(), category);
        dataHandler.saveCategories(categories);
        notifyObservers();
    }

    /**
     * Elimina una categoría y actualiza el almacenamiento persistente.
     *
     * @param category Categoría a eliminar.
     */
    public void removeCategory(MovementCategory category) {
        categories.remove(category.getName());
        dataHandler.saveCategories(categories);
        notifyObservers();
    }

    /**
     * Obtiene una categoría por su nombre.
     *
     * @param name Nombre de la categoría.
     * @return Categoría correspondiente o null si no existe.
     */
    public MovementCategory getCategoryByName(String name) {
        return categories.get(name);
    }

    /**
     * Registra un observador para recibir actualizaciones.
     *
     * @param observer Observador a registrar.
     */
    public void addOserver(CategoryObserver observer) {
        subject.addObserver(observer);
    }

    /**
     * Elimina un observador registrado.
     *
     * @param observer Observador a remover.
     */
    public void removeObserver(CategoryObserver observer) {
        subject.removeObserver(observer);
    }

    public void notifyObservers() {
        subject.notifyObservers(new ArrayList<>(categories.values()));
    }

    public HashMap<String, MovementCategory> getCategories() {
        return categories;
    }

    public MovementManagerSubject getSubject() {
        return subject;
    }
}
