package app;

import electrostatics.PotentialFieldLine;
import javafx.scene.shape.Polyline;
import math.Vector2D;

public class Potential extends Polyline {
    private PotentialFieldLine pfl;
    public Potential(PotentialFieldLine pfl) {
        this.pfl = pfl;
    }

    public void draw() {
        for (Vector2D pt : pfl.getPoints()) {
            super.getPoints().addAll(pt.getX(), pt.getY());
            //System.out.printf("potential x: %f, y: %f\n", pt.getX(), pt.getY());
        }
    }
}
