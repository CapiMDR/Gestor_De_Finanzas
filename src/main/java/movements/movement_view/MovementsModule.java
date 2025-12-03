package movements.movement_view;

import accounts.account_model.Account;
import accounts.account_model.JsonDataHandler;
import movements.movement_controller.MovementController;
import movements.movement_model.CategoryManager;
import movements.movement_model.MovementManagerSubject;

public class MovementsModule {
    public static void initMovements(Account selectedAccount) {

        JsonDataHandler testDataHandler = new JsonDataHandler();

        MovementManagerSubject movementSubject = new MovementManagerSubject();

        CategoryManager movementModel = new CategoryManager(
                movementSubject,
                testDataHandler);

        MovementManagerView movementView = new MovementManagerView();

        MovementController movementController = new MovementController(
                movementModel,
                movementView,
                selectedAccount);
        movementView.setVisible(true);
    }
}