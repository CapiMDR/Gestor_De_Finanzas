package config;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

/**
 * Utility class for common UI operations to prevent code duplication.
 */

public class UIUtils {

    /**
     * Shows an informational alert dialog.
     *
     * @param title   dialog title
     * @param message message body
     */
    public static void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    /**
     * Shows an error alert dialog.
     *
     * @param title   dialog title
     * @param message error message body
     */
    public static void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    /**
     * Shows a warning alert dialog.
     *
     * @param title   dialog title
     * @param message warning message body
     */
    public static void showWarning(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING, message, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
        });
    }

    /**
     * Shows a yes/no confirmation dialog.
     *
     * @param title   dialog title
     * @param message confirmation question
     * @return {@code true} if the user clicked YES
     */
    public static boolean showConfirm(String title, String message) {
        // Must run on FX thread, but block for result.
        // Assuming this is called from the FX application thread by default (e.g. action events)
        Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().filter(r -> r == ButtonType.YES).isPresent();
    }

    /**
     * Initializes hour and minute combo boxes with standard 24-hour options.
     * 
     * @param cmbHour   ComboBox for hours (00-23)
     * @param cmbMinute ComboBox for minutes (00-59)
     */
    public static void setupTimeComboBoxes(ComboBox<String> cmbHour, ComboBox<String> cmbMinute) {
        if (cmbHour != null && cmbMinute != null) {
            cmbHour.getItems().clear();
            cmbMinute.getItems().clear();
            for (int i = 0; i < 24; i++) cmbHour.getItems().add(String.format("%02d", i));
            for (int i = 0; i < 60; i++) cmbMinute.getItems().add(String.format("%02d", i));
            cmbHour.getSelectionModel().select("12");
            cmbMinute.getSelectionModel().select("00");
        }
    }
}
