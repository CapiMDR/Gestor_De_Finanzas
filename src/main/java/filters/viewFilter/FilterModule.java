package filters.viewFilter;

import accounts.account_model.Account;
import filters.controllerFilter.FilterController;
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
            if (onBack != null) view.setOnBack(onBack);
            FilterController controller = new FilterController();
            
            controller.setViewModule(view, selectedAccount);

            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
