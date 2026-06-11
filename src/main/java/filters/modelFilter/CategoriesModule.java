package filters.modelFilter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import accounts.account_model.Account;
import filters.controllerFilter.FilterController;
import filters.controllerFilter.FilterViewFX;

import java.io.IOException;

/**
 * Module in charge of initializing the categories/filters view via JavaFX.
 */
public class CategoriesModule {
    
    public static void initCategories(Account selectedAccount) {
        try {
            FXMLLoader loader = new FXMLLoader(CategoriesModule.class.getResource("/fxml/filter.fxml"));
            Parent root = loader.load();

            FilterViewFX view = loader.getController();
            FilterController controller = new FilterController();
            
            if (selectedAccount != null) {
                controller.setViewModule(view, selectedAccount);
                view.setController(controller);
            }

            Stage stage = new Stage();
            stage.setTitle("Filtros / Categorías");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}