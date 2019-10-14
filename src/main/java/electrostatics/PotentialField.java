package electrostatics;

import math.RungeKutta;
import math.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

public class PotentialField extends Field {
    private Function<Vector2D, Double> func;
    private double potentialint;
    private ArrayList<ElectricFieldLine> lines;

    public PotentialField(ThreadPoolExecutor threadpool, ArrayList<Particle> charges, RungeKutta solver, Function<Vector2D, Double> func, ArrayList<ElectricFieldLine> lines, double potentialint) {
        super(threadpool, charges, solver);
        this.potentialint = potentialint;
        this.lines = lines;
        this.func = func;
    }

    public CompletableFuture<Void> compute() {
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
        for (ElectricFieldLine efl : this.lines) {
            if (efl.getLinenum() == 0) {
                double potential = func.apply(efl.start);
                for (int i = 0;i < efl.getPoints().size();++i) {
                    if (Math.abs(func.apply(efl.getPoints().get(i)) - potential) > potentialint) {
                        CompletableFuture<Void> f = new CompletableFuture<>();
                        futures.add(f);
                        potential = func.apply(efl.getPoints().get(i));
                        super.lines.add(new PotentialFieldLine(efl.getPoints().get(i), solver, f, this.func.apply(efl.getPoints().get(i)), efl.getCharge().getPosition()));
                    }
                }
            }
        }

        for (FieldLine line : super.lines) {
            super.threadpool.submit(line);
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}
