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
        this.newoffset = new Vector2D(0, 0);
        this.points = new ArrayList<>();
    }

    public void draw() {
        synchronized (super.getPoints()) {
            super.getPoints().removeAll(points);
            System.out.println("drawing LINE");
            points = new ArrayList<>();
            for (Vector2D pt : efl.getPoints()) {
                Vector2D pos = pt.sub(newoffset);
                points.add(pos.getX());
                points.add(pos.getY());
                System.out.println(pt.getX() + '|' + pt.getY());
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
