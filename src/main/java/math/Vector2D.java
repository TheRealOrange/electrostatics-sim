package math;

import java.awt.*;
import java.io.Serializable;

public class Vector2D implements Cloneable, Serializable {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public double getX() { return x; }

    public void setX(double x) { this.x = x; }

    public double getY() { return y; }

    public void setY(double y) { this.y = y; }

    public double magnitude() {
        return Math.pow((x*x+y*y), 0.5);
    }

    public double direction() {
        return Math.atan(y/x);
    }

    public Vector2D mul(double a) {
        return new Vector2D(this.x*a, this.y*a);
    }

    public Vector2D div(double a) {
        return new Vector2D(this.x/a, this.y/a);
    }

    public Vector2D add(Vector2D a) {
        return new Vector2D(this.x+a.getX(), this.y+a.getY());
    }

    public Vector2D sub(Vector2D a) {
        return new Vector2D(this.x-a.getX(), this.y-a.getY());
    }

    public double dot(Vector2D a) {
        return this.x*a.getX()+this.y*a.getY();
    }

    public Vector2D clone() {
        return new Vector2D(this.x, this.y);
    }

    public Vector2D unit() {
        return this.div(this.magnitude());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2D) return ((Vector2D)obj).getX()==this.x && ((Vector2D)obj).getY()==this.y;
        return false;
    }

    @Override
    public String toString() {
        return "[Vector2D (x: " + this.x + ", y: " + this.y + ")]";
    }
}
