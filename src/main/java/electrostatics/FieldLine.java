package electrostatics;

import math.RungeKutta;
import math.Vector2D;

import java.util.ArrayList;

public abstract class FieldLine {
    private ArrayList<Vector2D> points;
    Vector2D start;
    RungeKutta solver;
    double unit;

    public FieldLine(Vector2D start, RungeKutta solver) {
        points = new ArrayList<>();
        this.start = start;
        this.solver = solver;
        this.unit = 10;
    }

    public void add(Vector2D pt) {
        points.add(pt.clone());
    }

    public ArrayList<Vector2D> getPoints() {
        return points;
    }

    public double getUnit() {
        return unit;
    }
}