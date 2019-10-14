package electrostatics;

import math.AdaptiveRungeKutta;
import math.RungeKutta;
import math.Vector2D;

import java.util.concurrent.CompletableFuture;

public class PotentialFieldLine extends FieldLine {
    private static int num_steps_adaptive = 8000;
    private static double step_amt_adaptive = 0.001;

    private static int num_steps = 15000;
    private static double step_amt = 0.0001;

    private static double tolerance = 1e-3;

    private double potential;
    private Vector2D wcenter;

    private int numsteps;
    private double[] step;

    public PotentialFieldLine(Vector2D start, RungeKutta solver, CompletableFuture<Void> future, double potential, Vector2D wcenter) {
        super(start, solver, future);
        this.potential = potential;
        this.wcenter = wcenter;
        this.numsteps = (solver instanceof AdaptiveRungeKutta)?num_steps_adaptive:num_steps;
        this.step = new double[]{(solver instanceof AdaptiveRungeKutta)?step_amt_adaptive:step_amt};
    }

    @Override
    public void run() {
        Vector2D point = this.start;
        double dist = 0;
        double t = 0;

        double[] nextstep = new double[]{this.step[0]};

        for (int i = 0;i < this.numsteps;++i) {
            point = solver.step(point, t, step, nextstep, tolerance);

            add(point);

            wcenter = wcenter.add(point.mul(step[0]));

            t += step[0];
            if (solver instanceof AdaptiveRungeKutta) step[0] = nextstep[0];

            dist = start.sub(point).magnitude();
            if (dist * unit < 6. && t > 2.*dist) { add(start); wcenter = wcenter.div(t); break; }
        }

        super.future.complete(null);
    }

    public static int getNum_steps_adaptive() {
        return num_steps_adaptive;
    }

    public static void setNum_steps_adaptive(int num_steps_adaptive) {
        PotentialFieldLine.num_steps_adaptive = num_steps_adaptive;
    }

    public static double getStep_amt_adaptive() {
        return step_amt_adaptive;
    }

    public static void setStep_amt_adaptive(double step_amt_adaptive) {
        PotentialFieldLine.step_amt_adaptive = step_amt_adaptive;
    }

    public static int getNum_steps() {
        return num_steps;
    }

    public static void setNum_steps(int num_steps) {
        PotentialFieldLine.num_steps = num_steps;
    }

    public static double getStep_amt() {
        return step_amt;
    }

    public static void setStep_amt(double step_amt) {
        PotentialFieldLine.step_amt = step_amt;
    }

    public static double getTolerance() {
        return tolerance;
    }

    public static void setTolerance(double tolerance) {
        PotentialFieldLine.tolerance = tolerance;
    }
}
