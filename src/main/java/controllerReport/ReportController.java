/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerReport;

import java.time.LocalDate;
import modelReport.Movement;
import modelReport.ReportData;
import modelReport.ReportGenerator;
import modelReport.ReportObserver;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import viewReport.FrmMain;

/**
 *
 * @author villa
 */
public class ReportController implements ReportObserver {

    private FrmMain view;
    private ReportGenerator reportGenerator;

    public void setViewModule(FrmMain view, ReportGenerator generator) {
        this.view = view;
        this.reportGenerator = generator;

        reportGenerator.addObserver(this);

        assignActions();
    }

    private void assignActions() {

        view.btnToday.addActionListener(e -> {
            reportGenerator.today();
        });

        view.btnYesterday.addActionListener(e -> {
            reportGenerator.weekAgo();
        });

        view.btnCustom.addActionListener(e -> {
            LocalDate s = LocalDate.parse(view.txtStartDate.getText());
            LocalDate e2 = LocalDate.parse(view.txtEndDate.getText());
            reportGenerator.selectPeriod(s, e2);
        });
    }

    @Override
    public void onNotify(ReportData data) {
        showCharts(data.getPeriodName(), data);
    }

    public void showCharts(String periodName, ReportData reportData) {

        double income = reportData.getMovements().stream()
                .filter(x -> x.getCategoryType().equals("INCOME"))
                .mapToDouble(Movement::getAmount)
                .sum();

        double expense = reportData.getMovements().stream()
                .filter(x -> x.getCategoryType().equals("EXPENSE"))
                .mapToDouble(Movement::getAmount)
                .sum();

        // --- PieChart ---
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("INCOME", income);
        dataset.setValue("EXPENSE", expense);

        view.updateCharts(dataset);

        // --- BarChart ---
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

        for (Movement m : reportData.getMovements()) {

            barDataset.addValue(
                    m.getAmount(),
                    m.getCategoryType(),
                    m.getDate().toLocalDate().toString()
            );
        }

        view.updateBarChart(barDataset);
    }
}
