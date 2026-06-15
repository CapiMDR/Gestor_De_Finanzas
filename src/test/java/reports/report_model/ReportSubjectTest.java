package reports.report_model;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ReportSubject}.
 * Verifies that the Observer pattern is correctly implemented for report events.
 */
class ReportSubjectTest {

    /**
     * Tests the core mechanisms of the subject: adding, notifying,
     * and removing observers to ensure proper subscription lifecycles.
     */
    @Test
    void testAddRemoveAndNotify() {
        ReportSubject subject = new ReportSubject();
        ReportData testData = new ReportData("test", new java.util.ArrayList<>(), java.math.BigDecimal.ZERO, "desc");
        
        AtomicBoolean notified1 = new AtomicBoolean(false);
        ReportObserver obs1 = data -> {
            if (data == testData) {
                notified1.set(true);
            }
        };

        AtomicBoolean notified2 = new AtomicBoolean(false);
        ReportObserver obs2 = data -> {
            if (data == testData) {
                notified2.set(true);
            }
        };

        subject.add(obs1);
        subject.add(obs2);

        subject.notifyObservers(testData);
        assertTrue(notified1.get());
        assertTrue(notified2.get());

        // Reset and test removal
        notified1.set(false);
        notified2.set(false);

        subject.remove(obs1);
        subject.notifyObservers(testData);
        assertFalse(notified1.get(), "Observer 1 should not be notified after removal");
        assertTrue(notified2.get(), "Observer 2 should still be notified");
    }
}
