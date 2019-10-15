package math;

import java.awt.*;
import javafx.geometry.Point2D;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import java.io.Serializable;

public class Vector2D implements Cloneable, Serializable {
    private static final Apfloat a0 = (new Apfloat(127, Constants.getPrecision())).divide(new Apfloat(128, Constants.getPrecision()));
    private static final Apfloat b0 = (new Apfloat(3, Constants.getPrecision())).divide(new Apfloat(16, Constants.getPrecision()));
    private static final Apfloat a1 = (new Apfloat(23, Constants.getPrecision())).divide(new Apfloat(32, Constants.getPrecision()));
    private static final Apfloat b1 = (new Apfloat(71, Constants.getPrecision())).divide(new Apfloat(128, Constants.getPrecision()));

    private Apfloat x;
    private Apfloat y;

    public Vector2D() {
        this(0.0, 0.0);
    }

    public Vector2D(Apfloat x, Apfloat y) {
        this.x = x.add(new Apfloat(0, Constants.getPrecision()));
        this.y = y.add(new Apfloat(0, Constants.getPrecision()));
    }

    public Vector2D(double x, double y) {
        this(new Apfloat(x, Constants.getPrecision()), new Apfloat(y, Constants.getPrecision()));
    }

    public Vector2D(Point p) {
        this.x = new Apfloat(p.getX(), Constants.getPrecision());
        this.y = new Apfloat(p.getY(), Constants.getPrecision());
    }

    public Vector2D(Point2D p) {
        this.x = new Apfloat(p.getX(), Constants.getPrecision());
        this.y = new Apfloat(p.getY(), Constants.getPrecision());
    }

    public Apfloat getX() { return x.add(new Apfloat(0, Constants.getPrecision())); }

    public void setX(Apfloat x) { this.x = x.add(new Apfloat(0, Constants.getPrecision())); }

    public Apfloat getY() { return y.add(new Apfloat(0, Constants.getPrecision())); }

    public void setY(Apfloat y) { this.y = y.add(new Apfloat(0, Constants.getPrecision())); }

    public Apfloat magnitude() {
        return ApfloatMath.sqrt(x.multiply(x).add(y.multiply(y)));
    }

    public Apfloat direction() {
        return ApfloatMath.atan(y.divide(x));
    }

    public Vector2D mul(Apfloat a) {
        return new Vector2D(this.x.multiply(a), this.y.multiply(a));
    }

    public Vector2D div(Apfloat a) {
        return new Vector2D(this.x.divide(a), this.y.divide(a));
    }

    public Vector2D add(Vector2D a) {
        return new Vector2D(this.x.add(a.getX()), this.y.add(a.getY()));
    }

    public Vector2D sub(Vector2D a) {
        return new Vector2D(this.x.subtract(a.getX()), this.y.subtract(a.getY()));
    }

    public Apfloat dot(Vector2D a) {
        return this.x.multiply(a.getX()).add(this.y.multiply(a.getY()));
    }

    public Vector2D clone() {
        return new Vector2D(this.x, this.y);
    }

    public Vector2D unit() {
        return this.div(this.magnitude());
    }

    public Point2D toPoint() { return new Point2D(this.x.doubleValue(), this.y.doubleValue()); }

    public Apfloat fast_magnitude() {
        Apfloat abs_x = ApfloatMath.abs(this.x);
        Apfloat abs_y = ApfloatMath.abs(this.y);
        Apfloat max = ApfloatMath.max(abs_x, abs_y);
        Apfloat min = ApfloatMath.min(abs_x, abs_y);

        return ApfloatMath.max(a0.multiply(max).add(b0.multiply(min)), a1.multiply(max).add(b1.multiply(min)));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2D) return ((Vector2D)obj).getX().equals(this.x) && ((Vector2D)obj).getY().equals(this.y);
        return false;
    }

    @Override
    public String toString() {
        return "[Vector2D (x: " + this.x.toString() + ", y: " + this.y.toString() + ")]";
    }
}
