package filters.filter_view;

import accounts.account_model.Account;
import filters.filter_controller.FilterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Initializes and manages the Filters module.
 */
public class FilterModule {

    public static javafx.scene.Node loadForAccount(Account selectedAccount, Runnable onBack) {
        if (selectedAccount == null) return null;
        try {
            FXMLLoader loader = new FXMLLoader(FilterModule.class.getResource("/fxml/filters/filter.fxml"));
            Parent root = loader.load();

            FilterViewFX view = loader.getController();
            if (selectedAccount != null) view.setAccountName(selectedAccount.getName());
            FilterController controller = new FilterController();
            
            controller.setViewModule(view, selectedAccount);

            if (onBack != null) {
                view.setOnBack(() -> {
                    controller.dispose();
                    onBack.run();
                });
            }

            return root;
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger("GlobalExceptionHandler").error("Excepción detectada", e);
            return null;
        }
    }
}
