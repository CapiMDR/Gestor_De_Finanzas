package accounts.account_view;

import accounts.account_model.Account;
import accounts.account_model.AccountManager;
import accounts.account_model.AccountObserver;
import accounts.account_controller.AccountDashboardController;
import goals.goals_view.GoalsModule;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import movements.movement_view.MovementsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recurringMoves.recurring_view.RecurringsModule;
import reminders.reminder_view.RemindersModule;
import filters.viewFilter.FilterModule;

import java.io.IOException;

/**
 * Navigation manager for a single account tab.
 *
 * AccountShell builds a {@link BorderPane} with:
 * - {@code top}:    mini navigation bar (back button + breadcrumb label)
 * - {@code center}: a {@link StackPane} whose single visible child is swapped
 *                   between the dashboard and the active sub-module view.
 *
 * The back button is hidden when the dashboard is shown and visible when a
 * sub-module is active. Clicking it restores the dashboard.
 *
 * This class also implements {@link AccountObserver} so it can update the
 * dashboard balance label whenever the model changes.
 */
public class AccountShell implements AccountObserver {

    private static final Logger logger = LoggerFactory.getLogger(AccountShell.class);

    private final Account cuenta;
    private final BorderPane root;
    private final StackPane contenido;

    // Dashboard node + controller (loaded once, reused)
    private Parent dashboardNode;
    private AccountDashboardController dashboardController;

    public AccountShell(Account cuenta) {
        this.cuenta = cuenta;

        // ── Content area ─────────────────────────────────────────────────────
        contenido = new StackPane();

        // ── Root layout ──────────────────────────────────────────────────────
        root = new BorderPane();
        root.setCenter(contenido);

        // Register as Observer to keep the balance updated
        AccountManager.addObserver(this);

        // Load and show the dashboard
        cargarDashboard();
        mostrarDashboard();
    }

    /** @return the root node to be set as the tab content */
    public Node getRoot() { return root; }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    private void cargarDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/accounts/account_dashboard.fxml"));
            dashboardNode = loader.load();
            dashboardController = loader.getController();

            // Populate account data
            dashboardController.setAccount(cuenta);

            // Wire navigation callbacks
            dashboardController.setOnMovimientos(   () -> mostrarModulo(cargarMovimientos()));
            dashboardController.setOnMetas(         () -> mostrarModulo(cargarMetas()));
            dashboardController.setOnRecurrentes(   () -> mostrarModulo(cargarRecurrentes()));
            dashboardController.setOnRecordatorios( () -> mostrarModulo(cargarRecordatorios()));
            dashboardController.setOnReportes(      () -> mostrarModulo(cargarReportes()));

        } catch (IOException e) {
            logger.error("Error al cargar el dashboard de la cuenta {}.", cuenta.getName(), e);
        }
    }

    private void mostrarDashboard() {
        contenido.getChildren().setAll(dashboardNode);
    }

    // ── Sub-module loaders ────────────────────────────────────────────────────

    /**
     * Shows a sub-module node inside the content area.
     *
     * @param modulo the loaded sub-module root node; may be {@code null} if loading failed
     */
    private void mostrarModulo(Node modulo) {
        if (modulo == null) return;
        contenido.getChildren().setAll(modulo);
    }

    private Node cargarMovimientos() {
        return MovementsModule.loadForAccount(cuenta, this::mostrarDashboard);
    }

    private Node cargarMetas() {
        return GoalsModule.loadForAccount(cuenta, this::mostrarDashboard);
    }

    private Node cargarRecurrentes() {
        return RecurringsModule.loadForAccount(cuenta, this::mostrarDashboard);
    }

    private Node cargarRecordatorios() {
        return RemindersModule.loadForAccount(cuenta, this::mostrarDashboard);
    }

    private Node cargarReportes() {
        return FilterModule.loadForAccount(cuenta, this::mostrarDashboard);
    }

    // ── AccountObserver ───────────────────────────────────────────────────────

    @Override
    public void onNotify(java.util.List<Account> accountsList) {
        // Refresh balance in the dashboard header when the model changes
        Account actualizada = accountsList.stream()
                .filter(a -> a.getName().equals(cuenta.getName()))
                .findFirst()
                .orElse(cuenta);
        if (dashboardController != null) {
            javafx.application.Platform.runLater(() -> dashboardController.refreshBalance(actualizada));
        }
    }

    /**
     * Called when the tab is closed. Unregisters the observer to prevent memory leaks.
     */
    public void dispose() {
        AccountManager.removeObserver(this);
        logger.debug("AccountShell liberado para cuenta: {}", cuenta.getName());
    }
}
