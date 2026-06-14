package reports.report_model;

/**
 * Interface for observing report generation events.
 *
 * @author villa
 */
public interface ReportObserver {
    void onNotify(ReportData data);
}