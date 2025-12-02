package movements.movement_model;

import java.util.ArrayList;
import java.util.HashMap;

import accounts.account_model.JsonDataHandler;

public class CategoryManager {
    private HashMap<String, MovementCategory> categories;
    private MovementManagerSubject subject;
    protected JsonDataHandler dataHandler;

    public CategoryManager(MovementManagerSubject subject, JsonDataHandler dataHandler) {
        this.subject = subject;
        this.dataHandler = dataHandler;
        this.categories = dataHandler.loadCategories();
        if (this.categories == null) {
            this.categories = new HashMap<>();
        }
    }

    public void addCategory(MovementCategory category) {
        categories.put(category.getName(), category);
        dataHandler.saveCategories(categories);
        notifyObservers();
    }

    public void removeCategory(MovementCategory category) {
        categories.remove(category.getName());
        dataHandler.saveCategories(categories);
        notifyObservers();
    }

    public MovementCategory getCategoryByName(String name) {
        return categories.get(name);
    }

    public void addOserver(MovementObserver observer) {
        subject.addObserver(observer);
    }

    public void removeObserver(MovementObserver observer) {
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
