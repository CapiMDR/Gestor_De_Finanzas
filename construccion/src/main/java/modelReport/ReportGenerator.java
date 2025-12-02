/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author villa
 */
public class ReportGenerator {

    private ReportSubject reportSubject;
    private JSONControllerPersistence persistence;

    /**
     * Creates a new ReportGenerator.
     *
     * @param subject     the subject used to notify observers
     * @param persistence the persistence controller used to load movement data
     */
    public ReportGenerator(ReportSubject subject, JSONControllerPersistence persistence) {
        this.reportSubject = subject;
        this.persistence = persistence;
    }

    /**
     * Generates a report for today's movements and notifies observers.
     */
    public void today() {
        LocalDate today = LocalDate.now();
        List<Movement> movements = persistence.loadAllMovements().stream()
                .filter(m -> m.getDate().toLocalDate().isEqual(today))
                .collect(Collectors.toList());

        double total = amountTotal(movements);

        ReportData data = new ReportData(
                "Today",
                movements,
                total,
                today.toString()
        );

        reportSubject.notifyObservers(data);
    }

    /**
     * Generates a report covering the last 7 days and notifies observers.
     */
    public void weekAgo() {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        List<Movement> movements = persistence.loadAllMovements().stream()
                .filter(m -> {
                    LocalDate date = m.getDate().toLocalDate();
                    return (!date.isBefore(start) && !date.isAfter(end));
                })
                .collect(Collectors.toList());

        double total = amountTotal(movements);

        ReportData data = new ReportData(
                "Week Ago",
                movements,
                total,
                start.toString() + "-" + end.toString()
        );

        reportSubject.notifyObservers(data);
    }

    /**
     * Generates a report for an arbitrary date range and notifies observers.
     *
     * @param start start date of the period (inclusive)
     * @param end   end date of the period (inclusive)
     */
    public void selectPeriod(LocalDate start, LocalDate end) {
        List<Movement> movements = persistence.loadAllMovements().stream()
                .filter(m -> {
                    LocalDate date = m.getDate().toLocalDate();
                    return (!date.isBefore(start) && !date.isAfter(end));
                })
                .collect(Collectors.toList());

        double total = amountTotal(movements);

        ReportData data = new ReportData(
                "Selected Period",
                movements,
                total,
                start.toString() + "-" + end.toString()
        );

        reportSubject.notifyObservers(data);
    }

    /**
     * Calculates the sum of all movement amounts.
     *
     * @param movements list of movements
     * @return total amount
     */
    private double amountTotal(List<Movement> movements) {
        double total = 0;
        for (Movement m : movements) {
            total += m.getAmount();
        }
        return total;
    }

    /**
     * Adds a new observer to the report subject.
     *
     * @param obs the observer to add
     */
    public void addObserver(ReportObserver obs) {
        reportSubject.add(obs);
    }
}