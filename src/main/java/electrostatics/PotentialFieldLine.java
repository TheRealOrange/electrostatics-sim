package electrostatics;

import math.AdaptiveRungeKutta;
import math.RungeKutta;
import math.Vector2D;

import java.util.concurrent.CompletableFuture;

public class PotentialFieldLine extends FieldLine implements Comparable<PotentialFieldLine>, Cloneable {
    private static int num_steps_adaptive = 10000;
    private static double step_amt_adaptive = 0.01;

    private static int num_steps = 20000;
    private static double step_amt = 0.1;

    private static double tolerance = 1e-3;

    private double potential;
    private Vector2D wcenter;
    private Vector2D std;

    private int numsteps;
    private double[] step;

    public PotentialFieldLine(Vector2D start, RungeKutta solver, CompletableFuture<Void> future, double potential, Vector2D wcenter) {
        super(start, solver, future);
        this.potential = potential;
        this.wcenter = wcenter;
        this.numsteps = (solver instanceof AdaptiveRungeKutta)?num_steps_adaptive:num_steps;
        this.step = new double[]{(solver instanceof AdaptiveRungeKutta)?step_amt_adaptive:step_amt};
        if (super.solver instanceof  AdaptiveRungeKutta) ((AdaptiveRungeKutta) super.solver).setMaxstep(0.1);
    }

    @Override
    public void run() {
        Vector2D point = this.start;
        Vector2D mean = new Vector2D();
        double dist;
        double t = 0;

        double[] nextstep = new double[]{this.step[0]};

        int i = 0;
        for (;i < this.numsteps;++i) {
            point = solver.step(point, t, step, nextstep, tolerance);

            add(point);

            wcenter = wcenter.add(point.mul(step[0]));
            mean = mean.add(point);

            t += step[0];
            if (solver instanceof AdaptiveRungeKutta) step[0] = nextstep[0];

            dist = start.sub(point).magnitude();
            if (dist * unit < 10.0 && t > 4.0*dist) { add(start); wcenter = wcenter.div(t); break; }
        }

        mean = mean.div(i);
        std = new Vector2D();
        Vector2D tmp;

        for (i = 0;i < super.getPoints().size();i++) {
            tmp = super.getPoints().get(i).sub(mean);
            std = std.add(new Vector2D(tmp.getX()*tmp.getX(), tmp.getY()*tmp.getY()));
        }

        std = std.div(i);
        std.setX(Math.sqrt(std.getX()));
        std.setY(Math.sqrt(std.getY()));

        super.future.complete(null);
    }

    public Vector2D getWcenter() {
        return wcenter;
    }

    public void setWcenter(Vector2D wcenter) {
        this.wcenter = wcenter;
    }

    public Vector2D getStd() {
        return std;
    }

    public void setStd(Vector2D std) {
        this.std = std;
    }

    @Override
    public int compareTo(PotentialFieldLine potentialFieldLine) {
        if (this.potential == potentialFieldLine.potential) return 0;
        return (this.potential > potentialFieldLine.potential)?1:-1;
    }

    public double getPotential() {
        return potential;
    }

    public FieldLine clone() {
        PotentialFieldLine pfl = new PotentialFieldLine(this.start, super.solver, super.future, this.potential, this.wcenter);
        pfl.setPoints(this.getPoints());
        pfl.setWcenter(this.wcenter);
        pfl.setStd(this.std);
        return pfl;
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
