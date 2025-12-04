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
 * Utilidad encargada de guardar y cargar los objetos {@link RecurringMove}
 * desde y hacia un archivo JSON.
 *
 * Esta clase es estática y no puede ser instanciada.
 */
public class RecurringJSONHandler {

    /** Constructor privado para evitar instanciación. */
    private RecurringJSONHandler() {
    }

    /** Nombre del archivo donde se guardan los recordatorios. */
    private static final String FILE_NAME = "recurrings.json";

    /** Formato estándar utilizado para serializar y deserializar fechas. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    // Ordenando a los recordatorios primero por su fecha, luego por su nombre
    /**
     * Comparador utilizado para ordenar los recordatorios por fecha y luego por
     * concepto.
     */
    public static Comparator<RecurringMove> REMINDER_COMPARATOR = Comparator.comparing(RecurringMove::getInitialDate)
            .thenComparing(RecurringMove::getConcept);

    /**
     * Guarda una colección de recordatorios recurrentes dentro de un archivo JSON.
     *
     * @param recurrentsList Lista ordenada de {@link RecurringMove} a guardar.
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
     * Carga los recordatorios recurrentes almacenados en el archivo JSON.
     *
     * @return Un {@link TreeSet} ordenado mediante {@link #REMINDER_COMPARATOR}
     *         conteniendo todos los objetos {@link RecurringMove} cargados.
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
