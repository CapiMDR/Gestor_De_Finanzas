package reports.report_controller;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kordamp.ikonli.javafx.FontIcon;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reports.report_model.ReportData;
import reports.report_model.ReportGenerator;
import reports.report_view.ReportsViewFX;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ReportController}.
 * Verifies chart updating, data sync, and UI event bindings.
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("ReportController Test")
@SuppressWarnings("java:S5973")
class ReportControllerTest {

    @Mock
    private ReportsViewFX view;

    @Mock
    private ReportGenerator generator;

    @Mock
    private Account account;

    @Mock
    private FontIcon accountIcon;

    private ReportController controller;
    private MockedStatic<AccountManagerSubject> mockedSubject;

    // Real JavaFX controls for testing without NullPointerExceptions
    private Button btnToday;
    private Button btnYesterday;
    private Button btnCurrentWeek;
    private Button btnWeek;
    private PieChart pieChart;
    private BarChart<String, Number> barChart;
    private Label lblAccountName;
    private Label lblAccountBalance;
    private HBox dummyBox;

    @BeforeAll
    static void initJFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @BeforeEach
    void setUp() {
        btnToday = new Button();
        btnYesterday = new Button();
        btnCurrentWeek = new Button();
        btnWeek = new Button();
        pieChart = new PieChart();
        barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        lblAccountName = new Label();
        lblAccountBalance = new Label();
        dummyBox = new HBox();

        // Stub the view
        lenient().when(view.getBtnToday()).thenReturn(btnToday);
        lenient().when(view.getBtnYesterday()).thenReturn(btnYesterday);
        lenient().when(view.getBtnCurrentWeek()).thenReturn(btnCurrentWeek);
        lenient().when(view.getBtnWeek()).thenReturn(btnWeek);
        lenient().when(view.getPieChartMovements()).thenReturn(pieChart);
        lenient().when(view.getBarChartMovements()).thenReturn(barChart);
        lenient().when(view.getLblAccountName()).thenReturn(lblAccountName);
        lenient().when(view.getLblAccountBalance()).thenReturn(lblAccountBalance);
        lenient().when(view.getAccountIcon()).thenReturn(accountIcon);
        lenient().when(view.getNavAddMovement()).thenReturn(dummyBox);
        lenient().when(view.getNavGoals()).thenReturn(dummyBox);
        lenient().when(view.getNavReminders()).thenReturn(dummyBox);
        lenient().when(view.getNavRecurrings()).thenReturn(dummyBox);
        lenient().when(view.getNavFilters()).thenReturn(dummyBox);
        lenient().when(view.getNavCredit()).thenReturn(dummyBox);

        lenient().when(account.getName()).thenReturn("Main Account");
        lenient().when(account.getCurrentBalance()).thenReturn(new BigDecimal("1500.50"));
        lenient().when(account.getType()).thenReturn(Account.AccountType.DIGITAL);

        mockedSubject = mockStatic(AccountManagerSubject.class);

        controller = new ReportController();
    }

    @AfterEach
    void tearDown() {
        mockedSubject.close();
    }

    @Test
    @DisplayName("should sync account details to view on init")
    void testSyncAccountOnInit() {
        // Act
        controller.setViewModule(view, generator, account);

        // Assert
        assertEquals("Main Account", lblAccountName.getText());
        assertEquals("$1500.50", lblAccountBalance.getText());
        verify(accountIcon).setIconLiteral("mdi2c-credit-card");
    }

    @Test
    @DisplayName("should trigger today generator when today button clicked")
    void testTodayButtonAction() {
        // Arrange
        controller.setViewModule(view, generator, account);

        // Act
        btnToday.fire();

        // Assert
        verify(generator).today();
        assertTrue(btnToday.getStyleClass().contains("btn-filter-selected"));
    }

    @Test
    @DisplayName("should render charts correctly on report notify via runLater")
    void testShowChartsOnNotify() throws InterruptedException {
        // Arrange
        controller.setViewModule(view, generator, account);

        List<Movement> movements = new ArrayList<>();
        movements.add(new Movement(UUID.randomUUID(), "Sueldo", new BigDecimal("5000"),
                new MovementCategory("Salario", MovementCategory.MovementType.INCOME), account, LocalDateTime.now()));
        movements.add(new Movement(UUID.randomUUID(), "Comida", new BigDecimal("1000"),
                new MovementCategory("Alimentos", MovementCategory.MovementType.EXPENSE), account, LocalDateTime.now()));

        ReportData data = new ReportData("Esta Semana", movements, BigDecimal.ZERO, "");

        // Act
        controller.onNotify(data);

        // Wait for runLater to finish
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(latch::countDown);
        latch.await();

        // Assert
        assertEquals(2, pieChart.getData().size());
        assertEquals(1, barChart.getData().size());
        assertEquals("Esta Semana", barChart.getData().get(0).getName());
        assertEquals(2, barChart.getData().get(0).getData().size()); // 1 income, 1 expense bar
    }

    @Test
    @DisplayName("should sync account on AccountManager notify if account matches")
    void testOnNotifyAccountChange() {
        // Arrange
        controller.setViewModule(view, generator, account);

        Account updatedAccount = mock(Account.class);
        when(updatedAccount.getName()).thenReturn("Main Account");
        when(updatedAccount.getCurrentBalance()).thenReturn(new BigDecimal("2000.00"));
        when(updatedAccount.getType()).thenReturn(Account.AccountType.CASH);

        List<Account> accounts = List.of(updatedAccount);

        // Act
        controller.onNotify(accounts);

        // Assert
        assertEquals("$2000.00", lblAccountBalance.getText());
        verify(accountIcon).setIconLiteral("mdi2p-piggy-bank");
    }
}
