package config;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for the Settings dialog ({@code settings_panel.fxml}).
 * Manages UI options for notification modes and Windows autostart.
 */
public class SettingsPanelController {

    private static final Logger logger = LoggerFactory.getLogger(SettingsPanelController.class);

    @FXML private RadioButton rbSoloPrimerPlano;
    @FXML private RadioButton rbSegundoPlano;
    @FXML private CheckBox chkAutostart;
    
    private final ToggleGroup modoGroup = new ToggleGroup();

    @FXML
    public void initialize() {
        rbSoloPrimerPlano.setToggleGroup(modoGroup);
        rbSegundoPlano.setToggleGroup(modoGroup);

        // Load current settings
        AppSettings settings = AppSettings.getInstance();
        if (settings.getModoNotificaciones() == AppSettings.ModoNotificaciones.SEGUNDO_PLANO) {
            rbSegundoPlano.setSelected(true);
        } else {
            rbSoloPrimerPlano.setSelected(true);
        }
        
        chkAutostart.setSelected(settings.isAutostart());

        // Setup the APPLY button handler (needs to be hooked up when the dialog is shown,
        // but we can setup the lookup mechanism by executing a runLater)
        Platform.runLater(() -> {
            DialogPane pane = (DialogPane) rbSoloPrimerPlano.getScene().getRoot();
            javafx.scene.control.Button applyBtn = (javafx.scene.control.Button) pane.lookupButton(ButtonType.APPLY);
            if (applyBtn != null) {
                applyBtn.addEventFilter(javafx.event.ActionEvent.ACTION, event -> guardarConfiguracion());
            }
        });
    }

    private void guardarConfiguracion() {
        AppSettings settings = AppSettings.getInstance();
        
        AppSettings.ModoNotificaciones nuevoModo = rbSegundoPlano.isSelected() 
                ? AppSettings.ModoNotificaciones.SEGUNDO_PLANO 
                : AppSettings.ModoNotificaciones.SOLO_PRIMER_PLANO;
                
        boolean modoCambiado = settings.getModoNotificaciones() != nuevoModo;
        boolean autostartCambiado = settings.isAutostart() != chkAutostart.isSelected();

        settings.setModoNotificaciones(nuevoModo);
        settings.setAutostart(chkAutostart.isSelected());

        // Apply autostart logic if it changed
        if (autostartCambiado) {
            if (chkAutostart.isSelected()) {
                WinRegistryHelper.registerAutostart();
            } else {
                WinRegistryHelper.unregisterAutostart();
            }
        }
        
        // Notify the user if they switched to background mode that it will take effect next launch
        // or apply it immediately to the SystemTray.
        if (modoCambiado && nuevoModo == AppSettings.ModoNotificaciones.SEGUNDO_PLANO) {
            notifications.SystemTrayManager.getInstance().enableTray();
        } else if (modoCambiado) {
            notifications.SystemTrayManager.getInstance().disableTray();
        }

        logger.info("Configuración guardada por el usuario.");
    }

    @FXML
    private void repetirTutorial(javafx.event.ActionEvent event) {
        AppSettings.getInstance().setTutorialMostrado(false);
        // Cerrar el diálogo actual
        javafx.scene.Node source = (javafx.scene.Node) event.getSource();
        javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();
        stage.close();
        
        // Lanzar el tutorial de nuevo
        Platform.runLater(() -> {
            com.mycompany.construccion.MainShell mainShell = com.mycompany.construccion.MainShell.getInstance();
            if (mainShell != null) {
                mainShell.startTutorial();
            }
        });
    }
}
