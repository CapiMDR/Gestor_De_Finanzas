package recurring_controller;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        for (RecurringMove reminder : recurringsModel.getRecurrings()) {
            if (reminder.shouldTrigger()) {
                triggerRecurring(reminder);
            } else {
                // Como los recordatorios están ordenados por su fecha, si el actual no ha
                // llegado a su tiempo, los demás tampoco
                break;
            }
        }
    }

    private void triggerRecurring(RecurringMove recMove) {
        recMove.setTriggered(true);
        javax.swing.SwingUtilities.invokeLater(() -> {
            recurringsView.showRecMoveAlert(recMove);
        });
    }

    public void handleRecurringrAddition(String name, String message, LocalDateTime date) {
        if (!isValidRecurring(name, message, date))
            return;

        recurringsModel.addRecurring(name, message, date);
        recurringsModel.saveRecurrings();
    }

    private boolean isValidRecurring(String name, String message, LocalDateTime date) {
        if (name == null || name.isEmpty())
            return false;
        if (date == null)
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
