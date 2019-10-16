package elements;

import electrostatics.PotentialFieldLine;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Potential extends DrawLine {
    private static ArrayList<Potential> objs = new ArrayList<>();
    private static int weight = 1;
    private static double[] style ={1, 0};
    private static int style_num = 0;
    private static Paint color = Color.BLACK;
    public Potential(PotentialFieldLine pfl) {
        super(pfl);
        super.setStrokeWidth(weight);
        super.getStrokeDashArray().clear();
        super.getStrokeDashArray().addAll(style[0], style[1]);
        super.setStroke(color);
        objs.add(this);
    }

    public static void setLineWeight(int weight) {
        Potential.weight = weight;
        for (Potential f : objs) f.setStrokeWidth(Potential.weight);
    }

    public static void setLineStyle(double[] style) {
        Potential.style = style;
        for (Potential f : objs) {
            f.getStrokeDashArray().clear();
            f.getStrokeDashArray().addAll(Potential.style[0], Potential.style[1]);
        }
    }

    public static void setLineColor(Paint p) {
        Potential.color = p;
        for (Potential f : objs) {
            f.setFill(p);
        }
    }

    public static int getWeight() {
        return weight;
    }

    public static int getStyle_num() {
        return style_num;
    }

    public static void setStyle_num(int style_num) {
        Potential.style_num = style_num;
    }

    public static void clear() {
        objs = new ArrayList<>();
    }
}
