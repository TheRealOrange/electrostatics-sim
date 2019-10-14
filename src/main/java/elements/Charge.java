package elements;

import app.App;
import canvas.CanvasNode;
import canvas.Movable;
import electrostatics.Particle;
import javafx.scene.shape.Circle;
import math.Vector2D;

public class Charge extends Circle implements CanvasNode {
    private Particle charge;
    private final Movable movable;

    public Charge(Particle charge) {
        super();
        this.charge = charge;
        super.setRadius(charge.getRadius());
        movable = new Movable(this, this::getCoords, this::setCoords);
        movable.setCoords(this.charge.getPosition());
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
        super.setCenterX(pos.getX().doubleValue());
        super.setCenterY(pos.getY().doubleValue());
        App.model.moveParticle(charge, pos);
        this.charge.setPosition(pos);
    }

    @Override
    public void reposition(Vector2D prevOffset, Vector2D offset) {
        movable.setCoords(movable.getCoords().sub(prevOffset).add(offset));
    }
}
