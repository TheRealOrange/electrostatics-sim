package elements;

import canvas.CanvasNode;
import electrostatics.ElectricFieldLine;
import javafx.scene.shape.Polyline;
import math.Vector2D;

import java.util.ArrayList;

public class Field extends Polyline implements CanvasNode {
    private Vector2D newoffset = new Vector2D(0, 0);
    private ElectricFieldLine efl;
    public Field(ElectricFieldLine efl) {
        this.efl = efl;
    }

    public void draw() {
        synchronized (super.getPoints()) {
            super.getPoints().removeAll();
            System.out.println("drawing");
            ArrayList<Double> points = new ArrayList<>();
            for (Vector2D pt : efl.getPoints()) {
                Vector2D pos = pt.sub(newoffset);
                points.add(pos.getX());
                points.add(pos.getY());
                //System.out.printf("%f, y: %f\n", pt.getX(), pt.getY());
            }
            super.getPoints().addAll(points);
        }
    }

    @Override
    public void reposition(Vector2D prevOffset, Vector2D offset) {
        newoffset = prevOffset.add(offset);
        draw();
    }
}
