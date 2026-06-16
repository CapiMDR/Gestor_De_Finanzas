package tutorial.tutorial_model;

import javafx.geometry.Pos;
import javafx.scene.Node;
import java.util.function.Supplier;

/**
 * Represents a single step in the first-run tutorial overlay.
 */
public class TutorialStep {

    private final String title;
    private final String text;
    private final Supplier<Node> targetNodeSupplier;
    private final Pos position; // Position relative to the target node

    /**
     * @param title      The title of the tutorial step.
     * @param text       The description explaining the feature.
     * @param targetNodeSupplier A supplier for the UI element to point to (optional, can be null or return null for center-screen).
     * @param position   The position of the popup relative to the target node.
     */
    public TutorialStep(String title, String text, Supplier<Node> targetNodeSupplier, Pos position) {
        this.title = title;
        this.text = text;
        this.targetNodeSupplier = targetNodeSupplier;
        this.position = position;
    }

    public String getTitle() { return title; }
    public String getText() { return text; }
    public Node getTargetNode() { return targetNodeSupplier == null ? null : targetNodeSupplier.get(); }
    public Pos getPosition() { return position; }
}
