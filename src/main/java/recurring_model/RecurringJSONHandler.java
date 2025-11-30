package recurring_model;

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

public class RecurringJSONHandler {
    private RecurringJSONHandler() {
    }

    private static final String FILE_NAME = "recurrings.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    // Ordenando a los recordatorios primero por su fecha, luego por su nombre
    public static Comparator<RecurringMove> REMINDER_COMPARATOR = Comparator.comparing(RecurringMove::getDate)
            .thenComparing(RecurringMove::getName);

    public static void saveReminders(TreeSet<RecurringMove> remindersList) {
        JSONArray arr = new JSONArray();

        for (RecurringMove reminder : remindersList) {
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

    public static TreeSet<RecurringMove> loadReminders() {
        TreeSet<RecurringMove> reminders = new TreeSet<>(REMINDER_COMPARATOR);
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

                reminders.add(new RecurringMove(name, message, date));
            }

        } catch (IOException e) {
            System.out.println("Error al cargar los recordatorios: " + e.getMessage());
        }

        return reminders;
    }
}
