package movements.movement_view;

import accounts.account_model.Account;
import accounts.account_model.JsonDataHandler;
import movements.movement_controller.MovementController;
import movements.movement_model.CategoryManager;
import movements.movement_model.MovementManagerSubject;

/**
 * Main class of the movements module.
 * Responsible for initializing the necessary components to manage
 * the movements of a selected account, including model, view and controller.
 * @author Capi Madera de Regil
 */
public class MovementsModule {
    
    @SuppressWarnings("unused")
    private static MovementController movementController;

    /**
     * Initializes the entire movements module using the selected account.
     * Configures the Subject for notifications, the model that manages categories,
     * the view and the controller responsible for coordinating the interaction.
     *
     * @param selectedAccount the account on which the movements will be managed
     */
    public static void initMovements(Account selectedAccount) {

        JsonDataHandler testDataHandler = new JsonDataHandler();

        MovementManagerSubject movementSubject = new MovementManagerSubject();

        CategoryManager movementModel = new CategoryManager(
                movementSubject,
                testDataHandler);

        MovementManagerView movementView = new MovementManagerView();

        movementController = new MovementController(
                movementModel,
                movementView,
                selectedAccount);
        movementView.setVisible(true);
    }
}