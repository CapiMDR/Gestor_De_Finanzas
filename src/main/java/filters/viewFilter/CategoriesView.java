package filters.viewFilter;

import java.awt.*;

import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CategoriesView extends JPanel {

    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private DefaultListModel<String> incomeListModel;
    private DefaultListModel<String> expenseListModel;
    private JList<String> incomeList;
    private JList<String> expenseList;

    // Paleta de colores
    private final Color BACKGROUND_COLOR = new Color(255, 195, 137);
    private final Color PANEL_COLOR = new Color(48, 45, 76);
    private final Color ACCENT_COLOR = new Color(249, 147, 34);
    private final Color LIGHT_ACCENT = new Color(253, 157, 102);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color INCOME_COLOR = new Color(76, 175, 80);
    private final Color EXPENSE_COLOR = new Color(244, 67, 54);

    public CategoriesView() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        initUI();
    }

    private void initUI() {

        // ---------- TÍTULO ----------
        JLabel title = new JLabel("CATEGORÍAS");
        title.setFont(new Font("Arial Black", Font.BOLD, 32));
        title.setForeground(PANEL_COLOR);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(20, 0, 20, 0));

        add(title, BorderLayout.NORTH);

        // ---------- PANEL CENTRAL ----------
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // INGRESOS ---------------------------------
        JPanel incomePanel = new JPanel(new BorderLayout());
        incomePanel.setBackground(INCOME_COLOR);
        incomePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel incomeTitle = new JLabel("INGRESOS");
        incomeTitle.setForeground(TEXT_COLOR);
        incomeTitle.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        incomeTitle.setHorizontalAlignment(SwingConstants.CENTER);

        totalIncomeLabel = new JLabel("$0.00");
        totalIncomeLabel.setForeground(TEXT_COLOR);
        totalIncomeLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 22));
        totalIncomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        incomeListModel = new DefaultListModel<>();
        incomeList = new JList<>(incomeListModel);
        incomeList.setBackground(Color.WHITE);
        incomeList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        incomePanel.add(incomeTitle, BorderLayout.NORTH);
        incomePanel.add(totalIncomeLabel, BorderLayout.CENTER);
        incomePanel.add(new JScrollPane(incomeList), BorderLayout.SOUTH);


        // GASTOS ---------------------------------
        JPanel expensePanel = new JPanel(new BorderLayout());
        expensePanel.setBackground(EXPENSE_COLOR);
        expensePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel expenseTitle = new JLabel("GASTOS");
        expenseTitle.setForeground(TEXT_COLOR);
        expenseTitle.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        expenseTitle.setHorizontalAlignment(SwingConstants.CENTER);

        totalExpenseLabel = new JLabel("$0.00");
        totalExpenseLabel.setForeground(TEXT_COLOR);
        totalExpenseLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 22));
        totalExpenseLabel.setHorizontalAlignment(SwingConstants.CENTER);

        expenseListModel = new DefaultListModel<>();
        expenseList = new JList<>(expenseListModel);
        expenseList.setBackground(Color.WHITE);
        expenseList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        expensePanel.add(expenseTitle, BorderLayout.NORTH);
        expensePanel.add(totalExpenseLabel, BorderLayout.CENTER);
        expensePanel.add(new JScrollPane(expenseList), BorderLayout.SOUTH);


        centerPanel.add(incomePanel);
        centerPanel.add(expensePanel);

        add(centerPanel, BorderLayout.CENTER);

        // ---------- BOTÓN CERRAR ----------
        JButton closeBtn = new JButton("Cerrar");
        closeBtn.setBackground(ACCENT_COLOR);
        closeBtn.setForeground(TEXT_COLOR);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());

        JPanel footer = new JPanel();
        footer.setBackground(BACKGROUND_COLOR);
        footer.add(closeBtn);

        add(footer, BorderLayout.SOUTH);
    }

    // =======================================================
    //        MÉTODO LLAMADO DESDE EL CONTROLLER
    // =======================================================
    public void updateCategories(Map<String, Double> incomes,
                                 Map<String, Double> expenses,
                                 double totalIncome,
                                 double totalExpense) {

        totalIncomeLabel.setText(String.format("$%.2f", totalIncome));
        totalExpenseLabel.setText(String.format("$%.2f", totalExpense));

        incomeListModel.clear();
        expenses.forEach((key, value) -> {});

        incomeListModel.clear();
        for (var entry : incomes.entrySet()) {
            incomeListModel.addElement(entry.getKey() + "  -  $" + String.format("%.2f", entry.getValue()));
        }

        expenseListModel.clear();
        for (var entry : expenses.entrySet()) {
            expenseListModel.addElement(entry.getKey() + "  -  $" + String.format("%.2f", entry.getValue()));
        }
    }
}