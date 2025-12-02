package reminders.reminder_model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase utilitaria para manejar la carga y guardado de recordatorios en un
 * archivo JSON.
 * 
 * <p>
 * Esta clase no puede ser instanciada ya que únicamente ofrece métodos
 * estáticos para la persistencia de objetos {@link Reminder}.
 * </p>
 */
public class ReminderJSONHandler {

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private ReminderJSONHandler() {
    }

    /** Nombre del archivo JSON donde se almacenan los recordatorios. */
    private static final String FILE_NAME = "reminders.json";

    /** Formato de fecha utilizado para guardar y leer fechas en el archivo JSON. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Comparador para ordenar los recordatorios primero por su fecha,
     * y luego por su nombre.
     */
    public static Comparator<Reminder> REMINDER_COMPARATOR = Comparator.comparing(Reminder::getDate)
            .thenComparing(Reminder::getName);

    /**
     * Guarda la lista de recordatorios en un archivo JSON.
     * 
     * @param remindersList Conjunto ordenado de recordatorios que serán guardados.
     */
    public static void saveReminders(TreeSet<Reminder> remindersList) {
        JSONArray arr = new JSONArray();

        for (Reminder reminder : remindersList) {
            JSONObject obj = new JSONObject();
            obj.put("name", reminder.getName());
            obj.put("message", reminder.getMessage());
            obj.put("date", reminder.getDate().format(FORMATTER));
            arr.put(obj);
        }

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(arr.toString(4));
        } catch (IOException e) {
            System.out.println("Error al guardar los recordatorios: " + e.getMessage());
        }
    }

    /**
     * Carga los recordatorios desde el archivo JSON.
     * 
     * @return Un {@link TreeSet} con los recordatorios cargados, ordenados
     *         por fecha y nombre.
     */
    public static TreeSet<Reminder> loadReminders() {
        TreeSet<Reminder> reminders = new TreeSet<>(REMINDER_COMPARATOR);
        StringBuilder jsonText = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }

            JSONArray arr = new JSONArray(jsonText.toString());

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                String name = obj.getString("name");
                String message = obj.getString("message");
                String dateStr = obj.getString("date");

                LocalDateTime date = LocalDateTime.parse(dateStr, FORMATTER);

                reminders.add(new Reminder(name, message, date));
            }

        } catch (IOException e) {
            System.out.println("Error al cargar los recordatorios: " + e.getMessage());
        }

        return reminders;
    }
}
