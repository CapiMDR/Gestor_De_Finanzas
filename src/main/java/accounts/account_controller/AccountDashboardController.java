package accounts.account_controller;

import java.util.TreeSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.report_model.ReportData;
import reports.report_model.ReportGenerator;
import reports.report_model.ReportObserver;

import java.math.BigDecimal;
/**
 * JavaFX controller for the per-account dashboard ({@code account_dashboard.fxml}).
 *
 * This controller integrates the navigation shell functionality with the charts
 * and metrics that were originally in the Reports module.
 */

public class AccountDashboardController implements AccountObserver, ReportObserver {

    private static final Logger logger = LoggerFactory.getLogger(AccountDashboardController.class);

    // ── Account summary ──────────────────────────────────────────────────────
    @FXML private Label lblAccountName;
    @FXML private Label lblAccountBalance;
    @FXML private FontIcon accountIcon;

    // ── Chart filter buttons ─────────────────────────────────────────────────
    @FXML private Button btnToday;
    @FXML private Button btnYesterday;
    @FXML private Button btnCurrentWeek;
    @FXML private Button btnWeek;

    // ── Charts ───────────────────────────────────────────────────────────────
    @FXML private PieChart pieChartMovements;
    @FXML private BarChart<String, Number> barChartMovements;

    // ── Navigation panel ─────────────────────────────────────────────────────
    @FXML private HBox navAddMovement;
    @FXML private HBox navGoals;
    @FXML private HBox navReminders;
    @FXML private HBox navRecurrings;
    @FXML private HBox navFilters;

    // Navigation callbacks provided by AccountShell
    private Runnable onMovements;
    private Runnable onGoals;
    private Runnable onRecurrings;
    private Runnable onReminders;
    private Runnable onReports; 

    private Account account;
    private ReportGenerator reportGenerator;

    private static final String COLOR_INCOME = "color-income";
    private static final String COLOR_EXPENSE = "color-expense";
    private static final String LABEL_INGRESO = "INGRESO";
    private static final String LABEL_EGRESO = "EGRESO";
    private static final String BTN_FILTER_SELECTED = "btn-filter-selected";

    public void setAccount(Account account) {
        this.account = account;
        
        lblAccountName.setText(account.getName());
        refreshBalance(account);

        if (account.getType() == Account.AccountType.DIGITAL) {
            accountIcon.setIconLiteral("mdi2c-credit-card");
        } else {
            accountIcon.setIconLiteral("mdi2p-piggy-bank");
        }

        // Initialize ReportGenerator for the charts
        reportGenerator = new ReportGenerator(new reports.report_model.ReportSubject(), account);
        reportGenerator.addObserver(this);
        AccountManagerSubject.addObserver(this);

        assignActions();
        
        // Re-enable animation for better UX
        pieChartMovements.setAnimated(true);
        barChartMovements.setAnimated(true);
        
        pieChartMovements.getData().clear();
        barChartMovements.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Movimientos");
        barChartMovements.getData().add(series);
        
        // Force layout
        pieChartMovements.layout();

        // Load default chart view
        setActiveFilterButton(btnWeek);
        reportGenerator.weekAgo();
    }

    private void assignActions() {
        // Navigation actions
        navAddMovement.setOnMouseClicked(e -> fireCallback(onMovements, "Movimientos"));
        navGoals.setOnMouseClicked(e -> fireCallback(onGoals, "Metas"));
        navReminders.setOnMouseClicked(e -> fireCallback(onReminders, "Recordatorios"));
        navRecurrings.setOnMouseClicked(e -> fireCallback(onRecurrings, "Recurrentes"));
        navFilters.setOnMouseClicked(e -> fireCallback(onReports, "Filtros/Reportes"));

        // Time filter actions
        btnToday.setOnAction(e -> {
            setActiveFilterButton(btnToday);
            reportGenerator.today();
        });
        btnYesterday.setOnAction(e -> {
            setActiveFilterButton(btnYesterday);
            reportGenerator.yesterday();
        });
        btnCurrentWeek.setOnAction(e -> {
            setActiveFilterButton(btnCurrentWeek);
            reportGenerator.currentWeek();
        });
        btnWeek.setOnAction(e -> {
            setActiveFilterButton(btnWeek);
            reportGenerator.weekAgo();
        });
    }

    private void setActiveFilterButton(Button activeButton) {
        btnToday.getStyleClass().remove(BTN_FILTER_SELECTED);
        btnYesterday.getStyleClass().remove(BTN_FILTER_SELECTED);
        btnCurrentWeek.getStyleClass().remove(BTN_FILTER_SELECTED);
        btnWeek.getStyleClass().remove(BTN_FILTER_SELECTED);
        activeButton.getStyleClass().add(BTN_FILTER_SELECTED);
    }

    public void refreshBalance(Account account) {
        if (account != null) {
            Platform.runLater(() -> lblAccountBalance.setText(String.format("$%,.2f", account.getCurrentBalance())));
        }
    }

    // ── Observer Updates ─────────────────────────────────────────────────────

    @Override
    public void onNotify(List<Account> accountsList) {
        for (Account a : accountsList) {
            if (a.getName().equals(this.account.getName())) {
                this.account = a;
                refreshBalance(this.account);
                // Refresh charts on account update
                Platform.runLater(() -> reportGenerator.weekAgo());
                break;
            }
        }
    }

    @Override
    public void onNotify(ReportData reportData) {
        showCharts(reportData);
    }
    
