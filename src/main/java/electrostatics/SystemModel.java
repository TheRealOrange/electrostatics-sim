package electrostatics;

import math.Constants;
import math.RungeKutta;
import math.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SystemModel {
    private Vector2D origin;
    private ArrayList<Particle> charges;
    private ElectricField efield;
    private PotentialField ufield;
    private ThreadPoolExecutor threadpool;

    private double potentialint;
    private int linedensity;

    private RungeKutta efieldsolver;
    private RungeKutta ufieldsolver;

    public SystemModel(ArrayList<Particle> charges, Vector2D origin, RungeKutta efieldsolver, RungeKutta ufieldsolver) {
        this.charges = new ArrayList<>(charges);
        this.origin = origin.clone();
        this.efieldsolver = efieldsolver;
        this.ufieldsolver = ufieldsolver;
        this.potentialint = 10;
        this.linedensity = 1;
        this.threadpool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        this.efield = new ElectricField(this.threadpool, this.charges, this.efieldsolver, this::checkCollision, this.linedensity);
        this.ufield = new PotentialField(this.threadpool, this.charges, this.ufieldsolver, this::potential, null, this.potentialint);
    }

    public SystemModel() {
        this(new ArrayList<Particle>(), new Vector2D(), null, null);
    }

    public void compute() throws InterruptedException, ExecutionException {
        this.efield = new ElectricField(this.threadpool, charges, this.efieldsolver, this::checkCollision, this.linedensity);
        this.efield.compute().get();
        ArrayList<ElectricFieldLine> lines = new ArrayList<>();
        for (FieldLine fl : this.efield.getLines()) lines.add((ElectricFieldLine) fl);
        this.ufield = new PotentialField(this.threadpool, charges, this.ufieldsolver, this::potential, lines, this.potentialint);
        this.ufield.compute().get();
    }

    public Particle getParticleAt(Vector2D pos) {
        for (Particle p : this.charges) {
            Vector2D dist = p.getPosition().sub(pos);
            if (p.getPosition().sub(pos).magnitude() < p.getRadius()) return p;
        }
        return null;
    }

    public boolean checkMoveCollision(Particle charge, Vector2D pos) {
        charge = findID(charge);
        for (Particle p : this.charges) {
            //System.out.printf("x: %f, y: %f\n", p.getPosition().getX(), p.getPosition().getY());
            if (p == charge) continue;
            if (p.getPosition().sub(pos).magnitude() <= (p.getRadius() + charge.getRadius() + 2) <= 0) return true;
        }
        return false;
    }

    public boolean checkCollision(Vector2D pos) {
        for (Particle p : this.charges) {
            if (p.getPosition().sub(pos).magnitude() <= (p.getRadius() + 2)) return true;
        }
        return false;
    }

    public boolean moveParticle(Particle p, Vector2D end) {
        p = findID(p);
        if (!checkMoveCollision(p, end)) p.setPosition(end);
        else return false;
        return true;
    }

    public Vector2D field(Vector2D position) {
        Vector2D e = new Vector2D();
        for (Particle p : this.charges) e = e.add(p.field(position));
        //System.out.println(e);
        return e;
    }

    public Apfloat potential(Vector2D position) {
        Apfloat u = new Apfloat(0, Constants.getPrecision());
        for (Particle p : this.charges) u = u.add(p.potential(position));
        return u;
    }

    public Vector2D solveField(double t, Vector2D position) {
        return field(position);
    }

    public Vector2D solvePotential(double t, Vector2D position) {
        Vector2D field = field(position);
        double x = field.getX();
        field.setX(field.getY().negate());
        field.setY(x);
        return field.unit();
    }

    public Vector2D getOrigin() {
        return this.origin;
    }

    public void setOrigin(Vector2D origin) {
        this.origin = origin;
    }

    public double getPotentialint() {
        return potentialint;
    }

    public void setPotentialint(double potentialint) {
        this.potentialint = potentialint;
    }

    public int getLinedensity() {
        return linedensity;
    }

    public void setLinedensity(int linedensity) {
        this.linedensity = linedensity;
    }

    public RungeKutta getEfieldsolver() {
        return efieldsolver;
    }

    public void setEfieldsolver(RungeKutta efieldsolver) {
        this.efieldsolver = efieldsolver;
    }

    public RungeKutta getUfieldsolver() {
        return ufieldsolver;
    }

    public void setUfieldsolver(RungeKutta ufieldsolver) {
        this.ufieldsolver = ufieldsolver;
    }

    public void addCharge(Vector2D pos, double radius, double charge) {
        this.charges.add(new Particle(new Apfloat(charge), radius, pos.clone()));
    }

    public void addCharge(Particle p) {
        this.charges.add(p.clone());
    }

    public Particle findID(Particle charge) {
        for (Particle p : this.charges) if (p.getId() == charge.getId()) return p;
        return null;
    }

    public ArrayList<PotentialFieldLine> getPotentialLines() {
        ArrayList<PotentialFieldLine> lines = new ArrayList<>();
        for (FieldLine pfl : this.ufield.getLines()) lines.add((PotentialFieldLine) pfl);
        return lines;
    }

    public ArrayList<ElectricFieldLine> getFieldLines() {
        ArrayList<ElectricFieldLine> lines = new ArrayList<>();
        for (FieldLine efl : this.efield.getLines()) lines.add((ElectricFieldLine) efl);
        return lines;
    }
}
