package elements;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Polyline;
import javafx.util.Callback;
import math.Vector2D;

import java.util.ArrayList;

public class LineAnimation extends AnimationTimer {
    Polyline p = new Polyline();
    int i = 0;
    int step;
    ArrayList<Vector2D> points;
    private Callback<LineAnimation, Void> onComplete;

    public LineAnimation(ArrayList<Vector2D> points, int step, Callback<LineAnimation, Void> onComplete) {
        this.points = points;
        this.step = step;
        this.onComplete = onComplete;
    }

    @Override
    public void handle(long now) {
        if (i < points.size()) {
            p.getPoints().addAll(points.get(i).getX(), points.get(i).getY());
            i += this.step;
            if (i > points.size()) {
                p.getPoints().addAll(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY());
                onComplete.call(this);
                this.stop();
            }
        }
    }

    public Polyline getP() {
        return p;
    }

    public void setP(Polyline p) {
        this.p = p;
    }
}
