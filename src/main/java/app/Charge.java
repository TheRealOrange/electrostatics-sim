package app;

import electrostatics.Particle;
import javafx.scene.shape.Circle;
import math.Vector2D;

public class Charge extends Circle {
    private Particle charge;
    private boolean move;

    public Charge(Particle charge) {
        super();
        this.charge = charge;
        super.setRadius(charge.getRadius());
        super.setCenterX(this.charge.getPosition().getX());
        super.setCenterY(this.charge.getPosition().getY());
        this.move = false;
    }

    public Particle getCharge() {
        return charge;
    }

    public void setCharge(Particle charge) {
        this.charge = charge;
    }

    public Vector2D getPos() {
        return new Vector2D(super.getCenterX(), super.getCenterY());
    }

    public void setPos(Vector2D pos) {
        super.setCenterX(pos.getX());
        super.setCenterY(pos.getY());
        this.charge.setPosition(pos);
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }
}
