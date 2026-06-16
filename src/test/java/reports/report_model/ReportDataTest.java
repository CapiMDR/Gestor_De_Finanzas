package reports.report_model;

import movements.movement_model.Movement;
import movements.movement_model.MovementCategory;
import movements.movement_model.MovementCategory.MovementType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ReportDataTest {

    @Test
    void testReportData() {
        String periodName = "Mensual";
        List<Movement> movements = new ArrayList<>();
        MovementCategory cat = new MovementCategory("Food", MovementType.EXPENSE);
        movements.add(new Movement(java.util.UUID.randomUUID(), "Lunch", new BigDecimal("150"), cat, null, LocalDateTime.of(2026, java.time.Month.JUNE, 15, 10, 0)));
        BigDecimal totalAmount = new BigDecimal("150");
        String dateRange = "01-06-2026 to 30-06-2026";

        ReportData data = new ReportData(periodName, movements, totalAmount, dateRange);

        assertEquals(periodName, data.getPeriodName());
        assertEquals(movements, data.getMovements());
        assertEquals(totalAmount, data.getTotalAmount());
        assertEquals(dateRange, data.getDateRange());
    }
}
