package accounts.account_controller;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
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
import reports.modelReport.ReportData;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportObserver;

import java.math.BigDecimal;
import java.util.List;

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
    private Runnable onMovimientos;
    private Runnable onMetas;
    private Runnable onRecurrentes;
    private Runnable onRecordatorios;
    private Runnable onReportes; 

    private Account cuenta;
    private ReportGenerator reportGenerator;

    public void setAccount(Account cuenta) {
        this.cuenta = cuenta;
        
        lblAccountName.setText(cuenta.getName());
        refreshBalance(cuenta);

        if (cuenta.getType() == Account.AccountType.DIGITAL) {
            accountIcon.setIconLiteral("mdi2c-credit-card");
        } else {
            accountIcon.setIconLiteral("mdi2p-piggy-bank");
        }

        // Initialize ReportGenerator for the charts
        reportGenerator = new ReportGenerator(new reports.modelReport.ReportSubject(), cuenta);
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
        navAddMovement.setOnMouseClicked(e -> fireCallback(onMovimientos, "Movimientos"));
        navGoals.setOnMouseClicked(e -> fireCallback(onMetas, "Metas"));
        navReminders.setOnMouseClicked(e -> fireCallback(onRecordatorios, "Recordatorios"));
        navRecurrings.setOnMouseClicked(e -> fireCallback(onRecurrentes, "Recurrentes"));
        navFilters.setOnMouseClicked(e -> fireCallback(onReportes, "Filtros/Reportes"));

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
        btnToday.getStyleClass().remove("btn-filter-selected");
        btnYesterday.getStyleClass().remove("btn-filter-selected");
        btnCurrentWeek.getStyleClass().remove("btn-filter-selected");
        btnWeek.getStyleClass().remove("btn-filter-selected");
        activeButton.getStyleClass().add("btn-filter-selected");
    }

    public void refreshBalance(Account cuenta) {
        if (cuenta != null) {
            Platform.runLater(() -> {
                lblAccountBalance.setText(String.format("$%,.2f", cuenta.getCurrentBalance()));
            });
        }
    }

    // ── Observer Updates ─────────────────────────────────────────────────────

    @Override
    public void onNotify(List<Account> accountsList) {
        for (Account a : accountsList) {
            if (a.getName().equals(this.cuenta.getName())) {
                this.cuenta = a;
                refreshBalance(this.cuenta);
                // Refresh charts on account update
                Platform.runLater(() -> reportGenerator.weekAgo());
                break;
            }
        }
    }

    @Override
    public void onNotify(ReportData reportData) {
        showCharts(reportData.getPeriodName(), reportData);
    }
    
    private void showCharts(String periodName, ReportData reportData) {
        BigDecimal income = reportData.getMovements().stream()
                .filter(x -> x.getCategory().getType() == MovementCategory.MovementType.INCOME)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = reportData.getMovements().stream()
                .filter(x -> x.getCategory().getType() == MovementCategory.MovementType.EXPENSE)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Platform.runLater(() -> {
            // --- PieChart ---
            pieChartMovements.getData().clear();
            if (income.compareTo(BigDecimal.ZERO) > 0) {
                pieChartMovements.getData().add(new PieChart.Data("INGRESO", income.doubleValue()));
            }
            if (expense.compareTo(BigDecimal.ZERO) > 0) {
                pieChartMovements.getData().add(new PieChart.Data("EGRESO", expense.doubleValue()));
            }

            // --- BarChart ---
            barChartMovements.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(periodName);
            if (income.compareTo(BigDecimal.ZERO) > 0) {
                series.getData().add(new XYChart.Data<>("INGRESO", income.doubleValue()));
            }
            if (expense.compareTo(BigDecimal.ZERO) > 0) {
                series.getData().add(new XYChart.Data<>("EGRESO", expense.doubleValue()));
            }
            barChartMovements.getData().add(series);

            // Force layout before applying styles to ensure nodes exist
            pieChartMovements.layout();
            barChartMovements.layout();

            for (PieChart.Data d : pieChartMovements.getData()) {
                if (d.getNode() != null) {
                    d.getNode().getStyleClass().removeAll("color-income", "color-expense");
                    if ("INGRESO".equals(d.getName())) {
                        d.getNode().getStyleClass().add("color-income");
                    } else {
                        d.getNode().getStyleClass().add("color-expense");
                    }
                }
            }
            for (XYChart.Data<String, Number> d : series.getData()) {
                if (d.getNode() != null) {
                    d.getNode().getStyleClass().removeAll("color-income", "color-expense");
                    if ("INGRESO".equals(d.getXValue())) {
                        d.getNode().getStyleClass().add("color-income");
                    } else {
                        d.getNode().getStyleClass().add("color-expense");
                    }
                }
            }

            // Fix pie chart legend
            for (javafx.scene.Node n : pieChartMovements.lookupAll(".chart-legend-item")) {
                if (n instanceof javafx.scene.control.Label) {
                    javafx.scene.control.Label label = (javafx.scene.control.Label) n;
                    if (label.getGraphic() != null) {
                        label.getGraphic().getStyleClass().removeAll("color-income", "color-expense");
                        if ("INGRESO".equals(label.getText())) {
                            label.getGraphic().getStyleClass().add("color-income");
                        } else if ("EGRESO".equals(label.getText())) {
                            label.getGraphic().getStyleClass().add("color-expense");
                        }
                    }
                }
            }
        });
    }

    // ── Callback setters ─────────────────────────────────────────────────────

    public void setOnMovimientos(Runnable r)   { this.onMovimientos   = r; }
    public void setOnMetas(Runnable r)         { this.onMetas         = r; }
    public void setOnRecurrentes(Runnable r)   { this.onRecurrentes   = r; }
    public void setOnRecordatorios(Runnable r) { this.onRecordatorios = r; }
    public void setOnReportes(Runnable r)      { this.onReportes      = r; }

    private void fireCallback(Runnable callback, String name) {
        if (callback != null) {
            logger.debug("Dashboard → navegando a: {}", name);
            callback.run();
        }
    }
}
