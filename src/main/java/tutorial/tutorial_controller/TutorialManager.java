package tutorial.tutorial_controller;

import tutorial.tutorial_model.TutorialStep;

import config.AppSettings;
import javafx.animation.FadeTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.List;

/**
 * Manages the overlay tutorial for first-time users.
 * Displays a sequence of tooltips over specific UI elements.
 */
public class TutorialManager {

    private final List<TutorialStep> steps;
    private int currentStepIndex = 0;
    private Popup popup;

    public TutorialManager(List<TutorialStep> steps) {
        this.steps = steps;
    }

    /**
     * Starts the tutorial if it hasn't been shown before.
     * @param ownerWindow The main application window.
     */
    public void startIfFirstRun(Window ownerWindow) {
        if (AppSettings.getInstance().isTutorialMostrado()) {
            return;
        }

        // Slight delay to allow the UI to fully layout before calculating bounds
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> start(ownerWindow));
        pause.play();
    }

    private void start(Window ownerWindow) {
        if (steps.isEmpty() || ownerWindow == null) return;
        currentStepIndex = 0;
        showStep(ownerWindow);
    }

    private void showStep(Window ownerWindow) {
        if (popup != null) {
            popup.hide();
        }

        TutorialStep step = steps.get(currentStepIndex);
        
        popup = new Popup();
        popup.setAutoHide(false);
        popup.setHideOnEscape(false);
        
        VBox content = buildPopupContent(step);
        popup.getContent().add(content);

        // Calculate position
        Node target = step.getTargetNode();
        if (target != null && target.getScene() != null) {
            Bounds boundsInScreen = target.localToScreen(target.getBoundsInLocal());
            
            // Wait for the popup to show to get its size, so we can position it perfectly
            popup.setOnShown(e -> {
                double popupWidth = content.getWidth();
                double popupHeight = content.getHeight();
                
                double x = boundsInScreen.getMinX();
                double y = boundsInScreen.getMinY();
                
                switch (step.getPosition()) {
                    case BOTTOM_CENTER:
                        x = boundsInScreen.getMinX() + (boundsInScreen.getWidth() / 2) - (popupWidth / 2);
                        y = boundsInScreen.getMaxY() + 10;
                        break;
                    case TOP_CENTER:
                        x = boundsInScreen.getMinX() + (boundsInScreen.getWidth() / 2) - (popupWidth / 2);
                        y = boundsInScreen.getMinY() - popupHeight - 10;
                        break;
                    case CENTER_LEFT:
                        x = boundsInScreen.getMinX() - popupWidth - 10;
                        y = boundsInScreen.getMinY() + (boundsInScreen.getHeight() / 2) - (popupHeight / 2);
                        break;
                    case CENTER_RIGHT:
                        x = boundsInScreen.getMaxX() + 10;
                        y = boundsInScreen.getMinY() + (boundsInScreen.getHeight() / 2) - (popupHeight / 2);
                        break;
                    default:
                        x = ownerWindow.getX() + (ownerWindow.getWidth() / 2) - (popupWidth / 2);
                        y = ownerWindow.getY() + (ownerWindow.getHeight() / 2) - (popupHeight / 2);
                        break;
                }
                
                popup.setX(x);
                popup.setY(y);
            });
            
            popup.show(ownerWindow);
        } else {
            // Center in screen
            popup.setOnShown(e -> {
                double popupWidth = content.getWidth();
                double popupHeight = content.getHeight();
                popup.setX(ownerWindow.getX() + (ownerWindow.getWidth() / 2) - (popupWidth / 2));
                popup.setY(ownerWindow.getY() + (ownerWindow.getHeight() / 2) - (popupHeight / 2));
            });
            popup.show(ownerWindow);
        }

        // Fade in animation
        FadeTransition ft = new FadeTransition(Duration.millis(300), content);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private VBox buildPopupContent(TutorialStep step) {
        VBox box = new VBox(10);
        box.getStyleClass().add("tutorial-popup");
        box.setPadding(new Insets(15));
        box.setMaxWidth(300);

        Label title = new Label(step.getTitle());
        title.getStyleClass().add("tutorial-title");

        Label text = new Label(step.getText());
        text.getStyleClass().add("tutorial-text");
        text.setWrapText(true);

        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button btnSkip = new Button("Omitir");
        btnSkip.getStyleClass().add("btn-text-small");
        btnSkip.setOnAction(e -> endTutorial());

        Button btnNext = new Button(currentStepIndex == steps.size() - 1 ? "Finalizar" : "Siguiente");
        btnNext.getStyleClass().add("btn-primary");
        btnNext.setOnAction(e -> {
            if (currentStepIndex < steps.size() - 1) {
                currentStepIndex++;
                showStep(popup.getOwnerWindow());
            } else {
                endTutorial();
            }
        });

        buttonBar.getChildren().addAll(btnSkip, btnNext);
        box.getChildren().addAll(title, text, buttonBar);

        return box;
    }

    private void endTutorial() {
        if (popup != null) {
            popup.hide();
            popup = null;
        }
        AppSettings.getInstance().setTutorialMostrado(true);
    }
}
