package elements;

import electrostatics.PotentialFieldLine;

import java.util.ArrayList;

public class Potential extends DrawLine {
    private static ArrayList<Field> objs = new ArrayList<>();
    private static double weight = 1;
    private static double[] style ={1, 0};
    public Potential(PotentialFieldLine pfl) {
        super(pfl);
    }

    public static void setLineWeight(double weight) {
        Potential.weight = weight;
        for (Field f : objs) f.setStrokeWidth(Potential.weight);
    }

    public static void setLineStyle(double[] style) {
        Potential.style = style;
        for (Field f : objs) {
            f.getStrokeDashArray().clear();
            f.getStrokeDashArray().addAll(Potential.style[0], Potential.style[1]);
        }
    }
}
