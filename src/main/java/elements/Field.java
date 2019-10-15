package elements;

import canvas.CanvasNode;
import electrostatics.ElectricFieldLine;
import javafx.scene.shape.Polyline;
import math.Vector2D;

import java.util.ArrayList;

public class Field extends Polyline implements CanvasNode {
    private Vector2D newoffset;
    private ElectricFieldLine efl;
    ArrayList<Double> points;

    public Field(ElectricFieldLine efl) {
        this.efl = efl;
        this.newoffset = new Vector2D();
        this.points = new ArrayList<>();
    }

    public void draw() {
        synchronized (super.getPoints()) {
            super.getPoints().removeAll(points);
            System.out.println("drawing LINE");
            points = new ArrayList<>();
            for (Vector2D pt : efl.getPoints()) {
                Vector2D pos = pt.sub(newoffset);
                points.add(pos.getX().doubleValue());
                points.add(pos.getY().doubleValue());
                //System.out.printf("%f, y: %f\n", pt.getX(), pt.getY());
            }
            super.getPoints().addAll(points);
        }
    }

    @Override
    public void reposition(Vector2D prevOffset, Vector2D offset) {
        newoffset = prevOffset.sub(offset);
        draw();
    }
}
