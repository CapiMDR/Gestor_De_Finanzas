package movements.movement_model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import accounts.account_model.JsonDataHandler;

/**
 * Manages the system's movement categories, allowing to
 * add, remove, retrieve and notify changes to observers.
 * Responsible for loading and saving data in JSON format through
 * {@link JsonDataHandler}.
 * 
 * @author Martín Jesús Pool Chuc
 */
public class CategoryManager {
    /**
     * Map containing the registered categories, using the category
     * name as the key.
     */
    private Map<String, MovementCategory> categories;


    protected JsonDataHandler dataHandler;

    /**
     * Constructs a CategoryManager using a subject for notification and
     * a data handler for persistence. Loads the categories from
     * storage if they exist.
     *
     * @param subject     Subject used to notify observers.
     * @param dataHandler Handler to load/save JSON data.
     */
    public CategoryManager(JsonDataHandler dataHandler) {
        this.dataHandler = dataHandler;
        this.dataHandler = dataHandler;
        this.categories = dataHandler.loadCategories();
        if (this.categories == null) {
            this.categories = new HashMap<>();
        }
    }

    public CategoryManager() {
        this.dataHandler = new JsonDataHandler();
        this.categories = dataHandler.loadCategories();
        if (this.categories == null) {
            this.categories = new HashMap<>();
        }
    }

    /**
     * Adds a new category and updates the persistent storage.
     *
     * @param category Category to add.
     */
    public void addCategory(MovementCategory category) {
        categories.put(category.getName(), category);
        dataHandler.saveCategories(categories);
        notifyObservers();
    }

    /**
     * Removes a category and updates the persistent storage.
     *
     * @param category Category to remove.
     */
    public void removeCategory(MovementCategory category) {
        categories.remove(category.getName());
        dataHandler.saveCategories(categories);
        notifyObservers();
    }

    /**
     * Gets a category by its name.
     *
     * @param name Name of the category.
     * @return Corresponding category or null if it doesn't exist.
     */
    public MovementCategory getCategoryByName(String name) {
        return categories.get(name);
    }

    /**
     * Registers an observer to receive updates.
     *
     * @param observer Observer to register.
     */
    public void addObserver(CategoryObserver observer) {
        MovementManagerSubject.addObserver(observer);
    }

    /**
     * Removes a registered observer.
     *
     * @param observer Observer to remove.
     */
    public void removeObserver(CategoryObserver observer) {
        MovementManagerSubject.removeObserver(observer);
    }

    public void notifyObservers() {
        MovementManagerSubject.notifyObservers(new ArrayList<>(categories.values()));
    }

    public Map<String, MovementCategory> getCategories() {
        return categories;
    }


}

