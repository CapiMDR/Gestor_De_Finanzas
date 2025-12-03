package goals.goals_view;

import goals.goals_model.Goal;

/**
 * Interfaz para manejar acciones desencadenadas desde tarjetas individuales de metas.
 * Esto permite que el Controlador sepa QUÉ meta necesita ser editada, eliminada o vista.
 *
 * @autor Jose Pablo Martinez
 */

public interface GoalActionListener {
    void onViewDetails(Goal goal);

    void onEdit(Goal goal);

    void onDelete(Goal goal);
}