package reminders.reminder_model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a reminder with a name, message and scheduled date.
 * 
 * The class allows verifying if the reminder should trigger according to its
 * scheduled date and if it was already triggered during the current execution.
 */
public class Reminder {

    /** Reminder name. */
    private String name;

    /** Message associated with the reminder. */
    private String message;

    /** Date and time when the reminder should trigger. */
    private LocalDateTime date;

    /**
     * Indicates whether the reminder has already triggered in this execution.
     * Used to prevent an overdue reminder from triggering multiple times.
     */
    private boolean triggered = false;

    /**
     * Main constructor of the reminder.
     * 
     * @param name    Reminder name.
     * @param message Message associated with the reminder.
     * @param date    Date and time when it should trigger.
     */
    public Reminder(String name, String message, LocalDateTime date) {
        this.name = name;
        this.message = message;
        this.date = date;
    }

    /**
     * Alternative constructor that assigns the current date as the
     * reminder date.
     * 
     * @param name    Reminder name.
     * @param message Message associated with the reminder.
     */
    public Reminder(String name, String message) {
        this.name = name;
        this.message = message;
        this.date = LocalDateTime.now(java.time.ZoneId.systemDefault());
    }

    /**
     * Gets the reminder name.
     * 
     * @return Reminder name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the reminder message.
     * 
     * @return Reminder message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the scheduled date of the reminder.
     * 
     * @return Date in {@link LocalDateTime} format.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Changes the reminder name.
     * 
     * @param name New reminder name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Changes the reminder message.
     * 
     * @param message New reminder message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets whether the reminder has already triggered.
     * 
     * @param t {@code true} if it was already triggered, otherwise {@code false}.
     */
    public void setTriggered(boolean t) {
        triggered = t;
    }

    /**
     * Gets whether the reminder has already triggered.
     * 
     * @return {@code true} if it has triggered, otherwise {@code false}.
     */
    public boolean isTriggered() {
        return triggered;
    }

    /**
     * Determines whether the reminder should trigger.
     * 
     * A reminder should trigger if:
     * - It has not triggered yet in this execution.
     * - The scheduled date is before the current moment.
     * 
     * @return {@code true} if it should trigger, otherwise {@code false}.
     */
    public boolean shouldTrigger() {
        LocalDateTime now = LocalDateTime.now(java.time.ZoneId.systemDefault());
        return !triggered && date.isBefore(now);
    }

    /**
     * Returns a text representation of the reminder.
     * 
     * @return Descriptive string of the reminder.
     */
    @Override
    public String toString() {
        return "Name: " + name + " Message: " + message + " Date "
                + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
