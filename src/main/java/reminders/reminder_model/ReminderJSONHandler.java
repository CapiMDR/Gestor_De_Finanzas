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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to handle the loading and saving of reminders in a
 * JSON file.
 * 
 * This class cannot be instantiated since it only offers static
 * methods for the persistence of {@link Reminder} objects.
 */
public class ReminderJSONHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReminderJSONHandler.class);

    /**
     * Private constructor to prevent instantiation.
     */
    private ReminderJSONHandler() {
    }

    /** JSON file name where reminders are stored. */
    private static final String FILE_NAME = "reminders.json";

    /** Date format used to save and read dates in the JSON file. */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Comparator to sort reminders first by their date,
     * and then by their name.
     */
    public static Comparator<Reminder> REMINDER_COMPARATOR = Comparator.comparing(Reminder::getDate)
            .thenComparing(Reminder::getName);

    /**
     * Saves the list of reminders in a JSON file.
     * 
     * @param remindersList Sorted set of reminders to be saved.
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
            logger.info("Reminders saved successfully — {} reminder(s).", remindersList.size());
        } catch (IOException e) {
            logger.error("Error saving reminders: {}", e.getMessage(), e);
        }
    }

    /**
     * Loads reminders from the JSON file.
     * 
     * @return A {@link TreeSet} with the loaded reminders, sorted
     *         by date and name.
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
            logger.error("Error loading reminders: {}", e.getMessage(), e);
        }

        logger.info("Reminders loaded — {} reminder(s).", reminders.size());
        return reminders;
    }
}
