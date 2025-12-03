package goals;

import javax.swing.JFrame;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import goals.goals_controller.GoalDetailController;
import goals.goals_controller.GoalsController;
import goals.goals_view.GoalDetailView;
import goals.goals_view.GoalEditView;
import goals.goals_view.GoalsView;

/**
 * Punto de entrada principal de la aplicación "Financial Manager".
 * Integra los módulos de Cuentas y Metas.
 *
 * @author Integración del Equipo
 */

public class GoalsModule {

    public static void initGoals(Account selectedAccount) {
        GoalsView goalsView = new GoalsView();

        JFrame frame = new JFrame("Metas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
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
            // Pasar la cuenta al módulo de metas
            goalsController.setAccount(selectedAccount);
        } else {
            System.out.println("No se seleccionó cuenta");
        }
        frame.setVisible(true);
    }
}