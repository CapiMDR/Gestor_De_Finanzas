package com.example.GoalsModule.Controller;

import com.example.AccountsModule.Model.Account;
import com.example.AccountsModule.Model.AccountManager;
import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalEditView;
import com.example.GoalsModule.View.GoalsView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GoalsController.
 * Tests business logic in isolation by mocking Accounts and Views.
 * @author Jose Pablo Martinez
 */

@ExtendWith(MockitoExtension.class)
class GoalsControllerTest {

    @Mock
    private GoalsView view;
    @Mock
    private GoalEditView editView;
    @Mock
    private AccountManager accountManager;
    @Mock
    private Account currentAccount;

    @InjectMocks
    private GoalsController controller;

    // We need a real list to simulate the Account's internal list
    private List<Goal> mockGoalsList;

    @BeforeEach
    void setUp() {
        // Initialize a real list to act as the Account's storage
        mockGoalsList = new ArrayList<>();
        
        // Tell the Mock Account to return our list when getGoals() is called
        // This allows us to check if items were added/removed
        lenient().when(currentAccount.getGoals()).thenReturn(mockGoalsList);
        
        // Set the context
        controller.setAccount(currentAccount);
        reset(view);
    }

    @Test
    @DisplayName("CreateNewGoal should add to account list and save")
    void testCreateNewGoal() {
        // Arrange
        String name = "New Car";
        BigDecimal target = new BigDecimal("20000");
        String desc = "Saving";

        // Act
        controller.createNewGoal(name, target, desc);

        // Assert
        // 1. Verify it was added to the list
        assertEquals(1, mockGoalsList.size());
        assertEquals("New Car", mockGoalsList.get(0).getName());

        // 2. Verify it called saveAll on the external manager
        verify(accountManager, times(1)).saveAll();

        // 3. Verify it updated the view
        verify(view, times(1)).updateGoalList(mockGoalsList);
    }

    @Test
    @DisplayName("DeleteSelectedGoal should remove from list and save")
    void testDeleteSelectedGoal() {
        // Arrange: Prepare a goal in the list
        Goal goalToDelete = new Goal("Old Goal", BigDecimal.TEN, "Desc");
        mockGoalsList.add(goalToDelete);
        
        // Mock user selection in the view
        when(view.getSelectedGoal()).thenReturn(goalToDelete);

        // Act
        controller.deleteSelectedGoal();

        // Assert
        assertEquals(0, mockGoalsList.size(), "List should be empty after delete");
        verify(accountManager, times(1)).saveAll();
        verify(view, times(1)).updateGoalList(mockGoalsList);
    }

    @Test
    @DisplayName("SaveGoalData should update existing goal object")
    void testSaveGoalData() {
        // Arrange
        Goal existingGoal = new Goal("Old Name", BigDecimal.ONE, "Old Desc");
        
        String newName = "Updated Name";
        BigDecimal newTarget = new BigDecimal("500");
        String newDesc = "Updated Desc";

        // Act
        controller.saveGoalData(existingGoal, newName, newTarget, newDesc);

        // Assert
        assertEquals(newName, existingGoal.getName());
        assertEquals(newTarget, existingGoal.getTargetAmount());
        
        verify(accountManager, times(1)).saveAll();
        verify(view, times(1)).updateGoalList(mockGoalsList);
    }
}