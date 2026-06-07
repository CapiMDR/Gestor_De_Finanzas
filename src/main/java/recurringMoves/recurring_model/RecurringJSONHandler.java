package recurringMoves.recurring_model;

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

import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;

/**
 * Utility in charge of saving and loading {@link RecurringMove} objects
 * to and from a JSON file.
 *
 * This class is static and cannot be instantiated.
 */
public class RecurringJSONHandler {

    /** Private constructor to prevent instantiation. */
    private RecurringJSONHandler() {
    }

    /** File name where reminders are saved. */
    private static final String FILE_NAME = "recurrings.json";

    /** Standard format used to serialize and deserialize dates. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    // Sorting reminders first by their date, then by their name
    /**
     * Comparator used to sort the reminders by date and then by
     * concept.
     */
    public static Comparator<RecurringMove> REMINDER_COMPARATOR = Comparator.comparing(RecurringMove::getInitialDate)
            .thenComparing(RecurringMove::getConcept);

    /**
     * Saves a collection of recurring reminders into a JSON file.
     *
     * @param recurrentsList Sorted list of {@link RecurringMove} to save.
     */
    public static void saveReminders(TreeSet<RecurringMove> recurrentsList) {
        JSONArray arr = new JSONArray();

        for (RecurringMove recMove : recurrentsList) {
            JSONObject obj = new JSONObject();
            obj.put("concept", recMove.getConcept());
            obj.put("amount", recMove.getAmount());
            obj.put("description", recMove.getDescription());
            obj.put("initialDate", recMove.getInitialDate().format(FORMATTER));
            obj.put("recurrence", recMove.getRecurrence().name());
            obj.put("categoryName", recMove.getCategory().getName());
            obj.put("categoryType", recMove.getCategory().getType().name());
            arr.put(obj);
        }

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(arr.toString(4));
        } catch (IOException e) {
            System.out.println("Error al guardar los pagos recurrentes: " + e.getMessage());
        }
    }

    /**
     * Loads the recurring reminders stored in the JSON file.
     *
     * @return A {@link TreeSet} sorted by {@link #REMINDER_COMPARATOR}
     *         containing all loaded {@link RecurringMove} objects.
     */
    public static TreeSet<RecurringMove> loadRecurrings() {
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

                String categoryName = obj.getString("categoryName");
                MovementType categoryType = MovementType.valueOf(obj.getString("categoryType"));
                MovementCategory category = new MovementCategory(categoryName, categoryType);
                recMoves.add(new RecurringMove(concept, amount, description, date, recurrence, category));
            }

        } catch (IOException e) {
            System.out.println("Error al cargar los recordatorios: " + e.getMessage());
        }

        return recMoves;
    }
}
