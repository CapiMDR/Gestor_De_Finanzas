/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports.controllerReport;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import com.mycompany.construccion.FrmMain;
import java.math.BigDecimal;
import java.util.List;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import reports.modelReport.ReportData;
import reports.modelReport.ReportGenerator;
import reports.modelReport.ReportObserver;

/**
 *
 * @author villa
 */
public class ReportController implements ReportObserver, AccountObserver {

    private FrmMain view;
    private ReportGenerator reportGenerator;
    private Account account;

    public void setViewModule(FrmMain view, ReportGenerator generator, Account selectedAccount) {
        this.account = selectedAccount;
        System.out.println("Se seleccionó la cuenta " + selectedAccount.getName());
        this.view = view;
        this.reportGenerator = generator;
        AccountManagerSubject.addObserver(this);
        reportGenerator.addObserver(this);

        assignActions();
        syncAccount();
    }

    private void assignActions() {

        view.btnToday.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                reportGenerator.today();
            }
        });

        view.btnWeek.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                reportGenerator.weekAgo();
            }
    });

    

        /*
         * view.btnCustom.addMouseListener(new java.awt.event.MouseAdapter() {
         * 
         * @Override
         * public void mouseClicked(java.awt.event.MouseEvent e) {
         * LocalDate s = LocalDate.parse(view.txtStartDate.getText());
         * LocalDate e2 = LocalDate.parse(view.txtEndDate.getText());
         * reportGenerator.selectPeriod(s, e2);
         * }
         * });
         */
    }


    private void syncAccount(){
        view.labelName.setText(""+account.getName());
        view.labelMoney.setText("$"+account.getCurrentBalance());
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

        // --- PieChart ---
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("INCOME", income);
        dataset.setValue("EXPENSE", expense);

        view.updateCharts(dataset);

        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

        barDataset.addValue(income, "INCOME", "INCOME");
        barDataset.addValue(expense, "EXPENSE", "EXPENSE");

        view.updateBarChart(barDataset);
    }

    @Override
    public void onNotify(List<Account> accountsList) {
        System.out.println("Se actualizo el dinero");
        syncAccount();
    }
}
