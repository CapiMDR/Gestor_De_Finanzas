package movements.movements_model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import movements.movement_model.CategoryManager;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementManagerSubject;
import movements.movement_model.MovementCategory.MovementType;
import accounts.account_model.JsonDataHandler;
class CategoryManagerTest {

    private CategoryManager categoryManager;
    private MovementManagerSubject subject;
    private JsonDataHandler dataHandler;

    @BeforeEach
    void setUp() {
        subject = new MovementManagerSubject();
        dataHandler = new JsonDataHandler(); 
        
        categoryManager = new CategoryManager(subject, dataHandler);
        
        categoryManager.getCategories().clear();
    }

    @Test
    void testAddCategory() {
        MovementCategory newCategory = new MovementCategory("Comida", MovementType.EXPENSE);

        categoryManager.addCategory(newCategory);

        MovementCategory retrieved = categoryManager.getCategoryByName("Comida");
        assertNotNull(retrieved, "La categoría debería existir en el manager");
        assertEquals(MovementType.EXPENSE, retrieved.getType(), "El tipo de categoría debe coincidir");
    }

    @Test
    void testRemoveCategory() {
        MovementCategory cat = new MovementCategory("Sueldo", MovementType.INCOME);
        categoryManager.addCategory(cat);
        
        assertNotNull(categoryManager.getCategoryByName("Sueldo"));

        categoryManager.removeCategory(cat);

        assertNull(categoryManager.getCategoryByName("Sueldo"), "La categoría no debería existir después de borrarse");
    }

    @Test
    void testGetCategoryByNameReturnsNullIfNotFound() {
        MovementCategory result = categoryManager.getCategoryByName("Inexistente");
        assertNull(result, "Debería devolver null si la categoría no existe");
    }
    
    @Test
    void testCategoryOverwrite() {
        categoryManager.addCategory(new MovementCategory("Test", MovementType.INCOME));
        
        categoryManager.addCategory(new MovementCategory("Test", MovementType.EXPENSE));
        
        MovementCategory result = categoryManager.getCategoryByName("Test");
        assertEquals(MovementType.EXPENSE, result.getType(), "Debería actualizarse la categoría existente");
    }
}