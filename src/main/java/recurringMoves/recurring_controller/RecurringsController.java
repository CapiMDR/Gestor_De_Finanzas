package recurringMoves.recurring_controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import recurringMoves.recurring_model.RecurrenceType;
import recurringMoves.recurring_model.RecurringMove;
import recurringMoves.recurring_model.RecurringsModel;
import recurringMoves.recurring_view.RecurringsEditorView;
import recurringMoves.recurring_view.RecurringsView;

/**
 * Controlador principal encargado de coordinar la interacción entre la vista,
 * el modelo y la lógica de notificaciones de los pagos recurrentes.
 *
 * <p>
 * Administra la creación, edición, eliminación y monitoreo automático
 * de {@link RecurringMove} utilizando un {@link ScheduledExecutorService}.
 * </p>
 */
public class RecurringsController {

    /** Modelo que contiene todas las operaciones recurrentes. */
    private final RecurringsModel recurringsModel = new RecurringsModel();

    /** Vista principal encargada de mostrar los recordatorios recurrentes. */
    private final RecurringsView recurringsView = new RecurringsView(this, recurringsModel);

    /** Servicio ejecutor para revisar periódicamente los recordatorios. */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void showRecMovesView() {
        recurringsView.setVisible(true);
    }

    /**
     * Construye el controlador, inicializa la vista y comienza el monitoreo
     * periódico
     * de los recordatorios.
     */
    public RecurringsController() {
        recurringsView.setVisible(true);
        scheduler.scheduleAtFixedRate(this::watchRecurrings, 0, 1, TimeUnit.SECONDS);
    }

    // Revisando los recordatorios cada segundo y activando aquellos que deban ser
    // activados

    /**
     * Revisa todos los recordatorios ordenados y activa aquellos cuya fecha ya
     * pasó.
     * Como los recordatorios están ordenados, la revisión termina cuando uno aún no
     * debe ser disparado.
     */
    private void watchRecurrings() {
        for (RecurringMove recMove : recurringsModel.getRecurrings()) {
            if (recMove.shouldTrigger()) {
                triggerRecurring(recMove);
            } else {
                // Como los recordatorios están ordenados por su fecha, si el actual no ha
                // llegado a su tiempo, los demás tampoco
                break;
            }
        }
    }

    // Cuando se activa la notificación de un pago recurrente se elimina y se crea
    // la siguiente instancia según su frecuencia

    /**
     * Maneja la activación de un recordatorio recurrente.
     * <p>
     * El proceso consiste en:
     * </p>
     * <ul>
     * <li>Eliminar el recordatorio actual.</li>
     * <li>Crear su siguiente instancia dependiendo de la frecuencia.</li>
     * <li>Guardar los cambios.</li>
     * <li>Mostrar una alerta en la vista.</li>
     * </ul>
     *
     * @param recMove el recordatorio que debe ser activado
     */
    private void triggerRecurring(RecurringMove recMove) {
        recurringsModel.deleteRecurring(recMove);
        RecurringMove next = recMove.createNextOccurrence();
        recurringsModel.addRecurring(next);

        recurringsModel.saveRecurrings();

        javax.swing.SwingUtilities.invokeLater(() -> {
            showRecMoveAlert(recMove);
        });
    }

    private void showRecMoveAlert(RecurringMove recMove) {
        JOptionPane.showMessageDialog(recurringsView,
                recMove.getDescription(),
                recMove.getConcept() + " - Recordatorio",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Procesa la solicitud de creación de un nuevo recordatorio.
     *
     * @param concept     nombre del recordatorio
     * @param amount      monto asociado
     * @param description descripción opcional
     * @param initialDate fecha inicial
     * @param recurrence  tipo de repetición
     */
    public void handleRecurringAddition(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence) {
        if (!isValidRecurring(concept, amount, description, initialDate, recurrence))
            return;

        recurringsModel.addRecurring(concept, amount, description, initialDate, recurrence);
        recurringsModel.saveRecurrings();
    }

    /**
     * Valida los campos necesarios para crear o editar un recordatorio recurrente.
     *
     * @return true si los datos son válidos, false en caso contrario
     */
    private boolean isValidRecurring(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence) {
        if (concept == null || concept.isEmpty())
            return false;

        if (amount == null)
            return false;

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return false;

        if (amount.scale() > 2)
            return false;

        return true;
    }

    /**
     * Maneja la eliminación de un recordatorio recurrente.
     *
     * @param recMove el recordatorio a eliminar
     */
    public void handleRecurringDeletion(RecurringMove recMove) {
        recurringsModel.deleteRecurring(recMove);
        recurringsModel.saveRecurrings();
    }

    /**
     * Procesa la edición de un recordatorio recurrente dado uno antiguo y uno
     * nuevo.
     *
     * @param oldRecMove recordatorio original
     * @param newRecMove recordatorio ya editado
     */
    public void handleRecurringEdit(RecurringMove oldRecMove, RecurringMove newRecMove) {
        recurringsModel.editRecurring(oldRecMove, newRecMove);
        recurringsModel.saveRecurrings();
    }

    /**
     * Abre la ventana de edición para un recordatorio y procesa el resultado si el
     * usuario confirma los cambios.
     *
     * @param recMove recordatorio a editar
     */
    public void onEditRequest(RecurringMove recMove) {
        RecurringsEditorView editor = new RecurringsEditorView(null, recMove);
        editor.setVisible(true);

        RecurringMove editedRecMove = editor.getEditedRecMove();
        if (editedRecMove != null)
            handleRecurringEdit(recMove, editedRecMove);
    }
}
