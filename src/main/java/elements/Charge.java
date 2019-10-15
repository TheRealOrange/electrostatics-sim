package elements;

import app.App;
import canvas.CanvasNode;
import canvas.Movable;
import electrostatics.Particle;
import javafx.scene.shape.Circle;
import math.Vector2D;

import java.util.function.Function;

public class Charge extends Circle implements CanvasNode {
    private Particle charge;
    private final Movable movable;

    private Runnable compute;
    private Runnable display;

    public Charge(Particle charge, Runnable compute, Runnable display) {
        super();
        this.charge = charge;
        super.setRadius(charge.getRadius());
        movable = new Movable(this, this::getCoords, this::setCoords);
        movable.setCoords(this.charge.getPosition());

        this.compute = compute;
        this.display = display;
    }

    public Charge(Particle charge) {
        this(charge, null, null);
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

    public Vector2D getCoords() {
        return charge.getPosition();
    }

    public void setCoords(Vector2D pos) {
        super.setCenterX(pos.getX());
        super.setCenterY(pos.getY());
        App.model.moveParticle(charge, pos);
        this.charge.setPosition(pos);

        if (compute != null && display != null) {
            this.compute.run();
            this.display.run();
        }
    }

    @Override
    public void reposition(Vector2D prevOffset, Vector2D offset) {
        movable.setCoords(movable.getCoords().sub(prevOffset).add(offset));
    }
}
