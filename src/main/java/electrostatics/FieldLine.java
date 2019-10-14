package electrostatics;

import math.RungeKutta;
import math.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public abstract class FieldLine implements Runnable {
    private ArrayList<Vector2D> points;
    Vector2D start;
    RungeKutta solver;
    double unit;

    CompletableFuture<Void> future;

    public FieldLine(Vector2D start, RungeKutta solver, CompletableFuture<Void> future) {
        points = new ArrayList<>();
        this.start = start;
        this.solver = solver;
        this.unit = 10;
        this.future = future;
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