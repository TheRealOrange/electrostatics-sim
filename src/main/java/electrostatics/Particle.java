package electrostatics;


import math.Vector2D;

import java.io.Serializable;

public class Particle implements Cloneable, Serializable {
    transient public static int ID = 0;
    private double charge;
    private double radius;
    private Vector2D position;
    private int id;

    public Particle(double charge, double radius, Vector2D position) {
        this.charge = charge;
        this.radius = radius;
        this.position = position;
        this.id = ++ID;
    }

    public Particle(Particle p) {
        this(p.getCharge(), p.getRadius(), p.getPosition());
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
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
        double r = dist.magnitude();
        Vector2D field = dist.div(Math.pow(r, 3)).mul(this.charge);
        return field;
    }

    public double potential(Vector2D position) {
        return charge / position.sub(this.position).magnitude();
    }

    public Particle clone() {
        Particle p = new Particle(this);
        p.setId(this.id);
        return p;
    }
}
