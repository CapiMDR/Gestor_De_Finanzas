package com.example.goals.goals_controller;

import com.example.goals.goals_model.Goal;
import com.example.goals.goals_view.GoalDetailView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the GoalDetailController.
 * Uses Mockito to verify that the controller correctly updates the view.
 * @author Jose Pablo Martinez
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Goal Detail Controller Test")
class GoalDetailControllerTest {

    @Mock
    private GoalDetailView view; // Mock the view dependency

    @InjectMocks
    private GoalDetailController controller; // Inject mock into the controller

    @Test
    @DisplayName("Should call view.showProgress when goal is valid")
    void testShowDetails() {
        // Arrange
        Goal testGoal = new Goal();
        
        // Act: Execute the method to be tested
        controller.showDetails(testGoal);
        
        // Assert: Verify that the controller called the view's method exactly once
        verify(view, times(1)).showProgress(testGoal);
    }

    @Test
    @DisplayName("Should not do anything if goal is null")
    void testShowDetailsWithNull() {
        // Act
        controller.showDetails(null);
        
        // Assert: Verify that no methods on the view were called
        verifyNoInteractions(view);
    }
}
