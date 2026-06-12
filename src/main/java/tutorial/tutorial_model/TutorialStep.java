package tutorial.tutorial_model;

import javafx.geometry.Pos;
import javafx.scene.Node;

/**
 * Represents a single step in the first-run tutorial overlay.
 */
public class TutorialStep {

    private final String title;
    private final String text;
    private final Node targetNode;
    private final Pos position; // Position relative to the target node

    /**
     * @param title      The title of the tutorial step.
     * @param text       The description explaining the feature.
     * @param targetNode The UI element to point to (optional, can be null for center-screen).
     * @param position   The position of the popup relative to the target node.
     */
    public TutorialStep(String title, String text, Node targetNode, Pos position) {
        this.title = title;
        this.text = text;
        this.targetNode = targetNode;
        this.position = position;
    }

    public String getTitle() { return title; }
    public String getText() { return text; }
    public Node getTargetNode() { return targetNode; }
    public Pos getPosition() { return position; }
}
