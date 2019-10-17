package app;

import elements.Charge;
import elements.Field;
import elements.Potential;

import java.util.ArrayList;

public abstract class Temp {
    public static boolean reload = false;

    public static ArrayList<Field> fieldlines;
    public static ArrayList<Potential> potentiallines;
    public static Charge selected;

    public static boolean updating;

    public static boolean render;
    public static boolean mode;

    public static boolean drawfield;
    public static boolean drawpotential;

    public static int e_solver;
    public static int p_solver;

    public static int e_style;
    public static int p_style;

    public static int e_weight;
    public static int p_weight;

    public static int locale;
}
