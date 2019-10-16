package elements;

import canvas.CanvasNode;
import electrostatics.PotentialFieldLine;
import javafx.scene.shape.Polyline;
import math.Vector2D;

import java.util.ArrayList;

public class Potential extends Polyline implements CanvasNode {
    private PotentialFieldLine pfl;
    ArrayList<Double> points;
    public Potential(PotentialFieldLine pfl) {
        this.pfl = pfl;
    }

    public void draw() {
        synchronized (super.getPoints()) {
            super.getPoints().clear();
            points = new ArrayList<>();
            double x = pfl.getPoints().get(0).getX()-10; double y = pfl.getPoints().get(0).getY()-10;
            for (Vector2D pt : pfl.getPoints()) {
                //System.out.printf("%f, y: %f\n", pt.getX(), pt.getY());
                if (Math.abs(pt.getX() - x) > 0.5 || Math.abs(pt.getY() - y) > 0.5) {
                    x = pt.getX(); y = pt.getY();
                    points.add(x); points.add(y);
                }
            }
            super.getPoints().addAll(points);
            super.toBack();
        }
    }

    @Override
    public void reposition(Vector2D prevOffset, Vector2D offset) {
        Vector2D newoffset = prevOffset.sub(offset);
        super.setLayoutX(super.getLayoutX()-newoffset.getX());
        super.setLayoutY(super.getLayoutY()-newoffset.getY());
    }
}
