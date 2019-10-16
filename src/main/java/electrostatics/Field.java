package electrostatics;

import math.RungeKutta;
import math.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class Field {
    ThreadPoolExecutor threadpool;
    ArrayList<Particle> charges;
    ArrayList<FieldLine> lines;
    RungeKutta solver;

    public Field(ThreadPoolExecutor threadpool, ArrayList<Particle> charges, RungeKutta solver) {
        this.threadpool = threadpool;
        this.charges = charges;
        this.solver = solver;
        this.lines = new ArrayList<>();
    }

    public ThreadPoolExecutor getThreadpool() {
        return threadpool;
    }

    public void setThreadpool(ThreadPoolExecutor threadpool) {
        this.threadpool = threadpool;
    }

    public ArrayList<Particle> getCharges() {
        return charges;
    }

    public void setCharges(ArrayList<Particle> charges) {
        this.charges = charges;
    }

    public ArrayList<FieldLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<FieldLine> lines) {
        this.lines = new ArrayList<>();
        for (FieldLine fl : lines) this.lines.add(fl.clone());
    }

    public RungeKutta getSolver() {
        return solver;
    }
}
