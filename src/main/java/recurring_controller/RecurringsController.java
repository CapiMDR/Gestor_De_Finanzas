package recurring_controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import recurring_model.RecurrenceType;
import recurring_model.RecurringMove;
import recurring_model.RecurringsModel;
import recurring_view.RecurringsEditorView;
import recurring_view.RecurringsView;

public class RecurringsController {

    private final RecurringsModel recurringsModel = new RecurringsModel();
    private final RecurringsView recurringsView = new RecurringsView(this, recurringsModel);
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public RecurringsController() {
        recurringsView.setVisible(true);
        scheduler.scheduleAtFixedRate(this::watchRecurrings, 0, 1, TimeUnit.SECONDS);
    }

    // Revisando los recordatorios cada segundo y activando aquellos que deban ser
    // activados
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
    private void triggerRecurring(RecurringMove recMove) {
        recurringsModel.deleteRecurring(recMove);
        RecurringMove next = recMove.createNextOccurrence();
        recurringsModel.addRecurring(next);

        recurringsModel.saveRecurrings();

        javax.swing.SwingUtilities.invokeLater(() -> {
            recurringsView.showRecMoveAlert(recMove);
        });
    }

    public void handleRecurringAddition(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence) {
        if (!isValidRecurring(concept, amount, description, initialDate, recurrence))
            return;

        recurringsModel.addRecurring(concept, amount, description, initialDate, recurrence);
        recurringsModel.saveRecurrings();
    }

    private boolean isValidRecurring(String concept, BigDecimal amount, String description,
            LocalDateTime initialDate, RecurrenceType recurrence) {
        if (concept == null || concept.isEmpty())
            return false;

        // Validate BigDecimal
        if (amount == null)
            return false;

        // Amount must be positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return false;

        // (Optional) Ensure scale is reasonable, e.g. max 2 decimal places
        if (amount.scale() > 2)
            return false;

        return true;
    }

    public void handleRecurringDeletion(RecurringMove recMove) {
        recurringsModel.deleteRecurring(recMove);
        recurringsModel.saveRecurrings();
    }

    public void handleRecurringEdit(RecurringMove oldRecMove, RecurringMove newRecMove) {
        recurringsModel.editRecurring(oldRecMove, newRecMove);
        recurringsModel.saveRecurrings();
    }

    public void onEditRequest(RecurringMove recMove) {
        RecurringsEditorView editor = new RecurringsEditorView(null, recMove);
        editor.setVisible(true);

        RecurringMove editedRecMove = editor.getEditedRecMove();
        if (editedRecMove != null)
            handleRecurringEdit(recMove, editedRecMove);
    }
}
