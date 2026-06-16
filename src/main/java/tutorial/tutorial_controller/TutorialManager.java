package tutorial.tutorial_controller;

import java.util.List;
import tutorial.tutorial_model.TutorialStep;

import config.AppSettings;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

/**
 * Manages the overlay tutorial for first-time users.
 * Displays a sequence of tooltips over specific UI elements,
 * with a glassmorphism dim background and a spotlight cutout.
 */
public class TutorialManager {

    private final List<TutorialStep> steps;
    private int currentStepIndex = 0;
    
    private Popup popup;
    private Pane overlayPane;
    private Path overlayPath;
    private VBox tooltipBox;
    
    private Window ownerWindow;
    private ChangeListener<Number> boundsListener;

    public TutorialManager(List<TutorialStep> steps) {
        this.steps = steps;
    }
    
    public void startIfFirstRun(Window ownerWindow) {
        if (AppSettings.getInstance().isTutorialMostrado()) {
            return;
        }

        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> start(ownerWindow));
        pause.play();
    }

    public void start(Window ownerWindow) {
        if (steps.isEmpty() || ownerWindow == null) return;
        this.ownerWindow = ownerWindow;
        currentStepIndex = 0;
        showStep();
    }

    private void showStep() {
        boolean isFirst = (popup == null);
        if (isFirst) {
            initPopup();
        }

        TutorialStep step = steps.get(currentStepIndex);
        
        // Rebuild tooltip content
        if (tooltipBox != null) {
            overlayPane.getChildren().remove(tooltipBox);
        }
        tooltipBox = buildPopupContent(step);
        overlayPane.getChildren().add(tooltipBox);

        updateOverlayBounds();

        if (isFirst) {
            popup.show(ownerWindow);
            
            FadeTransition ft = new FadeTransition(Duration.millis(500), overlayPane);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
            
            // Wait for show to apply CSS and get dimensions
            javafx.application.Platform.runLater(this::updateTooltipPositionInstant);
        } else {
            // Animate Spotlight and Tooltip Position
            updateSpotlight();
            updateTooltipPositionAnimated();
        }
    }

    private void initPopup() {
        popup = new Popup();
        popup.setAutoHide(false);
        popup.setHideOnEscape(false);
        
        overlayPane = new Pane();
        overlayPane.setStyle("-fx-background-color: transparent;");
        
        overlayPath = new Path();
        overlayPath.setFillRule(FillRule.EVEN_ODD);
        overlayPath.setFill(Color.rgb(0, 0, 0, 0.6)); // Dim background
        overlayPath.setStroke(null);
        
        boundsListener = (obs, oldV, newV) -> updateOverlayBounds();
        ownerWindow.xProperty().addListener(boundsListener);
        ownerWindow.yProperty().addListener(boundsListener);
        ownerWindow.widthProperty().addListener(boundsListener);
        ownerWindow.heightProperty().addListener(boundsListener);
        
        overlayPane.getChildren().add(overlayPath);
        popup.getContent().add(overlayPane);
    }

    private void updateOverlayBounds() {
        if (popup != null && ownerWindow != null) {
            // Cover the whole window
            popup.setX(ownerWindow.getX());
            popup.setY(ownerWindow.getY());
            overlayPane.setPrefWidth(ownerWindow.getWidth());
            overlayPane.setPrefHeight(ownerWindow.getHeight());
            updateSpotlight();
        }
    }

    private void updateSpotlight() {
        overlayPath.getElements().clear();
        
        double w = overlayPane.getPrefWidth();
        double h = overlayPane.getPrefHeight();
        
        // Background Full Size
        overlayPath.getElements().add(new MoveTo(0, 0));
        overlayPath.getElements().add(new LineTo(w, 0));
        overlayPath.getElements().add(new LineTo(w, h));
        overlayPath.getElements().add(new LineTo(0, h));
        overlayPath.getElements().add(new ClosePath());
        
        // Cutout Spotlight Hole
        TutorialStep step = steps.get(currentStepIndex);
        Node target = step.getTargetNode();
        if (target != null && target.getScene() != null) {
            Bounds screenBounds = target.localToScreen(target.getBoundsInLocal());
            if (screenBounds != null) {
                double padding = 8;
                double tx = screenBounds.getMinX() - popup.getX() - padding;
                double ty = screenBounds.getMinY() - popup.getY() - padding;
                double tw = screenBounds.getWidth() + padding * 2;
                double th = screenBounds.getHeight() + padding * 2;
                
                // Draw rectangle hole
                overlayPath.getElements().add(new MoveTo(tx, ty));
                overlayPath.getElements().add(new LineTo(tx + tw, ty));
                overlayPath.getElements().add(new LineTo(tx + tw, ty + th));
                overlayPath.getElements().add(new LineTo(tx, ty + th));
                overlayPath.getElements().add(new ClosePath());
            }
        }
    }

    private void updateTooltipPositionInstant() {
        double[] pos = calculateTooltipPosition();
        tooltipBox.setTranslateX(pos[0]);
        tooltipBox.setTranslateY(pos[1]);
        
        // Pequeña animación de entrada (Scale/Fade) para que no sea estático
        tooltipBox.setScaleX(0.9);
        tooltipBox.setScaleY(0.9);
        tooltipBox.setOpacity(0);
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(300), 
                new KeyValue(tooltipBox.opacityProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT),
                new KeyValue(tooltipBox.scaleXProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT),
                new KeyValue(tooltipBox.scaleYProperty(), 1.0, javafx.animation.Interpolator.EASE_OUT)
            )
        );
        timeline.play();
    }

    private void updateTooltipPositionAnimated() {
        double[] pos = calculateTooltipPosition();
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(400), 
                new KeyValue(tooltipBox.translateXProperty(), pos[0], javafx.animation.Interpolator.EASE_BOTH),
                new KeyValue(tooltipBox.translateYProperty(), pos[1], javafx.animation.Interpolator.EASE_BOTH)
            )
        );
        timeline.play();
    }

    private double[] calculateTooltipPosition() {
        TutorialStep step = steps.get(currentStepIndex);
        Node target = step.getTargetNode();
        
        tooltipBox.applyCss();
        tooltipBox.layout();
        
        double popupWidth = tooltipBox.prefWidth(-1);
        double popupHeight = tooltipBox.prefHeight(-1);
        
        double x = 0;
        double y = 0;
        
        if (target != null && target.getScene() != null) {
            Bounds screenBounds = target.localToScreen(target.getBoundsInLocal());
            if (screenBounds != null) {
                double tMinX = screenBounds.getMinX() - popup.getX();
                double tMinY = screenBounds.getMinY() - popup.getY();
                double tMaxX = screenBounds.getMaxX() - popup.getX();
                double tMaxY = screenBounds.getMaxY() - popup.getY();
                double tWidth = screenBounds.getWidth();
                double tHeight = screenBounds.getHeight();
                
                switch (step.getPosition()) {
                    case BOTTOM_CENTER:
                        x = tMinX + (tWidth / 2) - (popupWidth / 2);
                        y = tMaxY + 15;
                        break;
                    case TOP_CENTER:
                        x = tMinX + (tWidth / 2) - (popupWidth / 2);
                        y = tMinY - popupHeight - 15;
                        break;
                    case CENTER_LEFT:
                        x = tMinX - popupWidth - 15;
                        y = tMinY + (tHeight / 2) - (popupHeight / 2);
                        break;
                    case CENTER_RIGHT:
                        x = tMaxX + 15;
                        y = tMinY + (tHeight / 2) - (popupHeight / 2);
                        break;
                    default:
                        x = (overlayPane.getPrefWidth() - popupWidth) / 2;
                        y = (overlayPane.getPrefHeight() - popupHeight) / 2;
                        break;
                }
            }
        } else {
            x = (overlayPane.getPrefWidth() - popupWidth) / 2;
            y = (overlayPane.getPrefHeight() - popupHeight) / 2;
        }
        
        // Evitar que el tooltip se salga de la pantalla
        x = Math.max(10, Math.min(x, overlayPane.getPrefWidth() - popupWidth - 10));
        y = Math.max(10, Math.min(y, overlayPane.getPrefHeight() - popupHeight - 10));
        
        return new double[]{x, y};
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
                showStep();
            } else {
                endTutorial();
            }
        });

        buttonBar.getChildren().addAll(btnSkip, btnNext);
        box.getChildren().addAll(title, text, buttonBar);

        return box;
    }

    public void endTutorial() {
        if (popup != null) {
            FadeTransition ft = new FadeTransition(Duration.millis(300), overlayPane);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> {
                popup.hide();
                ownerWindow.xProperty().removeListener(boundsListener);
                ownerWindow.yProperty().removeListener(boundsListener);
                ownerWindow.widthProperty().removeListener(boundsListener);
                ownerWindow.heightProperty().removeListener(boundsListener);
                popup = null;
            });
            ft.play();
        }
        AppSettings.getInstance().setTutorialMostrado(true);
    }
}
