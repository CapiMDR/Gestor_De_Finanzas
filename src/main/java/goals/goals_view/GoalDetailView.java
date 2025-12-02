package goals.goals_view;

import movements.movement_model.MovementCategory;
import movements.movement_model.MovementObserver;

import goals.goals_model.Goal;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * View for displaying detailed progress of a single goal.
 * Includes Description field.
 *
 * @author Jose Pablo Martinez
 */
public class GoalDetailView extends JDialog implements MovementObserver {

    private final JLabel lblName;
    private final JLabel lblStatus;
    private final JTextArea txtDescriptionDisplay;
    private final JProgressBar progressBar;

    private Goal currentGoal;

    public GoalDetailView(JFrame parent) {
        super(parent, "Detalle de Meta", false);
        this.setSize(400, 300);
        this.setLayout(new BorderLayout(15, 15));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        lblName = new JLabel("Meta", SwingConstants.CENTER);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblStatus = new JLabel("$0.00 / $0.00", SwingConstants.CENTER);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setPreferredSize(new Dimension(300, 25));

        // Description Area (Read Only)
        JLabel lblDescTitle = new JLabel("Nota:");
        lblDescTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDescriptionDisplay = new JTextArea(4, 20);
        txtDescriptionDisplay.setWrapStyleWord(true);
        txtDescriptionDisplay.setLineWrap(true);
        txtDescriptionDisplay.setEditable(false);
        txtDescriptionDisplay.setBackground(this.getBackground());
        txtDescriptionDisplay.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        contentPanel.add(lblName);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(progressBar);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lblStatus);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(lblDescTitle);
        contentPanel.add(new JScrollPane(txtDescriptionDisplay));

        add(contentPanel, BorderLayout.CENTER);
        setLocationRelativeTo(parent);
    }

    public void showProgress(Goal objGoal) {
        this.currentGoal = objGoal;

        BigDecimal current = objGoal.getCurrentAmount();
        BigDecimal target = objGoal.getTargetAmount();

        double percentage = 0;
        if (target.compareTo(BigDecimal.ZERO) > 0) {
            percentage = current.doubleValue() / target.doubleValue() * 100;
        }

        NumberFormat currency = NumberFormat.getCurrencyInstance();
        lblName.setText(objGoal.getName());
        lblStatus.setText(String.format("Ahorrado: %s de %s", currency.format(current), currency.format(target)));
        progressBar.setValue((int) percentage);

        // Show Description
        txtDescriptionDisplay.setText(objGoal.getDescription());

        if (!this.isVisible()) {
            this.setVisible(true);
        }
    }

    @Override
    public void onNotify(List<MovementCategory> categories) {
        if (this.isVisible() && currentGoal != null) {
            showProgress(currentGoal);
        }
    }
}
