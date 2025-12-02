package com.example.goals.goals_controller;

import com.example.goals.goals_model.Goal;
import com.example.goals.goals_view.GoalEditView;
import com.example.goals.goals_view.GoalsView;
import com.example.account.account_model.Account;
import com.example.account.account_model.AccountManager;
import com.example.account.account_model.Account.Coin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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

/**
 * Unit tests for the main GoalsController.
 * Tests business logic in isolation by mocking Accounts, Views, and sub-controllers.
 * @author Jose Pablo Martinez
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Goals Controller Test")
class GoalsControllerTest {

    // Mock all dependencies
    @Mock private GoalsView view;
    @Mock private GoalEditView editView;
    @Mock private GoalDetailController detailController;
    @Mock private AccountManager accountManager;
    @Mock private Account currentAccount;
    
    @Mock private JButton btnAdd;

    // Spy on a real list to verify that items are being added/removed
    @Spy
    private List<Goal> goalList = new ArrayList<>();

    private GoalsController controller;

    @BeforeEach
    void setUp() {
        
        lenient().when(view.getBtnAddGoal()).thenReturn(btnAdd);
        lenient().when(currentAccount.getGoals()).thenReturn(goalList);
        lenient().when(currentAccount.getName()).thenReturn("Cuenta Test");
        lenient().when(currentAccount.getCoin()).thenReturn(Coin.USD);
        
        controller = new GoalsController(view, editView, detailController, accountManager);
        
        controller.setAccount(currentAccount);
        reset(view); 
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
    @DisplayName("CreateNewGoal should add goal to list and save")
    void testCreateNewGoal() {
        // Arrange
        String name = "New Goal";
        BigDecimal target = new BigDecimal("100.00");
        String desc = "Test Description";
        // Act
        controller.createNewGoal(name, target, desc);
        ArgumentCaptor<Goal> goalCaptor = ArgumentCaptor.forClass(Goal.class);
        
        verify(goalList).add(goalCaptor.capture());
        
        Goal createdGoal = goalCaptor.getValue();
        assertEquals(name, createdGoal.getName());
        assertEquals(target, createdGoal.getTargetAmount());
        assertEquals(desc, createdGoal.getDescription());
        
        verify(accountManager).saveAccountsData(); 
        verify(view).updateGoalList(goalList);
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