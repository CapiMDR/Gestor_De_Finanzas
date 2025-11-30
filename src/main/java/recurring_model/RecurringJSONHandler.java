package recurring_model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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
    public static Comparator<RecurringMove> REMINDER_COMPARATOR = Comparator.comparing(RecurringMove::getInitialDate)
            .thenComparing(RecurringMove::getConcept);

    public static void saveReminders(TreeSet<RecurringMove> recurrentsList) {
        JSONArray arr = new JSONArray();

        for (RecurringMove reminder : recurrentsList) {
            JSONObject obj = new JSONObject();
            obj.put("concept", reminder.getConcept());
            obj.put("amount", reminder.getAmount());
            obj.put("description", reminder.getDescription());
            obj.put("initialDate", reminder.getInitialDate().format(FORMATTER));
            obj.put("recurrence", reminder.getRecurrence().name());
            arr.put(obj);
        }

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(arr.toString(4));
        } catch (IOException e) {
            System.out.println("Error al guardar los pagos recurrentes: " + e.getMessage());
        }
    }

    public static TreeSet<RecurringMove> loadReminders() {
        TreeSet<RecurringMove> recMoves = new TreeSet<>(REMINDER_COMPARATOR);
        StringBuilder jsonText = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }

            JSONArray arr = new JSONArray(jsonText.toString());

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                String concept = obj.getString("concept");
                BigDecimal amount = obj.getBigDecimal("amount");
                String description = obj.getString("description");
                String initialDateStr = obj.getString("initialDate");
                RecurrenceType recurrence;
                try {
                    recurrence = RecurrenceType.valueOf(obj.optString("recurrence", "NONE"));
                } catch (IllegalArgumentException e) {
                    recurrence = RecurrenceType.Diario;
                }

                LocalDateTime date = LocalDateTime.parse(initialDateStr, FORMATTER);

                recMoves.add(new RecurringMove(concept, amount, description, date, recurrence));
            }

        } catch (IOException e) {
            System.out.println("Error al cargar los recordatorios: " + e.getMessage());
        }

        return recMoves;
    }
}
