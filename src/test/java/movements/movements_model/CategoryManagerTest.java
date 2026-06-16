package movements.movements_model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import config.AppConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.mockStatic;

import movements.movement_model.CategoryManager;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import accounts.account_model.JsonDataHandler;
class CategoryManagerTest {

    private CategoryManager categoryManager;

    private JsonDataHandler dataHandler;

    private MockedStatic<AppConfig> mockedAppConfig;
    private Path tempCategoriesFile;

    @BeforeEach
    void setUp() throws IOException {
        tempCategoriesFile = Files.createTempFile("test_categories_", ".json");
        mockedAppConfig = mockStatic(AppConfig.class);
        mockedAppConfig.when(AppConfig::getCategoriesFilePath).thenReturn(tempCategoriesFile.toString());

        dataHandler = new JsonDataHandler(); 
        
        categoryManager = new CategoryManager(dataHandler);
        
        categoryManager.getCategories().clear();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockedAppConfig.close();
        Files.deleteIfExists(tempCategoriesFile);
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

    @Test
    void testEmptyConstructor() {
        CategoryManager emptyManager = new CategoryManager();
        assertNotNull(emptyManager.getCategories());
    }

    @Test
    void testConstructorWithNullCategories() {
        JsonDataHandler mockHandler = org.mockito.Mockito.mock(JsonDataHandler.class);
        org.mockito.Mockito.when(mockHandler.loadCategories()).thenReturn(null);
        
        CategoryManager nullCategoriesManager = new CategoryManager(mockHandler);
        assertNotNull(nullCategoriesManager.getCategories());
        assertTrue(nullCategoriesManager.getCategories().isEmpty());
    }

    @Test
    void testAddRemoveObserver() {
        movements.movement_model.CategoryObserver mockObserver = org.mockito.Mockito.mock(movements.movement_model.CategoryObserver.class);
        
        categoryManager.addObserver(mockObserver);
        categoryManager.notifyObservers();
        
        org.mockito.Mockito.verify(mockObserver, org.mockito.Mockito.times(1)).onNotify(org.mockito.Mockito.anyList());
        
        categoryManager.removeObserver(mockObserver);
    }
}