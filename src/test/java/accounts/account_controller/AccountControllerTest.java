package accounts.account_controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_view.AccountViewFX;
import javafx.scene.control.Button;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AccountController}.
 * Verifies that the controller correctly communicates user actions to the AccountManager
 * and updates the view upon model changes.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AccountController Test")
class AccountControllerTest {

    @Mock
    private AccountViewFX view;

    @Mock
    private Button btnAddAccount;

    @Mock
    private Button btnDeleteAccount;

    @Mock
    private Button btnEditAccount;

    @Mock
    private Button btnAccessAccount;

    @Mock
    private Button btnCalculateInterest;

    private AccountController controller;
    private MockedStatic<AccountManager> mockedAccountManager;

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
     * Initializes the mocks and injects the controller before each test.
     */
    @BeforeEach
    void setUp() {
        // Leniently mock the view accessors so the controller's AssignEvents doesn't throw NPE
        lenient().when(view.getBtnAddAccount()).thenReturn(btnAddAccount);
        lenient().when(view.getBtnDeleteAccount()).thenReturn(btnDeleteAccount);
        lenient().when(view.getBtnEditAccount()).thenReturn(btnEditAccount);
        lenient().when(view.getBtnAccessAccount()).thenReturn(btnAccessAccount);
        lenient().when(view.getBtnCalculateInterest()).thenReturn(btnCalculateInterest);

        mockedAccountManager = mockStatic(AccountManager.class);
        
        controller = new AccountController(view);
    }

    /**
     * Closes the static mock after each test to avoid memory leaks.
     */
    @AfterEach
    void tearDown() {
        mockedAccountManager.close();
    }

    /**
     * Tests that adding an account fetches data from the view and calls AccountManager.
     */
    @Test
    @DisplayName("should update model when add account is triggered")
    void testAddAccountUpdatesModel() throws Exception {
        // Arrange
        when(view.getAccountName()).thenReturn("Ahorro");
        when(view.getInitialBalanceText()).thenReturn("5000.00");
        when(view.getSelectedAccountType()).thenReturn("Efectivo");
        when(view.getSelectedCurrency()).thenReturn("MXN");

        // Act
        // Use reflection to invoke the private addAccount method
        java.lang.reflect.Method method = AccountController.class.getDeclaredMethod("addAccount");
        method.setAccessible(true);
        method.invoke(controller);

        // Assert
        mockedAccountManager.verify(() -> AccountManager.addAccount(
                eq("Ahorro"),
                eq(Account.AccountType.CASH),
                eq(Account.Coin.MXN),
                eq(new BigDecimal("5000.00"))
        ));
        verify(view).showInfo(anyString(), anyString());
        verify(view).clearForm();
    }

    /**
     * Tests that deleting an account fetches the selected index and calls AccountManager.
     */
    @Test
    @DisplayName("should update model when delete account is triggered")
    void testDeleteAccountUpdatesModel() throws Exception {
        // Arrange
        int selectedIndex = 0;
        when(view.getSelectedAccountIndex()).thenReturn(selectedIndex);

        Account mockAccount = mock(Account.class);
        when(mockAccount.getId()).thenReturn(10);
        when(mockAccount.getName()).thenReturn("Test Account");

        mockedAccountManager.when(() -> AccountManager.getAccountByIndex(selectedIndex))
                            .thenReturn(mockAccount);

        when(view.showConfirm(anyString(), anyString())).thenReturn(true);

        // Act
        // Use reflection to invoke the private deleteAccount method
        java.lang.reflect.Method method = AccountController.class.getDeclaredMethod("deleteAccount");
        method.setAccessible(true);
        method.invoke(controller);

        // Assert
        mockedAccountManager.verify(() -> AccountManager.removeAccount(10));
        verify(view).showInfo(anyString(), anyString());
    }

    /**
     * Tests that receiving a notification updates the account list in the view.
     * This replaces the legacy behavior of view field updates on selection.
     */
    @Test
    @DisplayName("should update view data when notified of changes")
    void testSelectAccountUpdatesView() {
        // Arrange
        Account mockAccount = mock(Account.class);
        List<Account> accounts = Collections.singletonList(mockAccount);

        // Act
        controller.onNotify(accounts);

        // Assert
        verify(view).updateAccountList(accounts);
    }
}
