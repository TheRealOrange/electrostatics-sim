package app;

import electrostatics.ElectricFieldLine;
import javafx.scene.shape.Polyline;
import math.Vector2D;

public class Field extends Polyline {
    private ElectricFieldLine efl;
    public Field(ElectricFieldLine efl) {
        this.efl = efl;
    }

    public void draw() {
        synchronized (super.getPoints()) {
            System.out.println("drawing");
            for (Vector2D pt : efl.getPoints()) {
                super.getPoints().addAll(pt.getX(), pt.getY());
                //System.out.printf("%f, y: %f\n", pt.getX(), pt.getY());
            }
        }
    }
}
