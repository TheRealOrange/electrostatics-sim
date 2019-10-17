package math;

import java.util.function.BiFunction;

public class AdaptiveRungeKutta extends RungeKutta implements Cloneable {
    private double maxstep;
    private double minstep;
    private int order;
    private double[] lweights;

    public AdaptiveRungeKutta(BiFunction<Double, Vector2D, Vector2D> func, int stages, int order, double[] weights, double[] lweights, double[] nodes, double[][] coefficients, double minstep, double maxstep) {
        super(func, stages, weights, nodes, coefficients);
        this.maxstep = maxstep;
        this.minstep = minstep;
        this.order = order;
        this.lweights = new double[super.stages];
        System.arraycopy(lweights, 0, this.lweights, 0, super.stages);
    }

    public AdaptiveRungeKutta(BiFunction<Double, Vector2D, Vector2D> func, int stages, int order, double[] weights, double[] lweights, double[] nodes, double[][] coefficients) {
        this(func, stages, order, weights, lweights, nodes, coefficients, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public Vector2D step(Vector2D curr, double t, double[] h, double[] next_h, double tolerance) {
        for (int i = 0;; ++i) {
            Vector2D highsum = new Vector2D();
            Vector2D lowsum = new Vector2D();

            for (int j = 0;j < super.stages;++j) {
                Vector2D sum = new Vector2D();
                for (int k = 0;k < j; ++k) sum = sum.add(super.k[k].mul(super.coefficients[j - 1][k]));

                super.k[j] = super.func.apply(t + h[0] * super.nodes[j], curr.add(sum.mul(h[0])));

                highsum = highsum.add(super.k[j].mul(super.weights[j]));
                lowsum  = lowsum.add(super.k[j].mul(lweights[j]));
            }

            double error = Math.abs(highsum.sub(lowsum).magnitude()*h[0]);

            if (error <= tolerance || i > 1000 || Math.abs(h[0]) <= minstep) {
                Vector2D result = curr.add(highsum.mul(h[0]));

                if (error <= Double.MIN_VALUE) next_h[0] = 2*h[0];
                else if (error < tolerance) next_h[0] = h[0] * 0.9 * Math.pow(tolerance/error, 1/(double)(this.order+1));
                else next_h[0] = h[0];

                if (Math.abs(next_h[0]) < minstep) next_h[0] = minstep*Math.signum(next_h[0]);
                else if (Math.abs(next_h[0]) > maxstep) next_h[0] = maxstep*Math.signum(next_h[0]);

                return result;
            }

            h[0] *= 0.9 * Math.pow(tolerance/error, 1/(double)this.order);
        }
    }

    public AdaptiveRungeKutta clone() {
        return new AdaptiveRungeKutta(this.func, super.stages, this.order, super.weights, this.lweights, super.nodes, super.coefficients);
    }

    public double getMaxstep() {
        return maxstep;
    }

    public void setMaxstep(double maxstep) {
        this.maxstep = maxstep;
    }

    public double getMinstep() {
        return minstep;
    }

    public void setMinstep(double minstep) {
        this.minstep = minstep;
    }

    private static final double[] heuneulerLWeights = new double[]{1, 0};

    private static class HeunEuler extends AdaptiveRungeKutta {
        public HeunEuler(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 2, 2, RungeKutta.heunWeights, heuneulerLWeights, RungeKutta.heunNodes, RungeKutta.heunCoeff);
        }
    }

    private static final double[] bogackishampineWeights = new double[]{2.0/9.0, 1.0/3.0, 4.0/9.0, 0};
    private static final double[] bogackishampineLWeights = new double[]{7.0/24.0, 1.0/ 4.0, 1.0/ 3.0, 1.0/ 8.0};
    private static final double[] bogackishampineNodes = new double[]{0, 1.0/2.0, 3.0/4.0, 1};
    private static final double[][] bogackishampineCoeff = new double[][]{{1.0/2.0}, {0, 3.0/4.0}, {2.0/9.0, 1.0/3.0, 4.0/9.0}};

    public static class BogackiShampine extends AdaptiveRungeKutta {
        public BogackiShampine(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 2, 2, bogackishampineWeights, bogackishampineLWeights, bogackishampineNodes, bogackishampineCoeff);
        }
    }

