/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelReport;

import java.util.List;

/**
 *
 * @author villa
 */
public class ReportData {
    private String periodName;
    private List<Movement> movements;
    private double totalAmount;
    private String dateRange;

    public ReportData(String periodName, List<Movement> movements, double totalAmount, String dateRange) {
        this.periodName = periodName;
        this.movements = movements;
        this.totalAmount = totalAmount;
        this.dateRange = dateRange;
    }

    public String getPeriodName() { return periodName; }
    public List<Movement> getMovements() { return movements; }
    public double getTotalAmount() { return totalAmount; }
    public String getDateRange() { return dateRange; }
}
