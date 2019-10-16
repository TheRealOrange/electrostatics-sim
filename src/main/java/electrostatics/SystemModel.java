package electrostatics;

import math.RungeKutta;
import math.Vector2D;

import java.util.ArrayList;
import java.util.Collections;
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
        this.potentialint = 0.1;
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

        ArrayList<PotentialFieldLine> plines = new ArrayList<>();
        for (FieldLine fl : this.ufield.getLines()) plines.add((PotentialFieldLine) fl);
        Collections.sort(plines);

        if (plines.size() <= 0) return;
        double pt = plines.get(0).getPotential();

        ArrayList< ArrayList<PotentialFieldLine> > equipotentials = new ArrayList<>(); equipotentials.add(new ArrayList<>());
        ArrayList< ArrayList<Boolean> > duplicate = new ArrayList<>(); duplicate.add(new ArrayList<>());
        int i = 0;

        for (PotentialFieldLine pfl : plines) {
            if (Math.abs(pfl.getPotential()-pt) >= potentialint) {
                equipotentials.add(new ArrayList<>()); duplicate.add(new ArrayList<>()); ++i;
                pt = pfl.getPotential();
            }
            equipotentials.get(i).add(pfl);
            duplicate.get(i).add(true);
        }

        ArrayList<PotentialFieldLine> apl;
        for (int n = 0;n < equipotentials.size();++n) {
            apl = equipotentials.get(n);
            for (int j = 0;j < apl.size();++j) {
                for (int k = j+1;k < apl.size() && duplicate.get(n).get(j);++k) {
                    System.out.println(apl.get(j).getStd().sub(apl.get(k).getStd()).magnitude() + ", " + apl.get(j).getWcenter().sub(apl.get(k).getWcenter()).magnitude());
                    if (apl.get(j).getStd().sub(apl.get(k).getStd()).magnitude() < 35 && apl.get(j).getWcenter().sub(apl.get(k).getWcenter()).magnitude() < 5) {
                        duplicate.get(n).set(k, false);
                    }
                }
            }
        }

        ArrayList<FieldLine> newlines = new ArrayList<>();

        for (int n = 0;n < equipotentials.size();++n) {
            apl = equipotentials.get(n);
            for (int j = 0;j < apl.size();++j) {
                if (duplicate.get(n).get(j)) newlines.add(apl.get(j));
            }
        }

        this.ufield.setLines(newlines);
    }

    public Particle getParticleAt(Vector2D pos) {
        for (Particle p : this.charges) {
            if (p.getPosition().sub(pos).magnitude() < p.getRadius()) return p;
        }
        return null;
    }

    public boolean checkMoveCollision(Particle charge, Vector2D pos) {
        charge = findID(charge);
        for (Particle p : this.charges) {
            //System.out.printf("x: %f, y: %f\n", p.getPosition().getX(), p.getPosition().getY());
            if (p == charge) continue;
            if (p.getPosition().sub(pos).magnitude() <= (p.getRadius() + charge.getRadius())) return true;
        }
        return false;
    }

    public double checkCollision(Vector2D pos1, Vector2D pos2) {
        double mindist = Double.MAX_VALUE;
        double dist;
        double dRadius;
        double dLen;
        double dA, dB, dC;
        double dDistToLine;
        boolean collision = false;
        for (Particle p : this.charges) {
            dist = p.getPosition().sub(pos2).magnitude();
            mindist = Math.min(mindist, dist);
            dRadius = p.getRadius() + 0.1;
            if (dist <= dRadius)
                collision = true;
            else {
                dLen = pos2.sub(pos1).magnitude();
                if (dLen > dRadius) {
                    // x(y2-y1) + y(x1-x2) + y1(x2-x1)-x1(y2-y1)=0
                    dA = pos2.getY() - pos1.getY();
                    dB = pos1.getX() - pos2.getX();
                    dC = pos1.getY() * (pos2.getX() - pos1.getX()) - pos1.getX() * (pos2.getY()-pos1.getY());
                    dDistToLine = Math.abs(dA * p.getPosition().getX() + dB*p.getPosition().getY()+dC)/Math.sqrt(dA*dA+dB*dB);
                    if (dDistToLine<=dRadius)
                        collision = true;
                }

            }
        }
        return (collision)?-mindist:mindist;
    }

    public boolean moveParticle(Particle p, Vector2D end) {
        p = findID(p);
        if (!checkMoveCollision(p, end)) p.setPosition(end);
        else return false;
        return true;
    }

    public Vector2D field(Vector2D position) {
        Vector2D e = new Vector2D(0, 0);
        for (Particle p : this.charges) e = e.add(p.field(position));
        //System.out.println(e);
        return e;
    }

    public double potential(Vector2D position) {
        double u = 0;
        for (Particle p : this.charges) u += p.potential(position);
        return u;
    }

    public Vector2D solveField(double t, Vector2D position) {
        return field(position);
    }

    public Vector2D solvePotential(double t, Vector2D position) {
        Vector2D field = field(position);
        double x = field.getX();
        field.setX(-field.getY());
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
        this.charges.add(new Particle(charge, radius, pos.clone()));
    }

    public void addCharge(Particle p) {
        this.charges.add(p.clone());
    }

    public void removeCharge(Particle p) { this.charges.remove(findID(p)); }

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
