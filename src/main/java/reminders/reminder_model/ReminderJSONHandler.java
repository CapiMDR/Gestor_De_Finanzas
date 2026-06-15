package reminders.reminder_model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.AppConfig;

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
            obj.put("triggered", reminder.isTriggered());
            arr.put(obj);
        }

        Path target = Paths.get(AppConfig.getRemindersFilePath());
        Path temp = Paths.get(AppConfig.getRemindersFilePath() + ".tmp");

        try {
            Files.writeString(temp, arr.toString(4), StandardCharsets.UTF_8);
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
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
        java.io.File file = new java.io.File(AppConfig.getRemindersFilePath());
        if (!file.exists() || file.length() == 0) {
            return reminders;
        }

        try {
            String content = Files.readString(Paths.get(AppConfig.getRemindersFilePath()), StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(content);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                String name = obj.getString("name");
                String message = obj.getString("message");
                String dateStr = obj.getString("date");
                boolean triggered = obj.optBoolean("triggered", false);

                LocalDateTime date = LocalDateTime.parse(dateStr, FORMATTER);

                Reminder r = new Reminder(name, message, date);
                r.setTriggered(triggered);
                reminders.add(r);
            }

        } catch (IOException e) {
            logger.error("Error reading reminders file: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error parsing reminders JSON: {}", e.getMessage(), e);
            try {
                Files.copy(Paths.get(AppConfig.getRemindersFilePath()), Paths.get(AppConfig.getRemindersFilePath() + ".bak"), StandardCopyOption.REPLACE_EXISTING);
                logger.error("Corrupted reminders file backed up to .bak");
            } catch (IOException ioEx) {
                logger.error("Failed to backup corrupted reminders file", ioEx);
            }
        }

        logger.info("Reminders loaded — {} reminder(s).", reminders.size());
        return reminders;
    }
}