    private static final double[] fehlbergWeights = {16.0/135.0, 0, 6656.0/12825.0, 28561.0/56430.0, -9.0/50.0, 2.0/55.0};
    private static final double[] fehlbergLWeights = {25.0/216.0, 0, 1408.0/2565.0, 2197.0/4104.0, -1.0/5.0, 0};
    private static final double[] fehlbergNodes = {0, 1.0/4.0, 3.0/8.0, 12.0/13.0, 1, 1.0/2.0};
    private static final double[][] fehlbergCoeff = {{1.0/4.0}, {3.0/32.0, 9.0/32.0}, {1932.0/2197.0, -7200.0/2197.0, 7296.0/2197.0}, {439.0/216.0, -8, 3680.0/513.0, -845.0/4104.0}, {-8.0/27.0, 2, -3544.0/2565.0, 1859.0/4104.0, -11.0/40.0}};

    public static class FehlBerg extends AdaptiveRungeKutta {
        public FehlBerg(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 6, 5, fehlbergWeights, fehlbergLWeights, fehlbergNodes, fehlbergCoeff);
        }
    }

    private static final double[] cashkarpWeights = new double[]{37.0/378.0, 0, 250.0/621.0, 125.0/594.0, 0, 512.0/1771.0};
    private static final double[] cashkarpLWeights = new double[]{2825.0/27648.0, 0, 18575.0/48384.0, 13525.0/55296.0, 277.0/14336.0, 1.0/4.0};
    private static final double[] cashkarpNodes = new double[]{0, 1.0/5.0, 3.0/10.0, 3.0/5.0, 1, 7.0/8.0};
    private static final double[][] cashkarpCoeff = new double[][]{{1.0/ 5.0}, {3.0/40.0, 9.0/40.0}, {3.0/10.0, -9.0/10.0, 6.0/5.0}, {-11.0/54.0, 5.0/2.0, -70.0/27.0, 35.0/27.0}, {1631.0/55296.0, 175.0/512.0, 575.0/13824.0, 44275.0/110592.0, 253.0/4096.0}};

    public static class CashKarp extends AdaptiveRungeKutta {
        public CashKarp(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 6, 5, cashkarpWeights, cashkarpLWeights, cashkarpNodes, cashkarpCoeff);
        }
    }

    private static final double[] dormandprinceWeights = new double[]{35.0/384.0, 0, 500.0/1113.0, 125.0/192.0, -2187.0/6784.0, 11.0/84.0, 0};
    private static final double[] dormandprinceLWeights = new double[]{71.0/57600.0, 0, -71.0/16695.0, 71.0/1920.0, -17253.0/339200.0, 22.0/525.0, -1.0/40.0};
    private static final double[] dormandprinceNodes = new double[]{0, 1.0/5.0, 3.0/10.0, 4.0/5.0, 8.0/9.0, 1, 1};
    private static final double[][] dormandprinceCoeff = new double[][]{{1.0/5.0}, {3.0/40.0, 9.0/40.0}, {44.0/45.0, -56.0/15.0, 32.0/9.0}, {19372.0/6561.0, -25360.0/2187.0, 64448.0/6561.0, -212.0/729.0}, {9017.0/3168.0, -355.0/33.0, 46732.0/5247.0, 49.0/176.0, -5103.0/18656.0}, {35.0/384.0, 0, 500.0/ 1113.0, 125.0/1920., -2187.0/6784.0, 11.0/84.0}};

    public static class DormandPrince extends AdaptiveRungeKutta {
        public DormandPrince(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 7, 5, dormandprinceWeights, dormandprinceLWeights, dormandprinceNodes, dormandprinceCoeff);
        }
    }
}
