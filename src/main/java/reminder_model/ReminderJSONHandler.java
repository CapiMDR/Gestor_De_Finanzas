package reminder_model;

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

public class ReminderJSONHandler {
    private ReminderJSONHandler() {
    }

    private static final String FILE_NAME = "reminders.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    // Ordenando a los recordatorios primero por su fecha, luego por su nombre
    public static Comparator<Reminder> REMINDER_COMPARATOR = Comparator.comparing(Reminder::getDate)
            .thenComparing(Reminder::getName);

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
