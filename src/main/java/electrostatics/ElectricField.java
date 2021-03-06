package electrostatics;

import math.AdaptiveRungeKutta;
import math.RungeKutta;
import math.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiFunction;

public class ElectricField extends Field {
    private BiFunction<Vector2D, Vector2D, Double> func;
    private int linedensity;

    public ElectricField(ThreadPoolExecutor threadpool, ArrayList<Particle> charges, RungeKutta solver, BiFunction<Vector2D, Vector2D, Double> func, int linedensity) {
        super(threadpool, charges, solver);
        this.linedensity = linedensity;
        this.func = func;
    }

    public CompletableFuture<Void> compute() {
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Particle p : super.charges) {
            //System.out.printf("particle x: %f y: %f\n", p.getPosition().getX(), p.getPosition().getY());
            if (p.getCharge() > 0) {
                int lines = (int) (linedensity * p.getCharge());
                double[][] points = new double[lines][2];
                for (int i = 0; i < lines; ++i) {
                    CompletableFuture<Void> f = new CompletableFuture<>();
                    futures.add(f);
                    points[i][0] = (p.getRadius() + 1) * Math.sin((2.0 / (double) lines) * i * Math.PI) + p.getPosition().getX();
                    points[i][1] = (p.getRadius() + 1) * Math.cos((2.0 / (double) lines) * i * Math.PI) + p.getPosition().getY();
                    //System.out.printf("circle pts %f %f\n", points[i][0], points[i][1]);
                    super.lines.add(new ElectricFieldLine(p.clone(), new Vector2D(points[i][0], points[i][1]), (super.solver instanceof AdaptiveRungeKutta)?((AdaptiveRungeKutta) super.solver).clone():super.solver.clone(), func, f, i));
                }
            }
        }

        for (FieldLine line : super.lines) {
            super.threadpool.submit(line);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public int getLinedensity() {
        return linedensity;
    }

    public void setLinedensity(int linedensity) {
        this.linedensity = linedensity;
    }
}
