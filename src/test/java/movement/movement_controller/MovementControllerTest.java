package movement.movement_controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.JsonDataHandler;
import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;
import movements.movement_controller.MovementController;
import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.CategoryManager;
import movements.movement_model.MovementManagerSubject;
import movements.movement_model.MovementCategory.MovementType;
import movements.movement_view.MovementManagerView;

class TestDataHandlerStub extends JsonDataHandler {
    public List<Account> inMemoryAccounts = new ArrayList<>();
    public int saveCallCount = 0;

    @Override
    public List<Account> loadAccounts() {
        return new ArrayList<>(inMemoryAccounts);
    }

    @Override
    public void saveAccounts(List<Account> accounts) {
        this.inMemoryAccounts = new ArrayList<>(accounts);
        this.saveCallCount++;
    }

    public int getSaveCallCount() {
        return saveCallCount;
    }
}

class TestMovementManagerStub extends CategoryManager {
    private int addMovementCallCount = 0;
    private List<Movement> movements = new ArrayList<>();

    public TestMovementManagerStub(MovementManagerSubject subject, JsonDataHandler dataHandler) {
        super(subject, dataHandler);
    }

    @Override
    public void addMovement(Movement movement) {
        this.movements.add(movement);
        this.addMovementCallCount++;
    }

    @Override
    public List<Movement> getMovements() {
        return this.movements;
    }

    public int getAddMovementCallCount() {
        return addMovementCallCount;
    }
}

@ExtendWith(MockitoExtension.class)
public class MovementControllerTest {

    private TestDataHandlerStub dataHandlerStub;
    private AccountManagerSubject accountSubject;
    private AccountManager accountManager;

    private MovementManagerSubject movementSubject;
    private TestMovementManagerStub movementManagerStub;

    @Mock
    private MovementManagerView view;

    @Mock
    private JButton btnAddIncome;
    @Mock
    private JButton btnAddExpense;
    @Mock
    private JButton btnAddCategoryIncome;
    @Mock
    private JButton btnAddCategoryExpense;

    private MovementController controller;
    private Account testAccount;
    private MovementCategory incomeCategory;

    @BeforeEach
    void setUp() {
        dataHandlerStub = new TestDataHandlerStub();
        accountSubject = new AccountManagerSubject();
        accountManager = new AccountManager(accountSubject, dataHandlerStub);

        movementSubject = new MovementManagerSubject();
        movementManagerStub = new TestMovementManagerStub(movementSubject, dataHandlerStub);

        lenient().when(view.getBtnAddIncome()).thenReturn(btnAddIncome);
        lenient().when(view.getBtnAddExpense()).thenReturn(btnAddExpense);
        lenient().when(view.getBtnAddCategoryIncome()).thenReturn(btnAddCategoryIncome);
        lenient().when(view.getBtnAddCategoryExpense()).thenReturn(btnAddCategoryExpense);

        testAccount = new Account(1, "Test Account", AccountType.CASH, Coin.USD, new BigDecimal("100.00"));
        accountManager.getAccounts().add(testAccount);
        incomeCategory = new MovementCategory("Salary", MovementType.INCOME);

        controller = new MovementController(movementManagerStub, accountManager, view, testAccount);
    }

    @Test
    void testAddMovement_Orchestration() {
        BigDecimal movementAmount = new BigDecimal("500.00");
        BigDecimal expectedBalance = new BigDecimal("600.00");

        dataHandlerStub.saveCallCount = 0;

        controller.addMovement("Test Income", movementAmount, incomeCategory, testAccount,
                java.time.LocalDateTime.now());

        assertEquals(expectedBalance, testAccount.getCurrentBalance(), "El balance debe ser 600.00");
        assertEquals(1, testAccount.getMovements().size(), "La cuenta debe tener 1 movimiento asociado");

        assertEquals(1, movementManagerStub.getAddMovementCallCount(),
                "El controlador debe llamar a addMovement en el manager");
        assertEquals(1, dataHandlerStub.getSaveCallCount(), "Debe haber 1 llamada a saveAccountsData");
    }
}