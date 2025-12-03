package goals.goals_controller;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.Account.Coin;
import goals.goals_model.Goal;
import goals.goals_view.GoalEditView;
import goals.goals_view.GoalsView;
import movements.movement_model.Movement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Goals Controller Test")
class GoalsControllerTest {

    @Mock private GoalsView view;
    @Mock private GoalEditView editView;
    @Mock private GoalDetailController detailController;
    @Mock private Account currentAccount;
    @Mock private JButton btnAdd;

    // Se usa Spy con una lista real para verificar que si se agregan elementos
    @Spy
    private List<Goal> goalList = new ArrayList<>();

    private GoalsController controller;

    @BeforeEach
    void setUp() {
        // Lenient (permisiva) sirve para evitar errores si algún mock no se usa en un test específico
        lenient().when(view.getBtnAddGoal()).thenReturn(btnAdd);
        lenient().when(currentAccount.getGoals()).thenReturn(goalList);
        lenient().when(currentAccount.getName()).thenReturn("Cuenta Test");
        lenient().when(currentAccount.getCoin()).thenReturn(Coin.USD);
        lenient().when(currentAccount.getInitialBalance()).thenReturn(BigDecimal.ZERO);
        lenient().when(currentAccount.getMovements()).thenReturn(new ArrayList<Movement>());

        controller = new GoalsController(view, editView, detailController);
        
        controller.setAccount(currentAccount);
        clearInvocations(view); 
    }

    @Test
    @DisplayName("SetAccount should update view with Name, Currency, and List")
    void testSetAccount() {
        controller.setAccount(currentAccount);

        verify(view).setAccountName("Cuenta Test");
        verify(view).setCurrencyLabel("USD");
        verify(view).updateGoalList(goalList);
    }

    @Test
    @DisplayName("CreateNewGoal should add goal to list and save via Static Manager")
    void testCreateNewGoal() {
        // Arrange
        String name = "New Goal";
        BigDecimal target = new BigDecimal("100.00");
        String desc = "Test Description";

        // Mock de la clase estática AccountManager
        try (MockedStatic<AccountManager> mockedManager = mockStatic(AccountManager.class)) {
            
            // Act
            controller.createNewGoal(name, target, desc);

            // Assert (Capturar la meta que se intentó agregar a la lista)
            ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
            verify(goalList).add(goalCaptor.capture());

            Goal createdGoal = goalCaptor.getValue();
            assertEquals(name, createdGoal.getName());
            assertEquals(target, createdGoal.getTargetAmount());
            assertEquals(desc, createdGoal.getDescription());

            // Verificar que se llamó al método
            mockedManager.verify(AccountManager::saveAccountsData);
            verify(view).updateGoalList(goalList);
        }
    }

    @Test
    @DisplayName("OnViewDetails should delegate to DetailController")
    void testOnViewDetails() {
        Goal goal = new Goal();
        controller.onViewDetails(goal);

        verify(detailController, times(1)).showDetails(goal);
    }

    @Test
    @DisplayName("OnEdit should populate edit view and show dialog")
    void testOnEdit() {
        Goal testGoal = new Goal("Test Goal", new BigDecimal("123"), "Test");

        controller.onEdit(testGoal);

        verify(editView).populateFields("Test Goal", new BigDecimal("123"), "Test");
        verify(editView).addSaveListener(any(ActionListener.class));
        verify(editView).showDialog();
    }
}