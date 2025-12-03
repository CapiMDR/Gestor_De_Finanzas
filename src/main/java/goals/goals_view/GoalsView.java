package goals.goals_view;

import goals.goals_controller.GoalsController;
import goals.goals_model.Goal;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Main view for the Goals Module.
 * Uses initComponents() for integration.
 *
 * @author Jose Pablo Martinez
 */

public class GoalsView extends JPanel {

    private GoalsController controller;
    private JTextField txtName;
    private JTextField txtTargetAmount;
    private JTextField txtDestinationAccount;
    private JTextArea txtDescription;
    private JLabel lblTargetAmount;
    private JButton btnAddGoal;

    private JPanel cardsContainer;

    private static final Color FORM_BG_COLOR = new Color(246, 107, 14); // Orange
    private static final Color LIST_BG_COLOR = new Color(32, 83, 117); // Blue Turkey
    private static final Color HEADER_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(51, 51, 51);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BTN_ADD_BG = new Color(32, 83, 117);
    private static final Color BTN_ADD_TEXT = Color.WHITE;
    private static final Color EYE_BTN_COLOR = new Color(41, 128, 185); // Blue for Eye Circle

    private GoalActionListener cardActionListener;

    public GoalsView() {
        initComponents();
    }

    /**
     * Initializes the GUI components.
     * This method is called from within the constructor to initialize the form.
     */

    public void initComponents() {
        setLayout(new BorderLayout(0, 0));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(255, 140, 0));

