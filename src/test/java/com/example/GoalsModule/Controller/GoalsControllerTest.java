package com.example.GoalsModule.Controller;

import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalEditView;
import com.example.GoalsModule.View.GoalsView;
import com.example.AccountsModule.Model.Account;
import com.example.AccountsModule.Model.AccountManager;
import com.example.AccountsModule.Model.Coin; // Importante para el test de moneda

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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the main GoalsController.
 * Updated to reflect the new UI logic (Currency, Account Name).
 *
 * @author Jose Pablo Martinez
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Goals Controller Test")
class GoalsControllerTest {

    @Mock private GoalsView view;
    @Mock private GoalEditView editView;
    @Mock private GoalDetailController detailController;
    @Mock private AccountManager accountManager;
    @Mock private Account currentAccount;
    
    // Mock Buttons
    @Mock private JButton btnAdd;

    // Spy List
    @Spy private List<Goal> goalList = new ArrayList<>();

    private GoalsController controller;

    @BeforeEach
    void setUp() {
        // Stubbing View
        lenient().when(view.getBtnAddGoal()).thenReturn(btnAdd);
        
        // Stubbing Account
        lenient().when(currentAccount.getGoals()).thenReturn(goalList);
        lenient().when(currentAccount.getName()).thenReturn("Test Account"); // Para setAccountName
        lenient().when(currentAccount.getCoin()).thenReturn(Coin.USD);       // Para setCurrencyLabel
        
        // Initialize Controller
        controller = new GoalsController(view, editView, detailController, accountManager);
        
        // Set Account (Triggers logic we want to test separately, so we reset after)
        controller.setAccount(currentAccount);
        
        // Clean slate for tests
        reset(view); 
    }

    @Test
    @DisplayName("SetAccount should update view with Name, Currency, and List")
    void testSetAccount() {
        // Act
        controller.setAccount(currentAccount);
        
        // Assert
        verify(view).setAccountName("Test Account");
        verify(view).setCurrencyLabel("$"); // Assumes Coin.USD.toString() returns "$"
        verify(view).updateGoalList(goalList);
    }

    @Test
    @DisplayName("CreateNewGoal should add to list and save")
    void testCreateNewGoal() {
        String name = "New Goal";
        BigDecimal target = new BigDecimal("100.00");
        String desc = "Desc";
        
        controller.createNewGoal(name, target, desc);

        ArgumentCaptor<Goal> captor = ArgumentCaptor.forClass(Goal.class);
        verify(goalList).add(captor.capture());
        
        Goal added = captor.getValue();
        assertEquals(name, added.getName());
        assertEquals(target, added.getTargetAmount());
        
        verify(accountManager).saveAll();
        verify(view).updateGoalList(goalList);
    }

    @Test
    @DisplayName("OnViewDetails should delegate to DetailController")
    void testOnViewDetails() {
        Goal goal = new Goal();
        controller.onViewDetails(goal);
        verify(detailController).showDetails(goal);
    }

    @Test
    @DisplayName("OnEdit should populate edit view")
    void testOnEdit() {
        Goal goal = new Goal("Test", BigDecimal.TEN, "Desc");
        controller.onEdit(goal);
        
        verify(editView).populateFields("Test", BigDecimal.TEN, "Desc");
        verify(editView).addSaveListener(any(ActionListener.class));
        verify(editView).showDialog();
    }
    
}