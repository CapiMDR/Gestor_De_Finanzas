package goals.goals_controller;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for {@link GoalDetailControllerFX}.
 * Tests the detail view presentation logic.
 */

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
@DisplayName("Goal Detail Controller Test")
class GoalDetailControllerTest {

    @InjectMocks
    private GoalDetailControllerFX controller;

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @Test
    @DisplayName("Should handle null goal gracefully without crashing")
    void testShowDetailsWithNull() {
        // Act & Assert
        // The method uses a try-catch, so passing null should trigger a NullPointerException
        // that gets caught internally, thus not throwing anything out of the method.
        assertDoesNotThrow(() -> controller.showDetails(null));
    }
}
