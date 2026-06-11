package reports.controllerReport;

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
import goals.GoalsModule;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_view.MovementsModule;
import reports.modelReport.ReportData;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportObserver;
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
        view.getBtnToday().setOnAction(e -> reportGenerator.today());
        view.getBtnWeek().setOnAction(e -> reportGenerator.weekAgo());

        // Navigation actions
        view.getNavAddMovement().setOnMouseClicked(e -> {
            MovementsModule.initMovements(account);
        });

        view.getNavGoals().setOnMouseClicked(e -> {
            GoalsModule.initGoals(account);
        });

        view.getNavReminders().setOnMouseClicked(e -> {
            reminders.reminder_view.RemindersModule.initReminders();
        });
        view.getNavRecurrings().setOnMouseClicked(e -> {
            recurringMoves.recurring_view.RecurringsModule.initRecurrings();
        });
        view.getNavFilters().setOnMouseClicked(e -> {
            filters.modelFilter.CategoriesModule.initCategories(account);
        });
        view.getNavCredit().setOnMouseClicked(e -> showUnderConstructionAlert("Calculadora de Crédito"));
    }

    private void showUnderConstructionAlert(String moduleName) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Módulo en Construcción");
        alert.setHeaderText(null);
        alert.setContentText("El módulo de " + moduleName + " está en construcción para JavaFX.");
        alert.showAndWait();
    }

    private void initComponents() {
        Platform.runLater(() -> {
            // --- PieChart ---
            view.getPieChartMovements().getData().clear();
            view.getPieChartMovements().getData().addAll(
                new PieChart.Data("INCOME", 0),
                new PieChart.Data("EXPENSE", 0)
            );

            // --- BarChart ---
            view.getBarChartMovements().getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Movimientos");
            series.getData().add(new XYChart.Data<>("INCOME", 0));
            series.getData().add(new XYChart.Data<>("EXPENSE", 0));
            view.getBarChartMovements().getData().add(series);

            for (PieChart.Data d : view.getPieChartMovements().getData()) {
                if (d.getNode() != null) {
                    if ("INCOME".equals(d.getName())) d.getNode().setStyle("-fx-pie-color: -fx-success;");
                    else d.getNode().setStyle("-fx-pie-color: -fx-danger;");
                }
            }
            for (XYChart.Data<String, Number> d : series.getData()) {
                if (d.getNode() != null) {
                    if ("INCOME".equals(d.getXValue())) d.getNode().setStyle("-fx-bar-fill: -fx-success;");
                    else d.getNode().setStyle("-fx-bar-fill: -fx-danger;");
                }
            }
        });
    }

    private void syncAccount() {
        Platform.runLater(() -> {
            view.getLblAccountName().setText(account.getName());
            view.getLblAccountBalance().setText("$" + account.getCurrentBalance().toPlainString());
        });
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
            view.getPieChartMovements().getData().addAll(
                new PieChart.Data("INCOME", income.doubleValue()),
                new PieChart.Data("EXPENSE", expense.doubleValue())
            );

            // --- BarChart ---
            view.getBarChartMovements().getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(periodName);
            series.getData().add(new XYChart.Data<>("INCOME", income.doubleValue()));
            series.getData().add(new XYChart.Data<>("EXPENSE", expense.doubleValue()));
            view.getBarChartMovements().getData().add(series);

            for (PieChart.Data d : view.getPieChartMovements().getData()) {
                if (d.getNode() != null) {
                    if ("INCOME".equals(d.getName())) d.getNode().setStyle("-fx-pie-color: -fx-success;");
                    else d.getNode().setStyle("-fx-pie-color: -fx-danger;");
                }
            }
            for (XYChart.Data<String, Number> d : series.getData()) {
                if (d.getNode() != null) {
                    if ("INCOME".equals(d.getXValue())) d.getNode().setStyle("-fx-bar-fill: -fx-success;");
                    else d.getNode().setStyle("-fx-bar-fill: -fx-danger;");
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
}