/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports.modelReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import accounts.account_model.Account;
import movements.movement_model.Movement;

/**
 *
 * @author villa
 */
public class ReportGenerator {

    private ReportSubject reportSubject;
    private Account account;

    /**
     * Creates a new ReportGenerator.
     *
     * @param subject     the subject used to notify observers
     * @param persistence the persistence controller used to load movement data
     */
    public ReportGenerator(ReportSubject subject, Account selectedAccount) {
        this.reportSubject = subject;
        this.account = selectedAccount;
    }

    /**
     * Generates a report for today's movements and notifies observers.
     */
    public void today() {
        LocalDate today = LocalDate.now();
        List<Movement> movements = account.getMovements().stream()
                .filter(m -> m.getDate().toLocalDate().isEqual(today))
                .collect(Collectors.toList());

        BigDecimal total = amountTotal(movements);

        ReportData data = new ReportData(
                "Today",
                movements,
                total,
                today.toString());

        reportSubject.notifyObservers(data);
    }

    /**
     * Generates a report covering the last 7 days and notifies observers.
     */
    public void weekAgo() {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        List<Movement> movements = account.getMovements().stream()
                .filter(m -> {
                    LocalDate date = m.getDate().toLocalDate();
                    return (!date.isBefore(start) && !date.isAfter(end));
                })
                .collect(Collectors.toList());

        BigDecimal total = amountTotal(movements);

        ReportData data = new ReportData(
                "Week Ago",
                movements,
                total,
                start.toString() + "-" + end.toString());

        reportSubject.notifyObservers(data);
    }

  

    /**
     * Calculates the sum of all movement amounts.
     *
     * @param movements list of movements
     * @return total amount
     */
    private BigDecimal amountTotal(List<Movement> movements) {
        BigDecimal total = new BigDecimal(0);
        for (Movement m : movements) {

            total.add(m.getAmount());
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