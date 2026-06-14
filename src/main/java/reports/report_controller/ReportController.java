package reports.report_controller;

import java.math.BigDecimal;
import java.util.List;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import javafx.application.Platform;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import goals.goals_view.GoalsModule;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_view.MovementsModule;
import reports.report_model.ReportData;
import reports.report_model.ReportGenerator;
import reports.report_model.ReportObserver;
import reports.report_view.ReportsViewFX;

/**
 * Controller class that manages reports and charts view interactions.
 * Implements ReportObserver and AccountObserver to stay synced with model changes.
 * Refactored for JavaFX migration.
 * @author villa
 */
public class ReportController implements ReportObserver, AccountObserver {

    private ReportsViewFX view;
    private ReportGenerator reportGenerator;
    private Account account;

    private static final String COLOR_INCOME = "color-income";
    private static final String COLOR_EXPENSE = "color-expense";
    private static final String BTN_FILTER_SELECTED = "btn-filter-selected";

    public void setViewModule(ReportsViewFX view, ReportGenerator generator, Account selectedAccount) {
        this.account = selectedAccount;
        this.view = view;
        this.reportGenerator = generator;
        
        AccountManagerSubject.addObserver(this);
        reportGenerator.addObserver(this);

        assignActions();
        syncAccount();
        initComponents();
    }

    private void assignActions() {
        // Time filter actions
        view.getBtnToday().setOnAction(e -> {
            setActiveFilterButton(view.getBtnToday());
            reportGenerator.today();
        });
        view.getBtnYesterday().setOnAction(e -> {
            setActiveFilterButton(view.getBtnYesterday());
            reportGenerator.yesterday();
        });
        view.getBtnCurrentWeek().setOnAction(e -> {
            setActiveFilterButton(view.getBtnCurrentWeek());
            reportGenerator.currentWeek();
        });
        view.getBtnWeek().setOnAction(e -> {
            setActiveFilterButton(view.getBtnWeek());
            reportGenerator.weekAgo();
        });

        // Navigation actions
        view.getNavAddMovement().setOnMouseClicked(e -> MovementsModule.initMovements(account));

        view.getNavGoals().setOnMouseClicked(e -> GoalsModule.initGoals(account));

        view.getNavReminders().setOnMouseClicked(e -> reminders.reminder_view.RemindersModule.initReminders());
        
        view.getNavRecurrings().setOnMouseClicked(e -> recurrings.recurring_view.RecurringsModule.initRecurrings());
        
        view.getNavFilters().setOnMouseClicked(e -> filters.filter_model.CategoriesModule.initCategories(account));
        view.getNavCredit().setOnMouseClicked(e -> showUnderConstructionAlert("Calculadora de Crédito"));
    }

