package reports.modelReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import accounts.account_model.Account;
import movements.movement_model.Movement;

/**
 * Generates reports based on the account's movements.
 * Notifies observers when a report is generated.
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
                "Hoy",
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
                "Ultimos 7 días",
                movements,
                total,
                start.toString() + "-" + end.toString());

        reportSubject.notifyObservers(data);
    }
    /**
     * Generates a report for yesterday's movements.
     */
    public void yesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Movement> movements = account.getMovements().stream()
                .filter(m -> m.getDate().toLocalDate().isEqual(yesterday))
                .collect(Collectors.toList());

        BigDecimal total = amountTotal(movements);

        ReportData data = new ReportData(
                "Ayer",
                movements,
                total,
                yesterday.toString());

        reportSubject.notifyObservers(data);
    }

    /**
     * Generates a report covering the current week (from Monday to Sunday).
     */
    public void currentWeek() {
        LocalDate today = LocalDate.now();
        // Go back to the most recent Monday
        LocalDate start = today.with(java.time.DayOfWeek.MONDAY);
        // End is today
        LocalDate end = today;

        List<Movement> movements = account.getMovements().stream()
                .filter(m -> {
                    LocalDate date = m.getDate().toLocalDate();
                    return (!date.isBefore(start) && !date.isAfter(end));
                })
                .collect(Collectors.toList());

        BigDecimal total = amountTotal(movements);

        ReportData data = new ReportData(
                "Semana Actual",
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

            total = total.add(m.getAmount());
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

    /**
     * Removes an observer from the report subject to prevent memory leaks.
     *
     * @param obs the observer to remove
     */
    public void removeObserver(ReportObserver obs) {
        reportSubject.remove(obs);
    }
}