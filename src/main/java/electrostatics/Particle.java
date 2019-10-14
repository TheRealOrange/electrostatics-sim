package electrostatics;


import math.Constants;
import math.Vector2D;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import java.io.Serializable;

public class Particle implements Cloneable, Serializable {
    transient public static int ID = 0;
    private Apfloat charge;
    private double radius;
    private Vector2D position;
    private int id;

    public Particle(Apfloat charge, double radius, Vector2D position) {
        this.charge = charge.add(new Apfloat(0, Constants.getPrecision()));
        this.radius = radius;
        this.position = position.clone();
        this.id = ++ID;
    }

    public Particle(Particle p) {
        this(p.getCharge(), p.getRadius(), p.getPosition());
    }

    public Apfloat getCharge() {
        return charge.add(new Apfloat(0, Constants.getPrecision()));
    }

    public void setCharge(Apfloat charge) {
        this.charge = charge.add(new Apfloat(0, Constants.getPrecision()));
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector2D getPosition() {
        return position.clone();
    }

    public void setPosition(Vector2D position) {
        this.position = position.clone();
    }

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        Particle.ID = ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vector2D field(Vector2D position) {
        Vector2D dist = position.sub(this.position);
        Apfloat r = dist.fast_magnitude();
        Vector2D field = dist.div(ApfloatMath.pow(r, 3)).mul(this.charge);
        return field;
    }

    public Apfloat potential(Vector2D position) {
        return charge.divide(position.sub(this.position).fast_magnitude());
    }

    public Particle clone() {
        Particle p = new Particle(this);
        p.setId(this.id);
        return p;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "charge=" + charge.toString() +
                ", radius=" + radius +
                ", position=" + position +
                ", id=" + id +
                '}';
    }
}
