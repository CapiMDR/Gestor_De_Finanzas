package goals.goals_controller;

import goals.goals_model.Goal;
import goals.goals_view.GoalDetailView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Goal Detail Controller Test")
class GoalDetailControllerTest {

    @Mock
    private GoalDetailView view; // Mock de la vista

    @InjectMocks
    private GoalDetailController controller;

    @Test
    @DisplayName("Should call view.showProgress when goal is valid")
    void testShowDetails() {
        // Arrange (Preparar)
        Goal testGoal = new Goal();

        // Act (Ejecutar)
        controller.showDetails(testGoal);

        // Assert (Verificar)
        // Verificar que se llamo al método con esa meta
        verify(view, times(1)).showProgress(testGoal);
    }

    @Test
    @DisplayName("Should not do anything if goal is null")
    void testShowDetailsWithNull() {
        // Act
        controller.showDetails(null);

        // Assert
        // Verficar que no hubo interacción con la vista
        verifyNoInteractions(view);
    }
}
