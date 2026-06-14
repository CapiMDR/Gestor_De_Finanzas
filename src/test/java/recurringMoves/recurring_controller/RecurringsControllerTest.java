package recurringMoves.recurring_controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.SingleSelectionModel;
import movements.movement_model.MovementCategory;
import recurringMoves.recurring_model.RecurrenceType;
import recurringMoves.recurring_model.RecurringMove;
import recurringMoves.recurring_model.RecurringsModel;
import recurringMoves.recurring_view.RecurringsViewFX;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RecurringsController}.
 * Verifies the scheduling of recurring movements and their addition logic.
 */

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
@DisplayName("RecurringsController Test")
@SuppressWarnings("java:S5973")
class RecurringsControllerTest {

    @Mock
    private RecurringsModel recurringsModel;

    @Mock
    private ScheduledExecutorService scheduler;

    @Mock
    private RecurringsViewFX recurringsView;

    @Mock
    private TextField txtDescription;

    @Mock
    private TextField txtAmount;

    @Mock
    private ComboBox<String> cmbCategory;

    @Mock
    private SingleSelectionModel<String> categorySelectionModel;

    @Mock
    private DatePicker datePicker;

    @Mock
    private javafx.scene.control.ListView<String> listRecurrings;

    @Mock
    private javafx.scene.control.Button btnAddRecurring;

    @Mock
    private javafx.scene.control.Button btnDeleteRecurring;

    @Mock
    private javafx.scene.control.Button btnEditRecurring;

    @Mock
    private javafx.collections.ObservableList<String> mockItems;

    private RecurringsController controller;

    @Captor
    private ArgumentCaptor<Runnable> taskCaptor;

    /**
     * Initializes the JavaFX Toolkit before any mocks are created.
     */
    @org.junit.jupiter.api.BeforeAll
    static void initJFX() {
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    /**
     * Initializes the controller with the mocked dependencies before each test.
     */
    @BeforeEach
    void setUp() {
        controller = new RecurringsController(recurringsModel, scheduler);
    }

    /**
     * Tests that a recurring movement whose date is in the past is triggered.
     */
    @Test
    @DisplayName("scheduler should trigger expired recurrings")
    void testWatchRecurringsExecutesDueItems() {
        // Arrange
        verify(scheduler).scheduleAtFixedRate(taskCaptor.capture(), eq(0L), eq(1L), eq(TimeUnit.SECONDS));
        Runnable watchTask = taskCaptor.getValue();

        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        RecurringMove dueMove = new RecurringMove("Due", new BigDecimal("100"), "Due", pastDate, RecurrenceType.Mensual, null);
        
        TreeSet<RecurringMove> mockSet = new TreeSet<>(Comparator.comparing(RecurringMove::getInitialDate));
        mockSet.add(dueMove);
        when(recurringsModel.getRecurrings()).thenReturn(mockSet);

        // Act
        watchTask.run();

        // Assert
        assertFalse(dueMove.shouldTrigger());
    }

    /**
     * Tests that a recurring movement whose date is in the future is not triggered.
     */
    @Test
    @DisplayName("scheduler should not trigger future recurrings")
    void testWatchRecurringsSkipsFutureItems() {
        // Arrange
        verify(scheduler).scheduleAtFixedRate(taskCaptor.capture(), eq(0L), eq(1L), eq(TimeUnit.SECONDS));
        Runnable watchTask = taskCaptor.getValue();

        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        RecurringMove futureMove = spy(new RecurringMove("Future", new BigDecimal("100"), "Future", futureDate, RecurrenceType.Mensual, null));
        
        TreeSet<RecurringMove> mockSet = new TreeSet<>(Comparator.comparing(RecurringMove::getInitialDate));
        mockSet.add(futureMove);
        when(recurringsModel.getRecurrings()).thenReturn(mockSet);

        // Act
        watchTask.run();

        // Assert
        verify(futureMove, never()).setTriggered(true);
    }

    /**
     * Tests that valid inputs from the view trigger the addition of a new 
     * recurring movement in the model and save it.
     */
    @Test
    @DisplayName("should add recurring when input is valid")
    void testAddRecurringCallsModel() {
        // Arrange
        // Leniently mock view accessors to prevent NPEs during setView() initialization
        lenient().when(recurringsView.getListRecurrings()).thenReturn(listRecurrings);
        lenient().when(recurringsView.getBtnAddRecurring()).thenReturn(btnAddRecurring);
        lenient().when(recurringsView.getBtnDeleteRecurring()).thenReturn(btnDeleteRecurring);
        lenient().when(recurringsView.getBtnEditRecurring()).thenReturn(btnEditRecurring);
        lenient().when(recurringsView.getCmbCategory()).thenReturn(cmbCategory);
        lenient().when(cmbCategory.getItems()).thenReturn(mockItems);
        
        controller.setView(recurringsView);

        // Since setView calls refreshView which interacts with model and views,
        // we leniently mock the view accessors.
        lenient().when(recurringsView.getTxtDescription()).thenReturn(txtDescription);
        lenient().when(recurringsView.getTxtAmount()).thenReturn(txtAmount);
        lenient().when(recurringsView.getCmbCategory()).thenReturn(cmbCategory);
        lenient().when(cmbCategory.getSelectionModel()).thenReturn(categorySelectionModel);
        lenient().when(recurringsView.getDatePicker()).thenReturn(datePicker);

        when(txtDescription.getText()).thenReturn("Netflix");
        when(txtAmount.getText()).thenReturn("15.99");
        when(categorySelectionModel.getSelectedItem()).thenReturn("Egreso");
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        when(datePicker.getValue()).thenReturn(tomorrow);

        // Act
        controller.handleRecurringAddition();

        // Assert
        verify(recurringsModel).addRecurring(eq("Netflix"), eq(new BigDecimal("15.99")), eq("Netflix"), any(LocalDateTime.class), eq(RecurrenceType.Mensual), any(MovementCategory.class));
        verify(recurringsModel).saveRecurrings();
    }
}
