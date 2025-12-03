package reports.controllerReport;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import com.mycompany.construccion.FrmMain;

import java.awt.Image;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.ImageIcon;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
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
        initComponents();
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
    }

    private void initComponents() {
        // --- PieChart ---
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("INCOME", 0);
        dataset.setValue("EXPENSE", 0);

        view.updateCharts(dataset);
        
        // --- BarChart ---
        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();

        barDataset.addValue(0, "INCOME", "INCOME");
        barDataset.addValue(0, "EXPENSE", "EXPENSE");

        view.updateBarChart(barDataset);


        String pathImg = "";

        if (account.getType() == Account.AccountType.CASH) {
            pathImg = "/images/piggy.png";
        } else {
            pathImg = "/images/credit.png";
        }

        ImageIcon icon = new ImageIcon(getClass().getResource(pathImg));

        Image img = icon.getImage().getScaledInstance(
                view.credit.getWidth(),
                view.credit.getHeight(),
                Image.SCALE_SMOOTH);

        view.credit.setIcon(new ImageIcon(img));
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

        // --- BarChart ---
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