    private void showUnderConstructionAlert(String moduleName) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Módulo en Construcción");
        alert.setHeaderText(null);
        alert.setContentText("El módulo de " + moduleName + " está en construcción para JavaFX.");
        alert.showAndWait();
    }

    private void setActiveFilterButton(Button activeButton) {
        view.getBtnToday().getStyleClass().remove(BTN_FILTER_SELECTED);
        view.getBtnYesterday().getStyleClass().remove(BTN_FILTER_SELECTED);
        view.getBtnCurrentWeek().getStyleClass().remove(BTN_FILTER_SELECTED);
        view.getBtnWeek().getStyleClass().remove(BTN_FILTER_SELECTED);
        
        activeButton.getStyleClass().add(BTN_FILTER_SELECTED);
    }

    private void initComponents() {
        Platform.runLater(() -> {
            // Re-enable animation for better UX, accepting the occasional label overlapping bug
            view.getPieChartMovements().setAnimated(true);
            view.getBarChartMovements().setAnimated(true);

            // --- PieChart ---
            view.getPieChartMovements().getData().clear();

            // --- BarChart ---
            view.getBarChartMovements().getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Movimientos");
            view.getBarChartMovements().getData().add(series);
            
            // Force layout to avoid label overlapping bug
            view.getPieChartMovements().layout();
            
            // Initialize default filter and data
            setActiveFilterButton(view.getBtnWeek());
            reportGenerator.weekAgo();
        });
    }

    private void syncAccount() {
        if (account != null) {
            view.getLblAccountName().setText(account.getName());
            view.getLblAccountBalance().setText(String.format("$%.2f", account.getCurrentBalance()));
            
            if (account.getType() == Account.AccountType.DIGITAL) {
                view.getAccountIcon().setIconLiteral("mdi2c-credit-card");
            } else {
                view.getAccountIcon().setIconLiteral("mdi2p-piggy-bank");
            }
        }
    }

    @Override
    public void onNotify(ReportData data) {
        showCharts(data.getPeriodName(), data);
    }

    public void showCharts(String periodName, ReportData reportData) {

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
            view.getPieChartMovements().getData().clear();
            if (income.compareTo(BigDecimal.ZERO) > 0) {
                view.getPieChartMovements().getData().add(new PieChart.Data("INGRESO", income.doubleValue()));
            }
            if (expense.compareTo(BigDecimal.ZERO) > 0) {
                view.getPieChartMovements().getData().add(new PieChart.Data("EGRESO", expense.doubleValue()));
            }

            // --- BarChart ---
            view.getBarChartMovements().getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(periodName);
            if (income.compareTo(BigDecimal.ZERO) > 0) {
                series.getData().add(new XYChart.Data<>("INGRESO", income.doubleValue()));
            }
            if (expense.compareTo(BigDecimal.ZERO) > 0) {
                series.getData().add(new XYChart.Data<>("EGRESO", expense.doubleValue()));
            }
            view.getBarChartMovements().getData().add(series);

            // Force layout before applying styles to ensure nodes exist
            view.getPieChartMovements().layout();
            view.getBarChartMovements().layout();

            for (PieChart.Data d : view.getPieChartMovements().getData()) {
                if (d.getNode() != null) {
                    d.getNode().getStyleClass().removeAll(COLOR_INCOME, COLOR_EXPENSE);
                    if ("INGRESO".equals(d.getName())) {
                        d.getNode().getStyleClass().add(COLOR_INCOME);
                    } else {
                        d.getNode().getStyleClass().add(COLOR_EXPENSE);
                    }
                }
            }
            for (XYChart.Data<String, Number> d : series.getData()) {
                if (d.getNode() != null) {
                    d.getNode().getStyleClass().removeAll(COLOR_INCOME, COLOR_EXPENSE);
                    if ("INGRESO".equals(d.getXValue())) {
                        d.getNode().getStyleClass().add(COLOR_INCOME);
                    } else {
                        d.getNode().getStyleClass().add(COLOR_EXPENSE);
                    }
                }
            }

            // Fix pie chart legend
            for (javafx.scene.Node n : view.getPieChartMovements().lookupAll(".chart-legend-item")) {
                if (n instanceof javafx.scene.control.Label label) {
                    if (label.getGraphic() != null) {
                        label.getGraphic().getStyleClass().removeAll(COLOR_INCOME, COLOR_EXPENSE);
                        if ("INGRESO".equals(label.getText())) {
                            label.getGraphic().getStyleClass().add(COLOR_INCOME);
                        } else if ("EGRESO".equals(label.getText())) {
                            label.getGraphic().getStyleClass().add(COLOR_EXPENSE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onNotify(List<Account> accountsList) {
        // Find if this specific account was updated
        for (Account a : accountsList) {
            if (a.getName().equals(this.account.getName())) {
                this.account = a;
                syncAccount();
                break;
            }
        }
    }

    /**
     * Unregisters this controller as an observer to prevent memory leaks.
     */
    public void dispose() {
        AccountManagerSubject.removeObserver(this);
        if (reportGenerator != null) {
            reportGenerator.removeObserver(this);
        }
    }
}