package reminder_controller;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import reminder_model.RemindersModel;
import reminder_model.Reminder;
import reminder_view.RemindersEditorView;
import reminder_view.RemindersView;

/**
 * Controlador principal encargado de coordinar la interacción entre el modelo
 * de
 * recordatorios, las vistas y la lógica de verificación periódica.
 * 
 * <p>
 * Se encarga de:
 * </p>
 * <ul>
 * <li>Escuchar solicitudes de la vista para agregar, editar o eliminar
 * recordatorios.</li>
 * <li>Disparar recordatorios cuando su fecha programada se cumple.</li>
 * <li>Sincronizar automáticamente los cambios con el almacenamiento en
 * JSON.</li>
 * </ul>
 */
public class RemindersController {

    /** Modelo que gestiona la colección de recordatorios. */
    private final RemindersModel remindersModel = new RemindersModel();

    /** Vista principal donde se muestran los recordatorios. */
    private final RemindersView remindersView = new RemindersView(this, remindersModel);

    /** Ejecutador programado para revisar periódicamente los recordatorios. */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Crea el controlador, muestra la vista principal y programa la tarea
     * periódica de vigilancia de recordatorios.
     */
    public RemindersController() {
        scheduler.scheduleAtFixedRate(this::watchReminders, 0, 1, TimeUnit.SECONDS);
    }

    public void showRemindersView() {
        remindersView.setVisible(true);
    }

    /**
     * Revisa los recordatorios cada segundo y activa aquellos cuya fecha haya sido
     * alcanzada.
     * 
     * <p>
     * Como los recordatorios están ordenados cronológicamente, si uno aún no debe
     * dispararse, los siguientes tampoco.
     * </p>
     */
    private void watchReminders() {
        for (Reminder reminder : remindersModel.getReminders()) {
            if (reminder.shouldTrigger()) {
                triggerReminder(reminder);
            } else {
                // Como los recordatorios están ordenados por su fecha, si el actual no ha
                // llegado a su tiempo, los demás tampoco
                break;
            }
        }
    }

    /**
     * Activa un recordatorio mostrando su alerta en la interfaz gráfica.
     * 
     * @param reminder Recordatorio que debe ser disparado.
     */
    private void triggerReminder(Reminder reminder) {
        reminder.setTriggered(true);
        javax.swing.SwingUtilities.invokeLater(() -> {
            showReminderAlert(reminder);
        });
    }

    private void showReminderAlert(Reminder reminder) {
        JOptionPane.showMessageDialog(remindersView,
                reminder.getMessage(),
                reminder.getName() + " - Recordatorio",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Maneja la petición de agregar un nuevo recordatorio desde la vista.
     * 
     * @param name    Nombre del recordatorio.
     * @param message Mensaje del recordatorio.
     * @param date    Fecha y hora programada.
     */
    public void handleReminderAddition(String name, String message, LocalDateTime date) {
        if (!isValidReminder(name, message, date))
            return;

        remindersModel.addReminder(name, message, date);
        remindersModel.saveReminders();
    }

    /**
     * Verifica si los datos proporcionados para un recordatorio son válidos.
     * 
     * @param name    Nombre del recordatorio.
     * @param message Mensaje asociado.
     * @param date    Fecha de activación.
     * @return {@code true} si los datos son válidos, {@code false} en caso
     *         contrario.
     */
    private boolean isValidReminder(String name, String message, LocalDateTime date) {
        if (name == null || name.isEmpty())
            return false;
        if (date == null)
            return false;
        return true;
    }

    /**
     * Maneja la petición de eliminar un recordatorio.
     * 
     * @param reminder Recordatorio a eliminar.
     */
    public void handleReminderDeletion(Reminder reminder) {
        remindersModel.deleteReminder(reminder);
        remindersModel.saveReminders();
    }

    /**
     * Maneja la edición de un recordatorio reemplazando el anterior con uno
     * modificado.
     * 
     * @param oldReminder Recordatorio original.
     * @param newReminder Versión editada del recordatorio.
     */
    public void handleReminderEdit(Reminder oldReminder, Reminder newReminder) {
        remindersModel.editReminder(oldReminder, newReminder);
        remindersModel.saveReminders();
    }

    /**
     * Abre la ventana de edición para un recordatorio específico.
     * 
     * @param reminder Recordatorio a editar.
     */
    public void onEditRequest(Reminder reminder) {
        RemindersEditorView editor = new RemindersEditorView(null, reminder);
        editor.setVisible(true);

        Reminder editedReminder = editor.getEditedReminder();
        if (editedReminder != null)
            handleReminderEdit(reminder, editedReminder);
    }
}
