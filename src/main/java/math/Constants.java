package math;

public abstract class Constants {
    private static long precision = 15;

    public static long getPrecision() {
        return precision;
    }

    public static void setPrecision(long precision) {
        Constants.precision = precision;
    }
}
