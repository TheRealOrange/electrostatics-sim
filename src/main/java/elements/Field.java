package elements;

import electrostatics.ElectricFieldLine;

import java.util.ArrayList;

public class Field extends DrawLine {
    private static ArrayList<Field> objs = new ArrayList<>();
    private static double weight = 1;
    private static double[] style = {1, 0};
    public Field(ElectricFieldLine efl) {
        super(efl);
        super.setStrokeWidth(weight);
        super.getStrokeDashArray().clear();
        super.getStrokeDashArray().addAll(style[0], style[1]);
        objs.add(this);
    }

    public static void setLineWeight(double weight) {
        Field.weight = weight;
        for (Field f : objs) f.setStrokeWidth(Field.weight);
    }

    public static void setLineStyle(double[] style) {
        Field.style = style;
        for (Field f : objs) {
            f.getStrokeDashArray().clear();
            f.getStrokeDashArray().addAll(Field.style[0], Field.style[1]);
        }
    }
}