    private void showCharts(ReportData reportData) {
        BigDecimal income = reportData.getMovements().stream()
                .filter(x -> x.getCategory().getType() == MovementCategory.MovementType.INCOME)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = reportData.getMovements().stream()
                .filter(x -> x.getCategory().getType() == MovementCategory.MovementType.EXPENSE)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Platform.runLater(() -> {
            updatePieChart(income, expense);
            updateBarChart(reportData);
            
            // Force layout before applying styles to ensure nodes exist
            pieChartMovements.layout();
            barChartMovements.layout();

            applyPieChartStyles();
            applyBarChartStyles();
            fixPieChartLegend();
        });
    }

    private void updatePieChart(BigDecimal income, BigDecimal expense) {
        if (pieChartMovements.getData().isEmpty()) {
            pieChartMovements.getData().add(new PieChart.Data(LABEL_INGRESO, 0));
            pieChartMovements.getData().add(new PieChart.Data(LABEL_EGRESO, 0));
        }
        
        double inc = income.doubleValue();
        double exp = expense.doubleValue();
        double total = inc + exp;
        
        for (PieChart.Data d : pieChartMovements.getData()) {
            if (d.getName().startsWith(LABEL_INGRESO)) {
                d.setPieValue(inc);
                double pct = total > 0 ? (inc / total) * 100 : 0;
                d.setName(String.format("%s (%.1f%%)", LABEL_INGRESO, pct));
            } else if (d.getName().startsWith(LABEL_EGRESO)) {
                d.setPieValue(exp);
                double pct = total > 0 ? (exp / total) * 100 : 0;
                d.setName(String.format("%s (%.1f%%)", LABEL_EGRESO, pct));
            }
        }
    }

    private void updateBarChart(ReportData reportData) {
        barChartMovements.setAnimated(false);
        barChartMovements.getData().clear();
        
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Ingresos");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Egresos");

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd MMM");
        
        Map<java.time.LocalDate, BigDecimal> incomesByDate = new HashMap<>();
        Map<java.time.LocalDate, BigDecimal> expensesByDate = new HashMap<>();
        
        for (Movement m : reportData.getMovements()) {
            java.time.LocalDate date = m.getDate().toLocalDate();
            if (m.getCategory().getType() == MovementCategory.MovementType.INCOME) {
                incomesByDate.put(date, incomesByDate.getOrDefault(date, BigDecimal.ZERO).add(m.getAmount()));
            } else {
                expensesByDate.put(date, expensesByDate.getOrDefault(date, BigDecimal.ZERO).add(m.getAmount()));
            }
        }
        
        Set<LocalDate> allDates = new TreeSet<>();
        allDates.addAll(incomesByDate.keySet());
        allDates.addAll(expensesByDate.keySet());
        
        for (LocalDate date : allDates) {
            String dateStr = date.format(formatter);
            incomeSeries.getData().add(new XYChart.Data<>(dateStr, incomesByDate.getOrDefault(date, BigDecimal.ZERO).doubleValue()));
            expenseSeries.getData().add(new XYChart.Data<>(dateStr, expensesByDate.getOrDefault(date, BigDecimal.ZERO).doubleValue()));
        }
        
        barChartMovements.getData().add(incomeSeries);
        barChartMovements.getData().add(expenseSeries);
        
        Platform.runLater(() -> barChartMovements.setAnimated(true));
    }

    private void applyPieChartStyles() {
        for (PieChart.Data d : pieChartMovements.getData()) {
            if (d.getNode() != null) {
                d.getNode().getStyleClass().removeAll(COLOR_INCOME, COLOR_EXPENSE);
                if (d.getName().startsWith(LABEL_INGRESO)) {
                    d.getNode().getStyleClass().add(COLOR_INCOME);
                } else {
                    d.getNode().getStyleClass().add(COLOR_EXPENSE);
                }
            }
        }
    }

    private void applyBarChartStyles() {
        if (barChartMovements.getData().size() >= 2) {
            XYChart.Series<String, Number> incomeSeries = barChartMovements.getData().get(0);
            XYChart.Series<String, Number> expenseSeries = barChartMovements.getData().get(1);
            
            for (XYChart.Data<String, Number> d : incomeSeries.getData()) {
                if (d.getNode() != null) {
                    d.getNode().getStyleClass().removeAll(COLOR_INCOME, COLOR_EXPENSE);
                    d.getNode().getStyleClass().add(COLOR_INCOME);
                }
            }
            for (XYChart.Data<String, Number> d : expenseSeries.getData()) {
                if (d.getNode() != null) {
                    d.getNode().getStyleClass().removeAll(COLOR_INCOME, COLOR_EXPENSE);
                    d.getNode().getStyleClass().add(COLOR_EXPENSE);
                }
            }
        }
    }

    private void fixPieChartLegend() {
        for (javafx.scene.Node n : pieChartMovements.lookupAll(".chart-legend-item")) {
            if (n instanceof javafx.scene.control.Label label && label.getGraphic() != null) {
                    label.getGraphic().getStyleClass().removeAll(COLOR_INCOME, COLOR_EXPENSE);
                    if (label.getText().startsWith(LABEL_INGRESO)) {
                        label.getGraphic().getStyleClass().add(COLOR_INCOME);
                    } else if (label.getText().startsWith(LABEL_EGRESO)) {
                        label.getGraphic().getStyleClass().add(COLOR_EXPENSE);
                    }
                }
        }
    }

    // ── Callback setters ─────────────────────────────────────────────────────

    public void setOnMovements(Runnable r)   { this.onMovements   = r; }
    public void setOnGoals(Runnable r)         { this.onGoals         = r; }
    public void setOnRecurrings(Runnable r)   { this.onRecurrings   = r; }
    public void setOnReminders(Runnable r) { this.onReminders = r; }
    public void setOnReports(Runnable r)      { this.onReports      = r; }

    private void fireCallback(Runnable callback, String name) {
        if (callback != null) {
            logger.debug("Dashboard → navegando a: {}", name);
            callback.run();
        }
    }
}
