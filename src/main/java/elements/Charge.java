package elements;

import app.App;
import canvas.CanvasNode;
import canvas.Movable;
import electrostatics.Particle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import math.Vector2D;

import java.util.function.Function;

public class Charge extends Circle implements CanvasNode {
    private static final Paint[] colors = new Paint[]{Color.rgb(255, 128, 128), Color.rgb(112, 112, 255), Color.rgb(122, 122, 122)};
    private Particle charge;
    private final Movable movable;

    private Runnable compute;
    private Runnable display;

    private Function<Charge, Boolean> checkbounds;
    private Function<Charge, Boolean> select;

    public Charge(Particle charge, Runnable compute, Runnable display, Function<Charge, Boolean> checkbounds, Function<Charge, Boolean> select) {
        super();
        this.charge = charge;
        this.checkbounds = checkbounds;
        this.select = select;
        super.setRadius(charge.getRadius());

        movable = new Movable(this, this::getCoords, this::setCoords, this::eventHandler);
        movable.setCoords(this.charge.getPosition());
        super.toBack();

        this.compute = compute;
        this.display = display;

        updateColor();
        super.getStyleClass().add("charge");
    }

    private void updateColor() {
        if (this.charge.getCharge() > 0.5) super.setFill(colors[0]);
        else if (this.charge.getCharge() < -0.5) super.setFill(colors[1]);
        else super.setFill(colors[2]);
    }

    public Charge(Particle charge) {
        this(charge, null, null, null, null);
    }

    public Particle getCharge() {
        return charge;
    }

    public void setChargeRadius(double r) {
        this.charge.setRadius(r);
        super.setRadius(r);
        App.model.findID(this.charge).setRadius(r);
    }

    public void setCharge(double c) {
        this.charge.setCharge(c);
        App.model.findID(this.charge).setCharge(c);
        updateColor();
    }

    public Vector2D getPos() {
        return new Vector2D(super.getCenterX(), super.getCenterY());
    }

    private Vector2D getCoords() {
        return charge.getPosition();
    }

    private void setCoords(Vector2D pos) {
        super.setCenterX(pos.getX());
        super.setCenterY(pos.getY());
        App.model.moveParticle(charge, pos);
        this.charge.setPosition(pos);

        if (compute != null && display != null) {
            this.compute.run();
            this.display.run();
        }
    }

    private boolean select() {
        if (this.select != null) return this.select.apply(this);
        return false;
    }

    private void eventHandler(Movable obj) {
        if (!select()) {
            if (Movable.getCurrentObject() == obj) {
                Movable.setCurrentObject(null);
                super.toBack();
                if (this.checkbounds != null) this.checkbounds.apply(this);
                if (compute != null && display != null) {
                    this.compute.run();
                    this.display.run();
                }
                return;
            }
            Movable.setCurrentObject(obj);
            super.toFront();
        }
    }

    @Override
    public void reposition(Vector2D prevOffset, Vector2D offset) {
        Vector2D pos = movable.getCoords().sub(prevOffset).add(offset);
        super.setCenterX(pos.getX());
        super.setCenterY(pos.getY());
        App.model.moveParticle(App.model.findID(charge), pos);
        this.charge.setPosition(pos);
    }
}
