package elements;

import app.App;
import canvas.CanvasNode;
import canvas.Movable;
import electrostatics.Particle;
import javafx.scene.shape.Circle;
import math.Vector2D;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Charge extends Circle implements CanvasNode {
    private Particle charge;
    private final Movable movable;

    private Runnable compute;
    private Runnable display;

    private Consumer<Charge> checkbounds;

    public Charge(Particle charge, Runnable compute, Runnable display, Consumer<Charge> checkbounds) {
        super();
        this.charge = charge;
        this.checkbounds = checkbounds;
        super.setRadius(charge.getRadius());
        movable = new Movable(this, this::getCoords, this::setCoords, this::checkbounds);
        movable.setCoords(this.charge.getPosition()); super.toBack();

        this.compute = compute;
        this.display = display;
    }

    public Charge(Particle charge) {
        this(charge, null, null, null);
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

    public void checkbounds() {
        if (this.checkbounds != null) this.checkbounds.accept(this);
        this.compute.run();
        this.display.run();
    }

    @Override
    public void reposition(Vector2D prevOffset, Vector2D offset) {
        Vector2D pos = movable.getCoords().sub(prevOffset).add(offset);
        super.setCenterX(pos.getX());
        super.setCenterY(pos.getY());
        App.model.moveParticle(charge, pos);
        this.charge.setPosition(pos);
    }
}
