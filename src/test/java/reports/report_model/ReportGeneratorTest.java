package reports.report_model;

import accounts.account_model.Account;
import movements.movement_model.Movement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ReportGenerator}.
 * Validates the generation of reports for different time periods.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Report Generator Test")
@SuppressWarnings("java:S5973")
class ReportGeneratorTest {

    @Mock
    private ReportSubject reportSubject;

    @Mock
    private Account account;

    private ReportGenerator reportGenerator;

    /**
     * Captures the ReportData object passed to the observer to verify its contents.
     */
    @Captor
    private ArgumentCaptor<ReportData> reportDataCaptor;

    /**
     * Initializes the ReportGenerator instance with mocked dependencies.
     */
    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGenerator(reportSubject, account);
    }

    /**
     * Helper method to create a mock Movement with a specific amount and date.
     *
     * @param amountStr the amount as a string
     * @param date      the date and time of the movement
     * @return the mocked Movement instance
     */
    private Movement createMockMovement(String amountStr, LocalDateTime date) {
        Movement m = mock(Movement.class);
        lenient().when(m.getAmount()).thenReturn(new BigDecimal(amountStr));
        lenient().when(m.getDate()).thenReturn(date);
        return m;
    }

    /**
     * Tests that the {@code today()} method correctly filters movements 
     * to include only those occurring on the current day.
     */
    @Test
    @DisplayName("today() should filter only today's movements")
    void testTodayReport() {
        // Arrange
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        
        Movement m1 = createMockMovement("100.50", today);
        Movement m2 = createMockMovement("50.00", yesterday);

        when(account.getMovements()).thenReturn(Arrays.asList(m1, m2));

        // Act
        reportGenerator.today();

        // Assert
        verify(reportSubject).notifyObservers(reportDataCaptor.capture());
        ReportData data = reportDataCaptor.getValue();

        assertEquals("Hoy", data.getPeriodName());
        assertEquals(1, data.getMovements().size());
        assertEquals(new BigDecimal("100.50"), data.getTotalAmount());
        assertTrue(data.getMovements().contains(m1));
    }

    /**
     * Tests that the {@code weekAgo()} method correctly filters movements 
     * to include only those occurring within the last 7 days.
     */
    @Test
    @DisplayName("weekAgo() should filter movements from the last 7 days")
    void testWeekAgoReport() {
        // Arrange
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime threeDaysAgo = today.minusDays(3);
        LocalDateTime eightDaysAgo = today.minusDays(8);

        Movement m1 = createMockMovement("200.00", today);
        Movement m2 = createMockMovement("50.00", threeDaysAgo);
        Movement m3 = createMockMovement("10.00", eightDaysAgo);

        when(account.getMovements()).thenReturn(Arrays.asList(m1, m2, m3));

        // Act
        reportGenerator.weekAgo();

        // Assert
        verify(reportSubject).notifyObservers(reportDataCaptor.capture());
        ReportData data = reportDataCaptor.getValue();

        assertEquals("Ultimos 7 días", data.getPeriodName());
        assertEquals(2, data.getMovements().size());
        assertEquals(new BigDecimal("250.00"), data.getTotalAmount());
        assertTrue(data.getMovements().containsAll(Arrays.asList(m1, m2)));
    }

    /**
     * Tests that the {@code yesterday()} method correctly filters movements 
     * to include only those occurring exactly one day ago.
     */
    @Test
    @DisplayName("yesterday() should filter only yesterday's movements")
    void testYesterdayReport() {
        // Arrange
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime twoDaysAgo = today.minusDays(2);

        Movement m1 = createMockMovement("100.00", today);
        Movement m2 = createMockMovement("30.00", yesterday);
        Movement m3 = createMockMovement("10.00", twoDaysAgo);

        when(account.getMovements()).thenReturn(Arrays.asList(m1, m2, m3));

        // Act
        reportGenerator.yesterday();

        // Assert
        verify(reportSubject).notifyObservers(reportDataCaptor.capture());
        ReportData data = reportDataCaptor.getValue();

        assertEquals("Ayer", data.getPeriodName());
        assertEquals(1, data.getMovements().size());
        assertEquals(new BigDecimal("30.00"), data.getTotalAmount());
        assertTrue(data.getMovements().contains(m2));
    }

    /**
     * Tests that the {@code currentWeek()} method correctly filters movements 
     * from Monday to Sunday of the current week.
     */
    @Test
    @DisplayName("currentWeek() should filter movements from current week (Mon-Sun)")
    void testCurrentWeekReport() {
        // Arrange
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime monday = today.toLocalDate().with(java.time.DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime previousSunday = monday.minusDays(1);

        Movement m1 = createMockMovement("40.00", today);
        Movement m2 = createMockMovement("20.00", monday);
        Movement m3 = createMockMovement("100.00", previousSunday);

        when(account.getMovements()).thenReturn(Arrays.asList(m1, m2, m3));

        // Act
        reportGenerator.currentWeek();

        // Assert
        verify(reportSubject).notifyObservers(reportDataCaptor.capture());
        ReportData data = reportDataCaptor.getValue();

        assertEquals("Semana Actual", data.getPeriodName());
        assertEquals(2, data.getMovements().size());
        assertEquals(new BigDecimal("60.00"), data.getTotalAmount());
        assertTrue(data.getMovements().containsAll(Arrays.asList(m1, m2)));
    }

    /**
     * Tests that when there are no movements, the total calculated amount
     * is correctly returned as zero.
     */
    @Test
    @DisplayName("amountTotal() logic works correctly (empty list returns ZERO)")
    void testEmptyMovements() {
        // Arrange
        when(account.getMovements()).thenReturn(Collections.emptyList());

        // Act
        reportGenerator.today();

        // Assert
        verify(reportSubject).notifyObservers(reportDataCaptor.capture());
        ReportData data = reportDataCaptor.getValue();

        assertTrue(data.getMovements().isEmpty());
        assertEquals(new BigDecimal("0"), data.getTotalAmount());
    }
}