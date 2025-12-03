package goals.goals_view;

import movements.movement_model.MovementCategory;
import movements.movement_model.CategoryObserver;

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
 * View for displaying detailed progress of a single goal.
 * Includes Description field.
 *
 * @author Jose Pablo Martinez
 */
public class GoalDetailView extends JDialog implements CategoryObserver {

    private final JLabel lblName;
    private final JLabel lblStatus;
    private final JTextArea txtDescriptionDisplay;
    private final JProgressBar progressBar;

    private Goal currentGoal;

    public GoalDetailView() {
        this.setSize(400, 300);
        this.setLayout(new BorderLayout(15, 15));

        Color bgColor = new Color(239, 239, 239);
        Color textColor = new Color(17, 43, 60);
        Color accentColor = new Color(246, 107, 14); // orange
        Color inputBg = new Color(210, 210, 210);
        
        Font titleFont = new Font("Inter", Font.BOLD, 24);
        Font labelFont = new Font("Inter", Font.BOLD, 14);
        Font textFont = new Font("Inter", Font.PLAIN, 12);

        this.getContentPane().setBackground(bgColor);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(bgColor);

        lblName = new JLabel("Meta", SwingConstants.CENTER);
        lblName.setFont(titleFont);
        lblName.setForeground(textColor);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblStatus = new JLabel("$0.00 / $0.00", SwingConstants.CENTER);
        lblStatus.setFont(labelFont);
        lblStatus.setForeground(textColor);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(accentColor);
        progressBar.setFont(textFont);
        progressBar.setPreferredSize(new Dimension(300, 25));

        // Description Area (Read Only)
        JLabel lblDescTitle = new JLabel("Nota:");
        lblDescTitle.setFont(labelFont);
        lblDescTitle.setForeground(textColor);
        lblDescTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDescriptionDisplay = new JTextArea(4, 20);
        txtDescriptionDisplay.setFont(textFont);
        txtDescriptionDisplay.setWrapStyleWord(true);
        txtDescriptionDisplay.setLineWrap(true);
        txtDescriptionDisplay.setEditable(false);
        txtDescriptionDisplay.setBackground(inputBg);
        txtDescriptionDisplay.setForeground(textColor);
        
        //Border for txt Area
        txtDescriptionDisplay.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(5, 5, 5, 5)
        ));

        contentPanel.add(lblName);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(progressBar);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lblStatus);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(lblDescTitle);
        contentPanel.add(Box.createVerticalStrut(5));
        
        JScrollPane scrollDesc = new JScrollPane(txtDescriptionDisplay);
        scrollDesc.setBorder(null); 
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(scrollDesc);

        add(contentPanel, BorderLayout.CENTER);
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