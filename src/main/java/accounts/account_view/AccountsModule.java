package accounts.account_view;

import accounts.account_controller.AccountController;
import accounts.account_model.AccountManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the Accounts JavaFX module.
 * Loads {@code account.fxml}, instantiates {@link AccountViewFX} via FXMLLoader,
 * then creates and wires {@link AccountController} to the view.
 *
 * The controller wires all button events internally via {@code AssignEvents()},
 * preserving the original Observer pattern architecture.
 */
public class AccountsModule {

    @SuppressWarnings("unused")
    private static AccountController accountController;
    private static Stage activeStage;

    /**
     * Initializes and displays the Accounts management window.
     * Should be called from the main application navigation.
     */
    public static void initAccountsModule() {
        if (activeStage != null && activeStage.isShowing()) {
            activeStage.toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                AccountsModule.class.getResource("/fxml/accounts/account.fxml"));

            Parent root = loader.load();

            // FXMLLoader created AccountViewFX and injected @FXML fields
            AccountViewFX view = loader.getController();

            // Controller registers itself as Observer and wires button events
            accountController = new AccountController(view);

            // Seed the list with current data (Observer fires on next change)
            view.updateAccountList(AccountManager.getAccounts());

            activeStage = new Stage();
            activeStage.setTitle("Gestión de Cuentas");
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(AccountsModule.class.getResource("/styles/app.css").toExternalForm());
            activeStage.setScene(scene);
            activeStage.setMaximized(true);
            activeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
