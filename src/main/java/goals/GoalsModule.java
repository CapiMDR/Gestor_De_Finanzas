package goals;

import javax.swing.JFrame;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import goals.goals_controller.GoalDetailController;
import goals.goals_controller.GoalsController;
import goals.goals_view.GoalDetailView;
import goals.goals_view.GoalEditView;
import goals.goals_view.GoalsView;

/**
 * Main entry point of the "Financial Manager" application.
 * Integrates the Accounts and Goals modules.
 *
 * @author Team Integration
 */

public class GoalsModule {

    public static void initGoals(Account selectedAccount) {
        GoalsView goalsView = new GoalsView();

        JFrame frame = new JFrame("Metas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1040, 740);
        frame.add(goalsView);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        GoalEditView goalEditView = new GoalEditView();
        GoalDetailView goalDetailView = new GoalDetailView();
        GoalDetailController goalDetailController = new GoalDetailController(goalDetailView);
        GoalsController goalsController = new GoalsController(
                goalsView,
                goalEditView,
                goalDetailController);
        if (selectedAccount != null) {
            // Pass the account to the goals module
            goalsController.setAccount(selectedAccount);
        }

        // Unregister observer when closing the window to prevent accumulation
        // de observers muertos y notificaciones duplicadas
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                AccountManagerSubject.removeObserver(goalsController);
            }
        });

        frame.setVisible(true);
    }
}