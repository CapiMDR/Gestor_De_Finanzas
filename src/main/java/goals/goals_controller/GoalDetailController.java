package goals.goals_controller;

import goals.goals_model.Goal;
import goals.goals_view.GoalDetailView;

/**
 * Controlador específicamente encargado de gestionar la Vista de Detalle de una Meta.
 * Maneja la lógica para mostrar el progreso de una meta específica.
 * 
 * @author Jose Pablo Martinez
 */

public class GoalDetailController {

    private final GoalDetailView view;

    public GoalDetailController(GoalDetailView view) {
        this.view = view;
    }

    /**
     * Recibe una solicitud para mostrar los detalles de una meta y actualiza la vista.
     * 
     * @param objGoal El objeto Meta a visualizar.
     */

    public void showDetails(Goal objGoal) {
        if (objGoal != null) {
            // Update UI Components
            view.showProgress(objGoal);

            if (!view.isVisible()) {
                view.setVisible(true);
            }
        }
    }
}