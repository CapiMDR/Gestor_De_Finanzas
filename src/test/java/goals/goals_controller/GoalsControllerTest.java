package goals.goals_controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import goals.goals_model.Goal;
import goals.goals_view.GoalsViewFX;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@ExtendWith(MockitoExtension.class)
@DisplayName("GoalsController Test")
class GoalsControllerTest {

    @Mock
    private GoalsViewFX mainView;

    @Mock
    private GoalEditController editController;

    @Mock
    private GoalDetailControllerFX detailController;

    private GoalsController controller;
    private MockedStatic<AccountManager> mockedAccountManager;

    @BeforeAll
    static void initJFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @BeforeEach
    void setUp() {
        // Mock UI elements returned by view to avoid NPE in assignEvents()
        lenient().when(mainView.getBtnAddGoal()).thenReturn(mock(Button.class));
        lenient().when(mainView.getBtnEditGoal()).thenReturn(mock(Button.class));
        lenient().when(mainView.getBtnDeleteGoal()).thenReturn(mock(Button.class));
        lenient().when(mainView.getBtnViewGoalDetails()).thenReturn(mock(Button.class));
        
        @SuppressWarnings("unchecked")
        ListView<String> mockList = mock(ListView.class);
        @SuppressWarnings("unchecked")
        MultipleSelectionModel<String> mockSelectionModel = mock(MultipleSelectionModel.class);
        @SuppressWarnings("unchecked")
        javafx.beans.property.ReadOnlyObjectProperty<String> mockProperty = mock(javafx.beans.property.ReadOnlyObjectProperty.class);
        lenient().when(mockSelectionModel.selectedItemProperty()).thenReturn(mockProperty);
        lenient().when(mockList.getSelectionModel()).thenReturn(mockSelectionModel);
        lenient().when(mainView.getListGoals()).thenReturn(mockList);
        
        lenient().when(mainView.getProgressContainer()).thenReturn(mock(VBox.class));
        lenient().when(mainView.getProgressBarGoal()).thenReturn(mock(ProgressBar.class));

        mockedAccountManager = mockStatic(AccountManager.class);
        
        controller = new GoalsController(mainView, editController, detailController);
    }

    @AfterEach
    void tearDown() {
        mockedAccountManager.close();
    }

    @Test
    @DisplayName("should set account and trigger notify")
    void testSetAccount() {
        Account mockAccount = mock(Account.class);
        when(mockAccount.getName()).thenReturn("Savings");
        when(mockAccount.getGoals()).thenReturn(new ArrayList<>());
        
        // Mock getAccounts to return our mock account
        mockedAccountManager.when(AccountManager::getAccounts).thenReturn(Collections.singletonList(mockAccount));
        
        controller.setAccount(mockAccount);
        
        verify(mainView).setAccountName("Savings");
        // Verify saveAccountsData is called during onNotify triggered by setAccount
        mockedAccountManager.verify(AccountManager::saveAccountsData, times(1));
    }

    @Test
    @DisplayName("should add goal successfully when valid inputs are provided")
    void testHandleAddGoalSuccess() throws Exception {
        Account mockAccount = mock(Account.class);
        when(mockAccount.getName()).thenReturn("Savings");
        when(mockAccount.getGoals()).thenReturn(new ArrayList<>());
        when(mockAccount.getMovements()).thenReturn(new ArrayList<>());
        
        mockedAccountManager.when(AccountManager::getAccounts).thenReturn(Collections.singletonList(mockAccount));
        controller.setAccount(mockAccount);

        TextField mockNameField = mock(TextField.class);
        when(mockNameField.getText()).thenReturn("Car");
        when(mainView.getTxtGoalName()).thenReturn(mockNameField);

        TextField mockTargetField = mock(TextField.class);
        when(mockTargetField.getText()).thenReturn("50000");
        when(mainView.getTxtTargetAmount()).thenReturn(mockTargetField);

        TextField mockDescField = mock(TextField.class);
        when(mockDescField.getText()).thenReturn("");
        lenient().when(mainView.getTxtDescription()).thenReturn(mockDescField);

        // Use reflection to invoke handleAddGoal
        java.lang.reflect.Method method = GoalsController.class.getDeclaredMethod("handleAddGoal");
        method.setAccessible(true);
        method.invoke(controller);

        verify(mockNameField).clear();
        verify(mockTargetField).clear();
        mockedAccountManager.verify(AccountManager::saveAccountsData, atLeastOnce());
        assertEquals(1, mockAccount.getGoals().size());
        assertEquals("Car", mockAccount.getGoals().get(0).getName());
    }

    @Test
    @org.junit.jupiter.api.Disabled("Requires JavaFX Thread for Alert")
    @DisplayName("should delete goal successfully")
    void testHandleDeleteGoal() throws Exception {
        Account mockAccount = mock(Account.class);
        Goal mockGoal = new Goal("Car", new BigDecimal("50000"), "Save for a car");
        ArrayList<Goal> goalsList = new ArrayList<>(Collections.singletonList(mockGoal));
        when(mockAccount.getName()).thenReturn("Savings");
        when(mockAccount.getGoals()).thenReturn(goalsList);
        when(mockAccount.getMovements()).thenReturn(new ArrayList<>());

        mockedAccountManager.when(AccountManager::getAccounts).thenReturn(Collections.singletonList(mockAccount));
        controller.setAccount(mockAccount);

        // Act
        java.lang.reflect.Method method = GoalsController.class.getDeclaredMethod("handleDeleteGoal", Goal.class);
        method.setAccessible(true);
        method.invoke(controller, mockGoal);

        // Assert
        assertEquals(0, goalsList.size());
        mockedAccountManager.verify(AccountManager::saveAccountsData, atLeastOnce());
    }
}