/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelReport;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author villa
 */
public class JSONControllerPersistence {

    public List<Movement> loadAllMovements() {
        return Arrays.asList(
            new Movement(
                1,
                LocalDateTime.parse("2025-11-17T18:28:03.814611300"),
                "INCOME",
                250.00,
                "Pago de la quincena",
                "Salario"
            ),
            new Movement(
                2,
                LocalDateTime.parse("2025-11-16T18:28:03.815615500"),
                "EXPENSE",
                150.00,
                "Cena en restaurante",
                "Comida"
            ),
            new Movement(
                3,
                LocalDateTime.parse("2025-11-17T17:10:00"),
                "EXPENSE",
                300.00,
                "Supermercado",
                "Despensa"
            ),
            new Movement(
                4,
                LocalDateTime.parse("2025-11-17T08:30:00"),
                "INCOME",
                1200.00,
                "Proyecto freelance",
                "Trabajo"
            ),
            new Movement(
                5,
                LocalDateTime.parse("2025-11-17T12:00:00"),
                "EXPENSE",
                500.00,
                "Pago de renta",
                "Renta"
            )
        );
    }
}
