package goals.goals_controller;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.Account.Coin;
import goals.goals_model.Goal;
import goals.goals_view.GoalsViewFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit and integration tests for {@link GoalsController} using TestFX.
 * Initializes a real JavaFX UI instance to properly test the controller's logic 
 * and avoid ByteBuddy proxy issues with Mockito on Java 25.
 */

@ExtendWith(ApplicationExtension.class)
@DisplayName("Goals Controller Test (TestFX)")
class GoalsControllerTest {

    private GoalsController controller;
    private GoalsViewFX view;
    private Account currentAccount;

    @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/goals/goals.fxml"));
        Parent root = loader.load();
        view = loader.getController();
        
        GoalEditController editController = new GoalEditController();
        GoalDetailControllerFX detailController = new GoalDetailControllerFX();
        
        controller = new GoalsController(view, editController, detailController);
        
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    void setUp() {
        currentAccount = new Account(1, "Cuenta Test", Account.AccountType.CASH, Coin.USD, BigDecimal.ZERO);
        // By default, Account constructor creates an empty list of goals.
    }

    @Test
    @DisplayName("SetAccount should update view with Name and update GoalList")
    void testSetAccount(FxRobot robot) {
        // Arrange
        Goal testGoal = new Goal("Mi Meta", new BigDecimal("100"), "Desc");
        currentAccount.getGoals().add(testGoal);

        // Act
        try (MockedStatic<AccountManager> mockedManager = mockStatic(AccountManager.class)) {
            mockedManager.when(AccountManager::getAccounts).thenReturn(Collections.singletonList(currentAccount));
            
            robot.interact(() -> {
                controller.setAccount(currentAccount);
            });

            // Assert
            assertNotNull(view.getListGoals(), "The goal list should not be null");
            assertEquals(1, view.getListGoals().getItems().size(), "There should be exactly one item in the view list");
            assertEquals("Mi Meta", view.getListGoals().getItems().get(0), "The item should match the goal name");
        }
    }

    @Test
    @DisplayName("onNotify should recalculate progress")
    void testOnNotify(FxRobot robot) {
        // Arrange
        Goal testGoal = new Goal("Mi Meta", new BigDecimal("100"), "Desc");
        currentAccount.getGoals().add(testGoal);

        // Act
        try (MockedStatic<AccountManager> mockedManager = mockStatic(AccountManager.class)) {
            mockedManager.when(AccountManager::getAccounts).thenReturn(Collections.singletonList(currentAccount));
            
            robot.interact(() -> {
                controller.setAccount(currentAccount);
                controller.onNotify(Collections.singletonList(currentAccount));
            });

            // Assert
            assertEquals(BigDecimal.ZERO, testGoal.getCurrentAmount(), "The current amount should be recalculated to 0");
        }
    }
}