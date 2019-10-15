package electrostatics;

import math.AdaptiveRungeKutta;
import math.Constants;
import math.RungeKutta;
import math.Vector2D;
import org.apfloat.Apfloat;

import java.util.concurrent.CompletableFuture;

public class PotentialFieldLine extends FieldLine {
    private static int num_steps_adaptive = 8000;
    private static Apfloat step_amt_adaptive = new Apfloat(0.001);

    private static int num_steps = 15000;
    private static Apfloat step_amt = new Apfloat(0.0001);

    private static Apfloat tolerance = new Apfloat(1e-3);

    private Apfloat potential;
    private Vector2D wcenter;

    private int numsteps;
    private Apfloat[] step;

    public PotentialFieldLine(Vector2D start, RungeKutta solver, CompletableFuture<Void> future, Apfloat potential, Vector2D wcenter) {
        super(start, solver, future);
        this.potential = potential.add(new Apfloat(0, Constants.getPrecision()));
        this.wcenter = wcenter;
        this.numsteps = (solver instanceof AdaptiveRungeKutta)?num_steps_adaptive:num_steps;
        this.step = new Apfloat[]{(solver instanceof AdaptiveRungeKutta)?step_amt_adaptive:step_amt};
    }

    @Override
    public void run() {
        Vector2D point = this.start;
        Apfloat dist;
        Apfloat t = new Apfloat(0, Constants.getPrecision());

        Apfloat[] nextstep = new Apfloat[]{this.step[0]};

        for (int i = 0;i < this.numsteps;++i) {
            point = solver.step(point, t, step, nextstep, tolerance);

            add(point);

            wcenter = wcenter.add(point.mul(step[0]));

            t = t.add(step[0]);
            if (solver instanceof AdaptiveRungeKutta) step[0] = nextstep[0];

            dist = start.sub(point).fast_magnitude();
            if (dist.multiply(new Apfloat(unit)).doubleValue() < 6. && t.doubleValue() > 2.*dist.doubleValue()) { add(start); wcenter = wcenter.div(t); break; }
        }

        super.future.complete(null);
    }

    public static int getNum_steps_adaptive() {
        return num_steps_adaptive;
    }

    public static void setNum_steps_adaptive(int num_steps_adaptive) {
        PotentialFieldLine.num_steps_adaptive = num_steps_adaptive;
    }

    public static Apfloat getStep_amt_adaptive() {
        return step_amt_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setStep_amt_adaptive(Apfloat step_amt_adaptive) {
        PotentialFieldLine.step_amt_adaptive = step_amt_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static int getNum_steps() {
        return num_steps;
    }

    public static void setNum_steps(int num_steps) {
        PotentialFieldLine.num_steps = num_steps;
    }

    public static Apfloat getStep_amt() {
        return step_amt.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setStep_amt(Apfloat step_amt) {
        PotentialFieldLine.step_amt = step_amt.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static Apfloat getTolerance() {
        return tolerance.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setTolerance(Apfloat tolerance) {
        PotentialFieldLine.tolerance = tolerance.add(new Apfloat(0, Constants.getPrecision()));
    }
}
