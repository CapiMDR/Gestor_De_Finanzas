package com.mycompany.construccion;

import accounts.account_model.Account;
import accounts.account_view.AccountShell;
import accounts.account_view.AccountsModule;
import config.AppSettings;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import notifications.notification_model.AppNotification;
import notifications.notification_controller.NotificationManager;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the main application shell ({@code main_shell.fxml}).
 *
 * Responsibilities:
 * - Manages the primary {@link TabPane} containing the Accounts tab and
 *   per-account tabs.
 * - Owns the notification bell and its badge counter.
 * - Opens/closes the notification panel overlay.
 * - Opens the Settings and Info dialogs.
 * - Provides the static {@link #openAccountTab(Account)} factory method used
 *   by {@link accounts.account_controller.AccountController}.
 */
public class MainShell {

    private static final Logger logger = LoggerFactory.getLogger(MainShell.class);

    // ── Static reference to the single shell instance ────────────────────────
    private static MainShell instance;

    /** Returns the application-wide shell instance (set during FXML initialization). */
    public static MainShell getInstance() { return instance; }

    // ── FXML fields ──────────────────────────────────────────────────────────
    @FXML private TabPane mainTabPane;
    @FXML private Label   lblBadge;
    @FXML private VBox    notificationPanel;
    @FXML private ListView<AppNotification> notificationList;

    // ── State ────────────────────────────────────────────────────────────────

    /** Maps account names to their open tab, preventing duplicates. */
    private final Map<String, Tab> accountTabs = new HashMap<>();

    // ── JavaFX lifecycle ─────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        setInstance(this);

        // Wire the "Mis Cuentas" tab (always first, not closeable)
        AccountsModule.embedInTab(mainTabPane);

        // Connect notification manager callback → update badge on the FX thread
        NotificationManager.getInstance().setCallbackNuevaNotificacion(
                () -> Platform.runLater(this::updateBadge)
        );

        // Load any unread notifications from the previous session
        NotificationManager.getInstance().cargarPendientes();
        updateBadge();

        // Configure notification list cell factory
        notificationList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AppNotification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                // Build notification card
                FontIcon icon = new FontIcon(item.getIconoLiteral());
                icon.setIconSize(22);

                // Type and Title
                String typeStr = item.getTipo().name().replace("_", " ");
                Label tipoLabel = new Label(typeStr);
                tipoLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666666; -fx-font-weight: bold;");

                Label titulo = new Label(item.getTitulo());
                titulo.getStyleClass().add("notification-titulo");
                javafx.scene.layout.HBox tituloRow = new javafx.scene.layout.HBox(5, tipoLabel, titulo);
                tituloRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                Label cuerpo = new Label(item.getCuerpo());
                cuerpo.getStyleClass().add("notification-cuerpo");
                cuerpo.setWrapText(true);

                // Time and Read Status
                Label tiempo = new Label(item.getTimestampFormateado());
                tiempo.getStyleClass().add("notification-tiempo");
                javafx.scene.layout.HBox tiempoRow = new javafx.scene.layout.HBox(10, tiempo);
                tiempoRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                
                if (item.isLeida()) {
                    Label leida = new Label("• Leída");
                    leida.setStyle("-fx-text-fill: #A0AEC0; -fx-font-size: 10px;");
                    tiempoRow.getChildren().add(leida);
                }

                javafx.scene.layout.VBox contenido = new javafx.scene.layout.VBox(2, tituloRow, cuerpo, tiempoRow);
                contenido.setPrefWidth(220); // allow wrapping

                // Delete button
                FontIcon trashIcon = new FontIcon("mdi2d-delete");
                trashIcon.setIconSize(18);
                javafx.scene.control.Button btnDelete = new javafx.scene.control.Button("", trashIcon);
                btnDelete.getStyleClass().addAll("btn-text-small", "btn-text-danger", "notification-item-btn-delete");
                btnDelete.setStyle("-fx-background-color: transparent; -fx-padding: 2;");
                btnDelete.setOnAction(e -> {
                    NotificationManager.getInstance().eliminarNotificacion(item);
                    notificationList.setItems(
                        FXCollections.observableArrayList(NotificationManager.getInstance().getPendientes())
                    );
                    updateBadge();
                });

                javafx.scene.layout.HBox tarjeta = new javafx.scene.layout.HBox(10, icon, contenido, btnDelete);
                tarjeta.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                tarjeta.getStyleClass().add(item.isLeida() ? "notification-card-leida" : "notification-card");

                setGraphic(tarjeta);
            }
        });

        // Setup and start tutorial
        Platform.runLater(this::startTutorial);
    }

    private void startTutorial() {
        if (config.AppSettings.getInstance().isTutorialMostrado()) {
            return;
        }
        
        // Define steps
        java.util.List<tutorial.tutorial_model.TutorialStep> steps = java.util.Arrays.asList(
            new tutorial.tutorial_model.TutorialStep(
                "¡Bienvenido!",
                "Esta es la nueva interfaz. Aquí podrás gestionar todas tus finanzas en un solo lugar de forma sencilla.",
                null, // center
                javafx.geometry.Pos.CENTER
            ),
            new tutorial.tutorial_model.TutorialStep(
                "Tus Cuentas",
                "Puedes ver y administrar tus cuentas en esta pestaña principal. Al abrir una cuenta, se creará una nueva pestaña.",
                mainTabPane,
                javafx.geometry.Pos.BOTTOM_CENTER
            ),
            new tutorial.tutorial_model.TutorialStep(
                "Notificaciones",
                "Aquí recibirás recordatorios y alertas cuando cumplas tus metas o venzan pagos recurrentes.",
                lblBadge.getParent(), // The stack pane holding the bell
                javafx.geometry.Pos.BOTTOM_CENTER
            ),
            new tutorial.tutorial_model.TutorialStep(
                "Configuración y Segundo Plano",
                "En esta sección puedes ajustar el modo en segundo plano para seguir recibiendo notificaciones aunque cierres la ventana.",
                ((javafx.scene.layout.Pane)lblBadge.getParent().getParent()).getChildren().get(2), // The settings icon (3rd child of the HBox)
                javafx.geometry.Pos.BOTTOM_CENTER
            )
        );

        tutorial.tutorial_controller.TutorialManager tutorialManager = new tutorial.tutorial_controller.TutorialManager(steps);
        // Ensure the scene and window are ready
        if (mainTabPane.getScene() != null && mainTabPane.getScene().getWindow() != null) {
            tutorialManager.startIfFirstRun(mainTabPane.getScene().getWindow());
        }
    }

    // ── Static factory ────────────────────────────────────────────────────────

    /**
     * Opens a tab for the given account, or brings the existing tab to the front
     * if one is already open. Called by {@link accounts.account_controller.AccountController}.
     *
     * @param cuenta the account to open
     */
    public static void openAccountTab(Account cuenta) {
        if (instance == null) return;
        Platform.runLater(() -> instance.openOrFocusTab(cuenta));
    }

    // ── Tab management ────────────────────────────────────────────────────────

    private void openOrFocusTab(Account cuenta) {
        // If a tab for this account already exists, focus it
        Tab existente = accountTabs.get(cuenta.getName());
        if (existente != null) {
            mainTabPane.getSelectionModel().select(existente);
            return;
        }

        // Create a new tab with AccountShell content
        Tab tab = new Tab(cuenta.getName());
        tab.setClosable(true);
        tab.getStyleClass().add("account-tab");

        AccountShell shell = new AccountShell(cuenta);
        tab.setContent(shell.getRoot());

        // Unregister when the tab is closed
        tab.setOnClosed(e -> {
            accountTabs.remove(cuenta.getName());
            shell.dispose();
            logger.info("Pestaña cerrada: {}", cuenta.getName());
        });

        accountTabs.put(cuenta.getName(), tab);
        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().select(tab);
        logger.info("Pestaña abierta: {}", cuenta.getName());
    }

    // ── Notification panel ────────────────────────────────────────────────────

    @FXML
    private void toggleNotifications() {
        boolean visible = notificationPanel.isVisible();
        if (!visible) {
            // Populate the list
            notificationList.setItems(
                    FXCollections.observableArrayList(NotificationManager.getInstance().getPendientes())
            );
        }
        notificationPanel.setVisible(!visible);
        notificationPanel.setManaged(!visible);
    }

    @FXML
    private void markAllAsRead() {
        NotificationManager.getInstance().marcarTodasLeidas();
        notificationList.refresh();
        updateBadge();
    }

    @FXML
    private void deleteAllNotifications() {
        NotificationManager.getInstance().eliminarTodas();
        notificationList.setItems(
            FXCollections.observableArrayList(NotificationManager.getInstance().getPendientes())
        );
        updateBadge();
    }

    private void updateBadge() {
        int count = NotificationManager.getInstance().getConteoNoLeidas();
        if (count > 0) {
            lblBadge.setText(count > 9 ? "9+" : String.valueOf(count));
            lblBadge.setVisible(true);
        } else {
            lblBadge.setVisible(false);
        }
    }

    // ── Top-bar actions ───────────────────────────────────────────────────────

    @FXML
    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/settings/settings_panel.fxml"));
            DialogPane pane = loader.load();
            pane.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Configuración");
            dialog.showAndWait();
        } catch (IOException e) {
            logger.error("Error al abrir configuración.", e);
        }
    }

    @FXML
    private void openInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/info/info_panel.fxml"));
            DialogPane pane = loader.load();
            
            javafx.scene.control.Label lblVersion = (javafx.scene.control.Label) pane.lookup("#lblVersion");
            if (lblVersion != null) {
                lblVersion.setText("Versión " + utils.UIUtils.getAppVersion());
            }

            pane.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.setTitle("Acerca de Gestor de Finanzas");
            dialog.showAndWait();
        } catch (IOException e) {
            logger.error("Error al abrir panel de información.", e);
        }
    }

    // ── Shutdown hook ─────────────────────────────────────────────────────────

    /**
     * Called by {@link Main} when the application is about to close.
     * Saves unread notifications to disk so they survive in foreground-only mode.
     */
    public void onClose() {
        if (AppSettings.getInstance().getModoNotificaciones()
                == AppSettings.ModoNotificaciones.SOLO_PRIMER_PLANO) {
            NotificationManager.getInstance().guardarPendientes();
        }
    }

    private static void setInstance(MainShell shell) {
        instance = shell;
    }
}
