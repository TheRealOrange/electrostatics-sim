package canvas;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import math.Vector2D;

public class InfiniteCanvas extends AnchorPane {
    private Vector2D offset = new Vector2D(0, 0);
    private Vector2D startPoint = new Vector2D(0, 0);
    private Vector2D oldOffset = offset;
    private long previousChange = 0;

    public InfiniteCanvas(int delay) {
        this.setOnMousePressed(event -> startPoint = new Vector2D(event.getSceneX(), event.getSceneY()));
        this.setOnMouseDragged(event -> {
            offset = offset.add(new Vector2D(event.getSceneX(), event.getSceneY()).sub(startPoint));
            startPoint = new Vector2D(event.getSceneX(), event.getSceneY());
            if (System.currentTimeMillis() - previousChange < delay) return;
            for (Node child : this.getChildren()) {
                if (child instanceof CanvasNode) {
                    ((CanvasNode) child).reposition(oldOffset, offset);
                }
            }
            oldOffset = offset;
            previousChange = System.currentTimeMillis();
        });
    }

    public InfiniteCanvas() {
        this(300);
    }
}
