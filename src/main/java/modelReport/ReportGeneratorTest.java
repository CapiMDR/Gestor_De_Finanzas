/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelReport;

/**
 *
 * @author villa
 */
/*
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;*/

/**
 * Unit tests for ReportGenerator.
 */
//public class ReportGeneratorTest {

    /**
     * Dummy persistence class used to mock movement loading.
     *//*
    private static class FakePersistence extends JSONControllerPersistence {
        private List<Movement> movements;

        public FakePersistence(List<Movement> movements) {
            this.movements = movements;
        }

        @Override
        public List<Movement> loadAllMovements() {
            return movements;
        }
    }*/

    /**
     * Dummy observer subject used to capture sent report data.
     *//*
    private static class FakeSubject extends ReportSubject {

        public ReportData lastData;

        @Override
        public void notifyObservers(ReportData data) {
            this.lastData = data;
        }

        @Override
        public void add(ReportObserver o) {
            // Not needed for test
        }
    }
*/
    /**
     * Helper method to create movement.
     *//*
    private Movement movement(double amount, LocalDate date) {
        return new Movement(amount, LocalDateTime.of(date, java.time.LocalTime.NOON));
    }*/
/*
    @Test
    void testTodayReport() {
        LocalDate today = LocalDate.now();

        FakePersistence persistence = new FakePersistence(List.of(
                movement(10, today),
                movement(5, today.minusDays(1))
        ));

        FakeSubject subject = new FakeSubject();
        ReportGenerator generator = new ReportGenerator(subject, persistence);

        generator.today();

        assertEquals("Today", subject.lastData.getName());
        assertEquals(1, subject.lastData.getMovements().size());
        assertEquals(10, subject.lastData.getTotal());
    }

    @Test
    void testWeekAgoReport() {
        LocalDate today = LocalDate.now();

        FakePersistence persistence = new FakePersistence(List.of(
                movement(10, today),
                movement(20, today.minusDays(3)),
                movement(5, today.minusDays(10)) // outside range
        ));

        FakeSubject subject = new FakeSubject();
        ReportGenerator generator = new ReportGenerator(subject, persistence);

        generator.weekAgo();

        assertEquals("Week Ago", subject.lastData.getName());
        assertEquals(2, subject.lastData.getMovements().size());
        assertEquals(30, subject.lastData.getTotal());
    }

    @Test
    void testSelectPeriod() {
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now().minusDays(2);

        FakePersistence persistence = new FakePersistence(List.of(
                movement(10, start),
                movement(20, start.plusDays(1)),
                movement(30, end),
                movement(5, end.plusDays(1)) // outside range
        ));

        FakeSubject subject = new FakeSubject();
        ReportGenerator generator = new ReportGenerator(subject, persistence);

        generator.selectPeriod(start, end);

        assertEquals("Selected Period", subject.lastData.getName());
        assertEquals(3, subject.lastData.getMovements().size());
        assertEquals(60, subject.lastData.getTotal());
    }

    @Test
    void testAmountTotal() throws Exception {
        FakePersistence persistence = new FakePersistence(List.of());
        FakeSubject subject = new FakeSubject();

        ReportGenerator generator = new ReportGenerator(subject, persistence);

        var movements = List.of(
                movement(10, LocalDate.now()),
                movement(25.5, LocalDate.now())
        );

        // Access private method via reflection
        var method = ReportGenerator.class.getDeclaredMethod("amountTotal", List.class);
        method.setAccessible(true);

        double total = (double) method.invoke(generator, movements);

        assertEquals(35.5, total);
    }
}
*/