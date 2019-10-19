package canvas;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import math.Vector2D;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Movable {
  private static Movable currentObject;
  private static Pane parent;
  private static ArrayList<Movable> objects = new ArrayList<>();
  private final Shape shape;

  private final Supplier<Vector2D> getcoords;
  private final Consumer<Vector2D> setcoords;
  private final Consumer<Movable> eventHandler;

  public static void init(Pane parent, Node[] objects) {
    Movable.parent = parent;
    Movable.parent.setOnMouseMoved(e -> {
      if (currentObject == null) return;
      reposition(e);
    });
    for (Node p : objects) {
      p.setOnMouseMoved(e -> {
        if (currentObject == null) return;
        reposition(e);
      });
    }
  }

  public Movable(Shape shape, Supplier<Vector2D> getcoords, Consumer<Vector2D> setcoords, Consumer<Movable> eventHandler) {
    this.shape = shape;
    this.eventHandler = eventHandler;

    this.getcoords = getcoords;
    this.setcoords = setcoords;

    shape.setOnMouseClicked(e -> this.eventHandler.accept(this));
    objects.add(this);
  }

  public Vector2D getCoords() {
    return this.getcoords.get();
  }

  public void setCoords(Vector2D coords) {
    this.setcoords.accept(coords);
  }

  private static void reposition(MouseEvent evt) {
    Vector2D prevCoords = currentObject.getCoords();
    currentObject.setCoords(new Vector2D(evt.getSceneX(), evt.getSceneY()));
    for (Movable object : objects) {
      if (object == currentObject) continue;
      Shape intersect = Shape.intersect(object.shape, currentObject.shape);
      if (intersect instanceof Path && ((Path) intersect).getElements().size() > 0) {
        // Object intersects with another movable object
        currentObject.setCoords(prevCoords);
      }
    }
  }

  public static Movable getCurrentObject() {
    return currentObject;
  }

  public static void setCurrentObject(Movable currentObject) {
    Movable.currentObject = currentObject;
  }
}
