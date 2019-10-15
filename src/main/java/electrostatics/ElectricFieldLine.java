package electrostatics;

import math.AdaptiveRungeKutta;
import math.Constants;
import math.RungeKutta;
import math.Vector2D;
import org.apfloat.Apfloat;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ElectricFieldLine extends FieldLine {
    private static int fine_compute_distance = 1000;

    private static Apfloat fine_precision_adaptive = new Apfloat(0.5, Constants.getPrecision());
    private static Apfloat fine_step_adaptive = new Apfloat(1, Constants.getPrecision());
    private static Apfloat rough_step_adaptive = new Apfloat(10, Constants.getPrecision());
    private static Apfloat rough_precision_adaptive = new Apfloat(10, Constants.getPrecision());

    private static Apfloat fine_step = new Apfloat(25, Constants.getPrecision());
    private static Apfloat rough_step = new Apfloat(100, Constants.getPrecision());

    private static int num_steps = 50000;

    private Function<Vector2D, Boolean> func;
    private int numsteps;
    private Apfloat[] step;

    private Apfloat precision;
    private Apfloat maxstep;

    private int linenum;

    private Vector2D end;

    private Particle charge;

    public ElectricFieldLine(Particle charge, Vector2D start, RungeKutta solver, Function<Vector2D, Boolean> func, CompletableFuture<Void> future, int linenum) {
        this(charge, start, solver, func, future, linenum, num_steps, rough_precision_adaptive, rough_step_adaptive);
    }

    public ElectricFieldLine(Particle charge, Vector2D start, RungeKutta solver, Function<Vector2D, Boolean> func, CompletableFuture<Void> future, int linenum, int numsteps, Apfloat precision, Apfloat maxstep) {
        super(start, solver, future);
        this.func = func;
        this.numsteps = numsteps;
        this.precision = precision;
        this.maxstep = maxstep;
        this.step = new Apfloat[]{rough_step};
        this.linenum = linenum;
        this.charge = charge;
    }

    @Override
    public void run() {
        Vector2D point = this.start.clone();
        add(point);

        Apfloat dist;
        Apfloat t = new Apfloat(0, Constants.getPrecision());

        Apfloat[] nextstep = new Apfloat[]{this.step[0]};

        for (int i = 0; i < this.numsteps; ++i) {
            dist = start.sub(point).fast_magnitude().multiply(new Apfloat(unit, Constants.getPrecision()));

            if (solver instanceof AdaptiveRungeKutta) {
                this.precision = (dist.doubleValue() < fine_compute_distance) ? fine_precision_adaptive : rough_precision_adaptive;
                this.maxstep = (dist.doubleValue() < fine_compute_distance) ? fine_step_adaptive : rough_step_adaptive;
                ((AdaptiveRungeKutta) solver).setMaxstep(maxstep);
            } else this.step[0] = (dist.doubleValue() < fine_compute_distance) ? fine_step : rough_step;

            point = solver.step(point, t, step, nextstep, precision);
            add(point);
            //System.out.println(point);

            if (this.func.apply(point)) {
                end = point.clone();
                break;
            }

            t = t.add(step[0]);
            if (solver instanceof AdaptiveRungeKutta) step[0] = nextstep[0].add(new Apfloat(0, Constants.getPrecision()));
        }
        super.future.complete(null);
    }

    public int getLinenum() {
        return linenum;
    }

    public void setLinenum(int linenum) {
        this.linenum = linenum;
    }

    public Vector2D getEnd() {
        return end;
    }

    public void setEnd(Vector2D end) {
        this.end = end;
    }

    public Particle getCharge() {
        return charge;
    }

    public void setCharge(Particle charge) {
        this.charge = charge;
    }

    public static int getFine_compute_distance() {
        return fine_compute_distance;
    }

    public static void setFine_compute_distance(int fine_compute_distance) {
        ElectricFieldLine.fine_compute_distance = fine_compute_distance;
    }

    public static Apfloat getFine_precision_adaptive() {
        return fine_precision_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setFine_precision_adaptive(Apfloat fine_precision_adaptive) {
        ElectricFieldLine.fine_precision_adaptive = fine_precision_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static Apfloat getFine_step_adaptive() {
        return fine_step_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setFine_step_adaptive(Apfloat fine_step_adaptive) {
        ElectricFieldLine.fine_step_adaptive = fine_step_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static Apfloat getRough_step_adaptive() {
        return rough_step_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setRough_step_adaptive(Apfloat rough_step_adaptive) {
        ElectricFieldLine.rough_step_adaptive = rough_step_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static Apfloat getRough_precision_adaptive() {
        return rough_precision_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setRough_precision_adaptive(Apfloat rough_precision_adaptive) {
        ElectricFieldLine.rough_precision_adaptive = rough_precision_adaptive.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static Apfloat getFine_step() {
        return fine_step.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setFine_step(Apfloat fine_step) {
        ElectricFieldLine.fine_step = fine_step.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static Apfloat getRough_step() {
        return rough_step.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static void setRough_step(Apfloat rough_step) {
        ElectricFieldLine.rough_step = rough_step.add(new Apfloat(0, Constants.getPrecision()));
    }

    public static int getNum_steps() {
        return num_steps;
    }

    public static void setNum_steps(int num_steps) {
        ElectricFieldLine.num_steps = num_steps;
    }
}
