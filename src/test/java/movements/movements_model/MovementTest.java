package movements.movements_model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import accounts.account_model.Account;
import accounts.account_model.Account.AccountType;
import accounts.account_model.Account.Coin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

class MovementTest {

    @Test
    void testMovementCreationDataIntegrity() {
        Account dummyAccount = new Account(1, "TestAcc", AccountType.CASH, Coin.MXN, BigDecimal.TEN);
        MovementCategory dummyCategory = new MovementCategory("Gym", MovementType.EXPENSE);
        
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = new BigDecimal("500.00");
        String description = "Pago mensualidad";

        Movement movement = new Movement(id, description, amount, dummyCategory, dummyAccount, now);

        assertEquals(id, movement.getIdMovement());
        assertEquals(description, movement.getDescription());
        assertEquals(amount, movement.getAmount());
        assertEquals(dummyCategory, movement.getCategory());
        assertEquals(dummyAccount, movement.getAccount());
        assertEquals(now, movement.getDate());
    }

    @Test
    void testMovementLogicWithAccount() {
        
        Account account = new Account(1, "Wallet", AccountType.DIGITAL, Coin.MXN, new BigDecimal("1000"));
        MovementCategory incomeCat = new MovementCategory("Bonus", MovementType.INCOME);
        Movement income = new Movement(UUID.randomUUID(), "Bono", new BigDecimal("200"), incomeCat, account, LocalDateTime.now());

        account.addMovement(income);

        assertEquals(new BigDecimal("1200"), account.getCurrentBalance(), 
            "El movimiento debe ser compatible con la lógica de saldo de la cuenta");
        assertEquals(1, account.getMovements().size(), 
            "La cuenta debe tener el movimiento registrado en su lista");
    }
}