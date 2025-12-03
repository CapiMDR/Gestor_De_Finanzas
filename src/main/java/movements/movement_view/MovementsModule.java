package movements.movement_view;

import accounts.account_model.Account;
import accounts.account_model.JsonDataHandler;
import movements.movement_controller.MovementController;
import movements.movement_model.CategoryManager;
import movements.movement_model.MovementManagerSubject;

public class MovementsModule {
    public static void initMovements(Account selectedAccount) {

        // 1. SIMULACIÓN DE DEPENDENCIAS GLOBALES (AccountManager)
        JsonDataHandler testDataHandler = new JsonDataHandler();

        // 2. CREACIÓN DE LA CUENTA DE PRUEBA (Contexto)
        // **ELIMINAR:** Account testAccount = new Account(1, "Braulio regalo", ...);

        // **NUEVO:** OBTENER LA CUENTA REAL DEL MODELO (AccountManager)

        // 3. CREACIÓN DEL MODELO DE MOVIMIENTOS
        MovementManagerSubject movementSubject = new MovementManagerSubject();
        CategoryManager movementModel = new CategoryManager(
                movementSubject,
                testDataHandler);

        // 4. CREACIÓN DE LA VISTA
        MovementManagerView movementView = new MovementManagerView();

        // 5. CREACIÓN DEL CONTROLADOR (El cableado)
        // Ahora pasamos la REFERENCIA a la cuenta gestionada por accountModel
        MovementController movementController = new MovementController(
                movementModel,
                movementView,
                selectedAccount);

        // 6. MOSTRAR LA VISTA
        movementView.setVisible(true);
    }

    // Variables declaration - do not modify
    // ... (Todas tus variables privadas) ...
    // End of variables declaration
}
