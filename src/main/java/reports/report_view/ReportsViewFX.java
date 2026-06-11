package reports.report_view;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * JavaFX controller for the Reports view (Account Dashboard).
 * Replaces {@code FrmMain.java} (Swing), which was located in
 * {@code com.mycompany.construccion} mixed with the application entry point.
 *
 * <p>This is the main dashboard screen shown after selecting an account.
 * It serves a dual role:
 * <ul>
 *   <li><b>Navigation hub</b> — provides access to all other modules
 *       (Movements, Goals, Reminders, Recurrings, Filters).</li>
 *   <li><b>Account summary</b> — displays the account name, current balance,
 *       and time-filtered charts (today / last 7 days).</li>
 * </ul>
 *
 * <p>Layout is defined declaratively in {@code /fxml/reports.fxml}.
 * Business logic is delegated to {@code ReportController}.
 *
 * @see reports.controllerReport.ReportController
 */
public class ReportsViewFX {

    // ── Account summary ──────────────────────────────────────────────────────
    /** Displays the name of the currently selected account. */
    @FXML private Label lblAccountName;

    /** Displays the current balance of the selected account. */
    @FXML private Label lblAccountBalance;

    // ── Chart filter buttons ─────────────────────────────────────────────────
    /** Filter charts to show only today's movements. */
    @FXML private Button btnToday;

    /** Filter charts to show movements from the last 7 days. */
    @FXML private Button btnWeek;

    // ── Charts ───────────────────────────────────────────────────────────────
    /** Pie chart showing income vs. expense distribution. */
    @FXML private PieChart pieChartMovements;

    /** Bar chart showing movement amounts grouped by date. */
    @FXML private BarChart<String, Number> barChartMovements;

    // ── Module navigation ────────────────────────────────────────────────────
    /** Opens the Movements module. */
    @FXML private HBox navAddMovement;

    /** Opens the Goals module. */
    @FXML private HBox navGoals;

    /** Opens the Reminders module. */
    @FXML private HBox navReminders;

    /** Opens the Recurrings module. */
    @FXML private HBox navRecurrings;

    /** Opens the Filters module. */
    @FXML private HBox navFilters;

    /** Opens the Interest Calculator. */
    @FXML private HBox navCredit;

    // TODO: Implement — Phase 3.4 (Reports/Dashboard module migration)
}
