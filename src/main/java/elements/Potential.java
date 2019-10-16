package elements;

import canvas.CanvasNode;
import electrostatics.PotentialFieldLine;
import javafx.scene.shape.Polyline;
import math.Vector2D;

import java.util.ArrayList;

public class Potential extends DrawLine {
    public Potential(PotentialFieldLine pfl) {
        super(pfl);
    }
}
