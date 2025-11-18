package com.example.GoalsModule.Controller;

import com.example.GoalsModule.Model.Goal;
import com.example.GoalsModule.View.GoalDetailView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Unit tests for GoalDetailController.
 * Uses Mockito to simulate the View interaction.
 * @author Jose Pablo Martinez
 */

@ExtendWith(MockitoExtension.class)
class GoalDetailControllerTest {

    @Mock
    private GoalDetailView view; // Mock of the View (doesn't need to exist functionally)

    @InjectMocks
    private GoalDetailController controller;

    @Test
    @DisplayName("Should update view when goal is provided")
    void testShowDetails() {
        Goal goal = new Goal();
        
        // Act
        controller.showDetails(goal);

        // Assert: Verify that view.showProgress() was called exactly once
        verify(view, times(1)).showProgress(goal);
    }

    @Test
    @DisplayName("Should do nothing if goal is null")
    void testShowDetailsNull() {
        // Act
        controller.showDetails(null);

        // Assert: Verify no interaction with view
        verifyNoInteractions(view);
    }
}
