package movements.movements_model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import movements.movement_model.CategoryObserver;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementManagerSubject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MovementManagerSubject Test")
class MovementManagerSubjectTest {

    @Mock
    private CategoryObserver mockObserver;

    @BeforeEach
    @AfterEach
    void resetObservers() throws Exception {
        // Since MovementManagerSubject uses static state, we need to reset it
        // to prevent test contamination.
        Field observersField = MovementManagerSubject.class.getDeclaredField("observers");
        observersField.setAccessible(true);
        observersField.set(null, new ArrayList<>());
    }

    @Test
    @DisplayName("addObserver and notifyObservers should notify registered observer")
    void testAddAndNotify() {
        MovementManagerSubject.addObserver(mockObserver);
        List<MovementCategory> categories = Collections.emptyList();

        MovementManagerSubject.notifyObservers(categories);

        verify(mockObserver, times(1)).onNotify(categories);
    }

    @Test
    @DisplayName("removeObserver should prevent notification for that observer")
    void testRemoveObserver() {
        MovementManagerSubject.addObserver(mockObserver);
        MovementManagerSubject.removeObserver(mockObserver);

        MovementManagerSubject.notifyObservers(Collections.emptyList());

        verify(mockObserver, never()).onNotify(any());
    }

    @Test
    @DisplayName("notifyObservers should handle multiple observers")
    void testNotifyMultiple() {
        CategoryObserver observer2 = mock(CategoryObserver.class);
        
        MovementManagerSubject.addObserver(mockObserver);
        MovementManagerSubject.addObserver(observer2);
        
        List<MovementCategory> categories = Collections.emptyList();
        MovementManagerSubject.notifyObservers(categories);

        verify(mockObserver, times(1)).onNotify(categories);
        verify(observer2, times(1)).onNotify(categories);
    }
}
