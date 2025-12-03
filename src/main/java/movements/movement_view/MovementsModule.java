package movements.movement_view;

import accounts.account_model.Account;
import accounts.account_model.JsonDataHandler;
import movements.movement_controller.MovementController;
import movements.movement_model.CategoryManager;
import movements.movement_model.MovementManagerSubject;

/**
 * Clase principal del módulo de movimientos.
 * Se encarga de inicializar los componentes necesarios para gestionar
 * los movimientos de una cuenta seleccionada, incluyendo modelo, vista y controlador.
 * @author Capi Madera de Regil
 */
public class MovementsModule {

    /**
     * Inicializa todo el módulo de movimientos utilizando la cuenta seleccionada.
     * Configura el Subject para notificaciones, el modelo que gestiona las categorías,
     * la vista y el controlador encargado de coordinar la interacción.
     *
     * @param selectedAccount la cuenta sobre la cual se gestionarán los movimientos
     */
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