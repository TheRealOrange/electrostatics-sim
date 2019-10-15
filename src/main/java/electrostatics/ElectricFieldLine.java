package electrostatics;

import math.AdaptiveRungeKutta;
import math.RungeKutta;
import math.Vector2D;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ElectricFieldLine extends FieldLine {
    private static int fine_compute_distance = 1000;

    private static double fine_precision_adaptive = 0.5;
    private static double fine_step_adaptive = 0.5;
    private static double rough_step_adaptive = 5;
    private static double rough_precision_adaptive = 5;

    private static double fine_step = 50;
    private static double rough_step = 250;

    private static int num_steps = 10000;

    private Function<Vector2D, Boolean> func;
    private int numsteps;
    private double[] step;

    private double precision;
    private double maxstep;

    private int linenum;

    private Vector2D end;

    private Particle charge;

    public ElectricFieldLine(Particle charge, Vector2D start, RungeKutta solver, Function<Vector2D, Boolean> func, CompletableFuture<Void> future, int linenum) {
        this(charge, start, solver, func, future, linenum, num_steps, rough_precision_adaptive, rough_step_adaptive);
    }

    public ElectricFieldLine(Particle charge, Vector2D start, RungeKutta solver, Function<Vector2D, Boolean> func, CompletableFuture<Void> future, int linenum, int numsteps, double precision, double maxstep) {
        super(start, solver, future);
        this.func = func;
        this.numsteps = numsteps;
        this.precision = precision;
        this.maxstep = maxstep;
        this.step = new double[]{rough_step};
        this.linenum = linenum;
        this.charge = charge;
    }

    @Override
    public void run() {
        Vector2D point = this.start.clone();
        add(point);

        double dist = 0;
        double t = 0;

        double[] nextstep = new double[]{this.step[0]};

        for (int i = 0; i < this.numsteps; ++i) {
            dist = start.sub(point).magnitude() * unit;

            if (solver instanceof AdaptiveRungeKutta) {
                this.precision = (dist < fine_compute_distance) ? fine_precision_adaptive : rough_precision_adaptive;
                this.maxstep = (dist < fine_compute_distance) ? fine_step_adaptive : rough_step_adaptive;
                ((AdaptiveRungeKutta) solver).setMaxstep(maxstep);
            } else this.step[0] = (dist < fine_compute_distance) ? fine_step : rough_step;

            point = solver.step(point, t, step, nextstep, precision);
            add(point);
            //System.out.println(point);

            if (this.func.apply(point)) {
                end = point.clone();
                break;
            }

            t += step[0];
            if (solver instanceof AdaptiveRungeKutta) step[0] = nextstep[0];
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

    public static double getFine_compute_distance() {
        return fine_compute_distance;
    }

    public static void setFine_compute_distance(int fine_compute_distance) {
        ElectricFieldLine.fine_compute_distance = fine_compute_distance;
    }

    public static double getFine_precision_adaptive() {
        return fine_precision_adaptive;
    }

    public static void setFine_precision_adaptive(double fine_precision_adaptive) {
        ElectricFieldLine.fine_precision_adaptive = fine_precision_adaptive;
    }

    public static double getFine_step_adaptive() {
        return fine_step_adaptive;
    }

    public static void setFine_step_adaptive(double fine_step_adaptive) {
        ElectricFieldLine.fine_step_adaptive = fine_step_adaptive;
    }

    public static double getRough_step_adaptive() {
        return rough_step_adaptive;
    }

    public static void setRough_step_adaptive(double rough_step_adaptive) {
        ElectricFieldLine.rough_step_adaptive = rough_step_adaptive;
    }

    public static double getRough_precision_adaptive() {
        return rough_precision_adaptive;
    }

    public static void setRough_precision_adaptive(double rough_precision_adaptive) {
        ElectricFieldLine.rough_precision_adaptive = rough_precision_adaptive;
    }

    public static double getFine_step() {
        return fine_step;
    }

    public static void setFine_step(double fine_step) {
        ElectricFieldLine.fine_step = fine_step;
    }

    public static double getRough_step() {
        return rough_step;
    }

    public static void setRough_step(double rough_step) {
        ElectricFieldLine.rough_step = rough_step;
    }

    public static int getNum_steps() {
        return num_steps;
    }

    public static void setNum_steps(int num_steps) {
        ElectricFieldLine.num_steps = num_steps;
    }
}
