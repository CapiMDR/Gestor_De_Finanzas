package filters.modelFilter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import accounts.account_model.Account;
import filters.controllerFilter.FilterController;
import filters.controllerFilter.FilterViewFX;

import java.util.HashMap;
import java.util.Map;

/**
 * Initializes and manages the Categories (Filters) module.
 */
public class CategoriesModule {
    
    private static Map<String, Stage> activeStages = new HashMap<>();

    public static void initCategories(Account selectedAccount) {
        if (selectedAccount == null) return;
        String accountId = selectedAccount.getName();
        if (activeStages.containsKey(accountId) && activeStages.get(accountId).isShowing()) {
            activeStages.get(accountId).toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(CategoriesModule.class.getResource("/fxml/filters/filter.fxml"));
            Parent root = loader.load();

            FilterViewFX view = loader.getController();
            FilterController controller = new FilterController();
            
            if (selectedAccount != null) {
                controller.setViewModule(view, selectedAccount);
            }

            Stage stage = new Stage();
            activeStages.put(accountId, stage);
            stage.setTitle("Filtros / Categorías");
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(CategoriesModule.class.getResource("/styles/app.css").toExternalForm());
            stage.setScene(scene);
            
            stage.setOnCloseRequest(e -> {
                activeStages.remove(accountId);
            });
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error de Carga");
            alert.setHeaderText("Error al abrir el módulo de Categorías");
            alert.setContentText(e.getMessage() != null ? e.getMessage() : e.toString());
            alert.showAndWait();
        }
    }
}