        JLabel lblTitle = new JLabel("METAS FINANCIERAS");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 38));
        lblTitle.setForeground(HEADER_COLOR);
        headerPanel.add(lblTitle);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        JPanel contentPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = buildFormPanel();
        leftPanel.setPreferredSize(new Dimension(300, 0));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(LIST_BG_COLOR);
        rightPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(LIST_BG_COLOR);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(LIST_BG_COLOR);
        scrollPane.getViewport().setBackground(LIST_BG_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        rightPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FORM_BG_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        Font inputFont = new Font("Inter", Font.BOLD, 12); 

        // Init Components
        txtName = new JTextField();
        txtName.setFont(inputFont);
        txtTargetAmount = new JTextField();
        txtTargetAmount.setFont(inputFont);
        txtDestinationAccount = new JTextField();
        txtDestinationAccount.setFont(inputFont);
        txtDestinationAccount.setEditable(false);
        txtDestinationAccount.setBackground(new Color(240, 240, 240));

        txtDescription = new JTextArea(4, 15);
        txtDescription.setFont(inputFont);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        btnAddGoal = new JButton("Agregar Meta");
        btnAddGoal.setBackground(BTN_ADD_BG);
        btnAddGoal.setForeground(BTN_ADD_TEXT);
        btnAddGoal.setFont(new Font("Inter", Font.BOLD, 28));
        btnAddGoal.setFocusPainted(false);
        btnAddGoal.setContentAreaFilled(false);
        btnAddGoal.setOpaque(true);
        btnAddGoal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        btnAddGoal.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblTargetAmount = new JLabel("Monto Objetivo ($):");

        addFormField(panel, "Nombre:", txtName);
        addFormField(panel, lblTargetAmount, txtTargetAmount);
        addFormField(panel, "Cuenta destino:", txtDestinationAccount);

        // Description
        JLabel lblDesc = new JLabel("Nota/Descripción:");
        lblDesc.setFont(new Font("Inter", Font.BOLD, 18));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDesc);
        panel.add(Box.createVerticalStrut(5));

        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.add(scrollDesc);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnAddGoal);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void addFormField(JPanel panel, JLabel label, JTextField field) {
        label.setFont(new Font("Inter", Font.BOLD, 18));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Force text fields to expand horizontally
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
    }

    private void addFormField(JPanel panel, String text, JTextField field) {
        addFormField(panel, new JLabel(text), field);
    }

    // Setters and Getters

    public void setController(GoalsController controller) {
        this.controller = controller;
    }

    public void setCardActionListener(GoalActionListener listener) {
        this.cardActionListener = listener;
    }

    public String getGoalName() {
        return txtName.getText().trim();
    }

    public String getDescription() {
        return txtDescription.getText().trim();
    }

    public BigDecimal getTargetAmount() {
        try {
            return new BigDecimal(txtTargetAmount.getText().trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public JButton getBtnAddGoal() {
        return btnAddGoal;
    }

    public void setAccountName(String name) {
        txtDestinationAccount.setText(name);
    }

    public void setCurrencyLabel(String currencySymbol) {
        lblTargetAmount.setText("Monto Objetivo (" + currencySymbol + "):");
    }

    public void clearForm() {
        txtName.setText("");
        txtTargetAmount.setText("");
        txtDescription.setText("");
    }

    public void updateGoalList(List<Goal> goals) {
        cardsContainer.removeAll();
        int index = 1;
        for (Goal goal : goals) {
            GoalCardPanel card = new GoalCardPanel(goal, index++);
            cardsContainer.add(card);
            cardsContainer.add(Box.createVerticalStrut(10));
        }
        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    // Inner Class
    private class GoalCardPanel extends JPanel {

        public GoalCardPanel(Goal goal, int order) {
            setLayout(new BorderLayout(10, 5));
            setBackground(CARD_BG);
            setBorder(new CompoundBorder(
                    new LineBorder(Color.LIGHT_GRAY, 1, true),
                    new EmptyBorder(8, 10, 8, 10)));
            setMinimumSize(new Dimension(200, 90));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

            // Center info
            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setBackground(CARD_BG);

            String nameHtml = "<html><body style='width: 160px'><b>#" + order + " " + goal.getName()
                    + "</b></body></html>";
            JLabel lblName = new JLabel(nameHtml);
            lblName.setForeground(TEXT_PRIMARY);
            lblName.setFont(new Font("Inter", Font.PLAIN, 18));

            NumberFormat currency = NumberFormat.getCurrencyInstance();
            JLabel lblAmount = new JLabel("Meta: " + currency.format(goal.getTargetAmount()));
            lblAmount.setForeground(Color.GRAY);

            infoPanel.add(lblName);
            infoPanel.add(lblAmount);

            // Eye button (Right)
            JPanel rightPanel = new JPanel(new GridBagLayout());
            rightPanel.setBackground(CARD_BG);
            JButton btnView = new JButton("👁");
            btnView.setFont(new Font("Segoe UI Symbol", Font.BOLD, 25));
            btnView.setForeground(EYE_BTN_COLOR);
            btnView.setFocusPainted(false);
            btnView.setContentAreaFilled(false);
            btnView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnView.setBorder(new CompoundBorder(
                    new LineBorder(EYE_BTN_COLOR, 2, true),
                    new EmptyBorder(5, 10, 5, 10)));

            rightPanel.add(btnView);

            // Edit/Delete (Bottom)
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            bottomPanel.setBackground(CARD_BG);
            JButton btnEdit = createIconBtn("✎", "Editar", 20);
            JButton btnDelete = createIconBtn("🗑", "Eliminar", 20);
            bottomPanel.add(btnEdit);
            bottomPanel.add(Box.createHorizontalStrut(15));
            bottomPanel.add(btnDelete);

            // Listeners
            btnView.addActionListener(e -> {
                if (cardActionListener != null)
                    cardActionListener.onViewDetails(goal);
            });
            btnEdit.addActionListener(e -> {
                if (cardActionListener != null)
                    cardActionListener.onEdit(goal);
            });
            btnDelete.addActionListener(e -> {
                if (cardActionListener != null)
                    cardActionListener.onDelete(goal);
            });

            add(infoPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private JButton createIconBtn(String text, String tooltip, int size) {
            JButton btn = new JButton(text);
            btn.setToolTipText(tooltip);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setForeground(Color.GRAY);
            btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, size));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return btn;
        }
    }
}