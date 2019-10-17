package elements;

import electrostatics.ElectricFieldLine;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Field extends DrawLine {
    private static ArrayList<Field> objs = new ArrayList<>();
    private static int weight = 1;
    private static double[] style ={1, 0};
    private static int style_num = 0;
    private static Paint color = Color.BLACK;
    public Field(ElectricFieldLine efl) {
        super(efl);
        super.setStrokeWidth(weight);
        super.getStrokeDashArray().clear();
        super.getStrokeDashArray().addAll(style[0], style[1]);
        super.setStroke(color);
        objs.add(this);
    }

    public static void setLineWeight(int weight) {
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

    public static void setLineColor(Paint p) {
        Field.color = p;
        for (Field f : objs) {
            f.setStroke(p);
        }
    }

    public static int getWeight() {
        return weight;
    }

    public static int getStyle_num() {
        return style_num;
    }

    public static void setStyle_num(int style_num) {
        Field.style_num = style_num;
    }

    public static void clear() {
        objs = new ArrayList<>();
    }
}
