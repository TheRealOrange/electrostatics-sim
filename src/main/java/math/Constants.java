package math;

public abstract class Constants {
    private static long precision = 20;

    public static long getPrecision() {
        return precision;
    }

    public static void setPrecision(long precision) {
        Constants.precision = precision;
    }
}
