package reports.report_model;

import java.util.List;
import java.math.BigDecimal;
import movements.movement_model.Movement;

/**
 * Holds the data required to display a report.
 * Contains the period name, movements within the period, total amount, and date range.
 *
 * @author villa
 */
public class ReportData {
    private String periodName;
    private List<Movement> movements;
    private BigDecimal totalAmount;
    private String dateRange;

    public ReportData(String periodName, List<Movement> movements, BigDecimal totalAmount, String dateRange) {
        this.periodName = periodName;
        this.movements = movements;
        this.totalAmount = totalAmount;
        this.dateRange = dateRange;
    }

    public String getPeriodName() {
        return periodName;
    }

    public List<Movement> getMovements() {
        return movements;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getDateRange() {
        return dateRange;
    }
}
