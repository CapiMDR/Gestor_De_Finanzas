package movements.movement_model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import accounts.account_model.JsonDataHandler;

public class MovementManager {
    private List<Movement> movements;
    private HashMap<String, MovementCategory> categories;
    private MovementManagerSubject subject;
    protected JsonDataHandler dataHandler;

    public MovementManager(MovementManagerSubject subject, JsonDataHandler dataHandler) {
        this.subject = subject;
        this.dataHandler = dataHandler;
        this.movements = new ArrayList<>();
        this.categories = dataHandler.loadCategories();
        if (this.categories == null) {
            this.categories = new HashMap<>();
        }
    }

    public void addMovement(Movement movement) {
        movement.setIdMovement(generateUniqueId());
        movements.add(movement);
        notifyObservers();
    }

    private int generateUniqueId() {
        return movements.size() > 0 ? movements.get(movements.size() - 1).getIdMovement() + 1 : 1;
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

    public List<Movement> getMovements() {
        return movements;
    }

    public HashMap<String, MovementCategory> getCategories() {
        return categories;
    }

    public MovementManagerSubject getSubject() {
        return subject;
    }
}
