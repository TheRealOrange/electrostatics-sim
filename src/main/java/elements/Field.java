package elements;

import canvas.CanvasNode;
import electrostatics.ElectricFieldLine;
import javafx.scene.shape.Polyline;
import math.Vector2D;

import java.util.ArrayList;

public class Field extends DrawLine {
    public Field(ElectricFieldLine efl) {
        super(efl);
    }
}
