package com.example.GoalsModule.View;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import com.example.GoalsModule.Model.Goal;
import java.math.BigDecimal;
import java.util.List;

/**
 * Dummy class for testing
 * @author Jose Pablo Martinez
 */

public class GoalsView {
    public JButton getBtnAddGoal() { return new JButton(); }
    public JButton getBtnDeleteGoal() { return new JButton(); }
    public JButton getBtnEditGoal() { return new JButton(); }
    public String getGoalName() { return ""; }
    public BigDecimal getTargetAmount() { return BigDecimal.ZERO; }
    public String getDescription() { return ""; }
    public void updateGoalList(List<Goal> goals) {}
    public Goal getSelectedGoal() { return null; }
}