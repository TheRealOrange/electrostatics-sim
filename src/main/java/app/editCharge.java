package app;

import elements.Charge;

public abstract class editCharge {
    public static void editCharge(Charge c, double charge) {
        c.setCharge(charge);
    }

    public static void editRadius(Charge c, double radius) {
        c.setChargeRadius(radius);
    }
}
