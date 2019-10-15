package math;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import java.util.function.BiFunction;

public class AdaptiveRungeKutta extends RungeKutta {
    private Apfloat maxstep;
    private Apfloat minstep;
    private int order;
    private Apfloat[] lweights;

    public AdaptiveRungeKutta(BiFunction<Apfloat, Vector2D, Vector2D> func, int stages, int order, double[] weights, double[] lweights, double[] nodes, double[][] coefficients) {
        super(func, stages, weights, nodes, coefficients);
        this.maxstep = new Apfloat(Double.MAX_VALUE, Constants.getPrecision());
        this.minstep = new Apfloat(Double.MIN_VALUE, Constants.getPrecision());
        this.order = order;
        this.lweights = new Apfloat[super.stages];
        for (int i = 0;i < super.stages;++i) this.lweights[i] = new Apfloat(lweights[i], Constants.getPrecision());
    }

    public AdaptiveRungeKutta(BiFunction<Apfloat, Vector2D, Vector2D> func, int stages, int order, Apfloat[] weights, Apfloat[] lweights, Apfloat[] nodes, Apfloat[][] coefficients, Apfloat maxstep, Apfloat minstep) {
        super(func, stages, weights, nodes, coefficients);
        this.maxstep = maxstep.add(new Apfloat(0, Constants.getPrecision()));
        this.minstep = minstep.add(new Apfloat(0, Constants.getPrecision()));
        this.order = order;
        this.lweights = new Apfloat[super.stages];
        for (int i = 0;i < super.stages;++i) this.lweights[i] = lweights[i].add(new Apfloat(0, Constants.getPrecision()));
    }

    public AdaptiveRungeKutta(BiFunction<Apfloat, Vector2D, Vector2D> func, int stages, int order, Apfloat[] weights, Apfloat[] lweights, Apfloat[] nodes, Apfloat[][] coefficients) {
        this(func, stages, order, weights, lweights, nodes, coefficients, new Apfloat(Double.MAX_VALUE, Constants.getPrecision()), new Apfloat(Double.MIN_VALUE, Constants.getPrecision()));
    }

    @Override
    public Vector2D step(Vector2D curr, Apfloat t, Apfloat[] h, Apfloat[] next_h, Apfloat tolerance) {
        for (int i = 0;; ++i) {
            Vector2D highsum = new Vector2D();
            Vector2D lowsum = new Vector2D();

            for (int j = 0;j < super.stages;++j) {
                Vector2D sum = new Vector2D();
                for (int k = 0;k < j; ++k) sum = sum.add(super.k[k].mul(super.coefficients[j - 1][k]));

                super.k[j] = super.func.apply(t.add(h[0].multiply(super.nodes[j])), curr.add(sum.mul(h[0])));

                highsum = highsum.add(super.k[j].mul(super.weights[j]));
                lowsum  = lowsum.add(super.k[j].mul(lweights[j]));
            }

            Apfloat error = ApfloatMath.abs(highsum.sub(lowsum).magnitude().multiply(h[0]));

            if (error.compareTo(tolerance) < 0 || i > 10000 || ApfloatMath.abs(h[0]).compareTo(minstep) <= 0) {
                Vector2D result = curr.add(highsum.mul(h[0]));

                if (error.compareTo(new Apfloat(Double.MIN_VALUE)) <= 0) next_h[0] = h[0].multiply(new Apfloat(2));
                else if (error.compareTo(tolerance) < 0) next_h[0] = h[0].multiply((new Apfloat(0.9, Constants.getPrecision())).multiply(ApfloatMath.pow(tolerance.divide(error), new Apfloat(1/(this.order+1), Constants.getPrecision()))));
                else next_h[0] = h[0].add(new Apfloat(0));

                if (ApfloatMath.abs(next_h[0]).compareTo(minstep) < 0) next_h[0] = minstep.multiply(new Apfloat(next_h[0].signum(), Constants.getPrecision()));
                else if (ApfloatMath.abs(next_h[0]).compareTo(maxstep) < 0) next_h[0] = maxstep.multiply(new Apfloat(next_h[0].signum(), Constants.getPrecision()));

                return result;
            }

            h[0] = h[0].multiply((new Apfloat(0.9, Constants.getPrecision())).multiply(ApfloatMath.pow(tolerance.divide(error), new Apfloat(1/(this.order+1), Constants.getPrecision()))));
        }
    }

    public Apfloat getMaxstep() {
        return maxstep;
    }

    public void setMaxstep(Apfloat maxstep) {
        this.maxstep = maxstep.add(new Apfloat(0));
    }

    public Apfloat getMinstep() {
        return minstep;
    }

    public void setMinstep(Apfloat minstep) {
        this.minstep = minstep.add(new Apfloat(0));
    }

    static final double[] heuneulerLWeights = new double[]{1, 0};

    public static class HeunEuler extends AdaptiveRungeKutta {
        public HeunEuler(BiFunction<Apfloat, Vector2D, Vector2D> func) {
            super(func, 2, 2, RungeKutta.heunWeights, heuneulerLWeights, RungeKutta.heunNodes, RungeKutta.heunCoeff);
        }
    }

    static final double[] bogackishampineWeights = new double[]{2.0/9.0, 1.0/3.0, 4.0/9.0, 0};
    static final double[] bogackishampineLWeights = new double[]{7.0/24.0, 1.0/ 4.0, 1.0/ 3.0, 1.0/ 8.0};
    static final double[] bogackishampineNodes = new double[]{0, 1.0/2.0, 3.0/4.0, 1};
    static final double[][] bogackishampineCoeff = new double[][]{{1.0/2.0}, {0, 3.0/4.0}, {2.0/9.0, 1.0/3.0, 4.0/9.0}};

    public static class BogackiShampine extends AdaptiveRungeKutta {
        public BogackiShampine(BiFunction<Apfloat, Vector2D, Vector2D> func) {
            super(func, 2, 2, bogackishampineWeights, bogackishampineLWeights, bogackishampineNodes, bogackishampineCoeff);
        }
    }

    static final double[] fehlbergWeights = {16.0/135.0, 0, 6656.0/12825.0, 28561.0/56430.0, -9.0/50.0, 2.0/55.0};
    static final double[] fehlbergLWeights = {25.0/216.0, 0, 1408.0/2565.0, 2197.0/4104.0, -1.0/5.0, 0};
    static final double[] fehlbergNodes = {0, 1.0/4.0, 3.0/8.0, 12.0/13.0, 1, 1.0/2.0};
    static final double[][] fehlbergCoeff = {{1.0/4.0}, {3.0/32.0, 9.0/32.0}, {1932.0/2197.0, -7200.0/2197.0, 7296.0/2197.0}, {439.0/216.0, -8, 3680.0/513.0, -845.0/4104.0}, {-8.0/27.0, 2, -3544.0/2565.0, 1859.0/4104.0, -11.0/40.0}};

    public static class FehlBerg extends AdaptiveRungeKutta {
        public FehlBerg(BiFunction<Apfloat, Vector2D, Vector2D> func) {
            super(func, 6, 5, fehlbergWeights, fehlbergLWeights, fehlbergNodes, fehlbergCoeff);
        }
    }

    static final double[] cashkarpWeights = new double[]{37.0/378.0, 0, 250.0/621.0, 125.0/594.0, 0, 512.0/1771.0};
    static final double[] cashkarpLWeights = new double[]{2825.0/27648.0, 0, 18575.0/48384.0, 13525.0/55296.0, 277.0/14336.0, 1.0/4.0};
    static final double[] cashkarpNodes = new double[]{0, 1.0/5.0, 3.0/10.0, 3.0/5.0, 1, 7.0/8.0};
    static final double[][] cashkarpCoeff = new double[][]{{1.0/ 5.0}, {3.0/40.0, 9.0/40.0}, {3.0/10.0, -9.0/10.0, 6.0/5.0}, {-11.0/54.0, 5.0/2.0, -70.0/27.0, 35.0/27.0}, {1631.0/55296.0, 175.0/512.0, 575.0/13824.0, 44275.0/110592.0, 253.0/4096.0}};

    public static class CashKarp extends AdaptiveRungeKutta {
        public CashKarp(BiFunction<Apfloat, Vector2D, Vector2D> func) {
            super(func, 6, 5, cashkarpWeights, cashkarpLWeights, cashkarpNodes, cashkarpCoeff);
        }
    }

    static final double[] dormandprinceWeights = new double[]{35.0/384.0, 0, 500.0/1113.0, 125.0/192.0, -2187.0/6784.0, 11.0/84.0, 0};
    static final double[] dormandprinceLWeights = new double[]{71.0/57600.0, 0, -71.0/16695.0, 71.0/1920.0, -17253.0/339200.0, 22.0/525.0, -1.0/40.0};
    static final double[] dormandprinceNodes = new double[]{0, 1.0/5.0, 3.0/10.0, 4.0/5.0, 8.0/9.0, 1, 1};
    static final double[][] dormandprinceCoeff = new double[][]{{1.0/5.0}, {3.0/40.0, 9.0/40.0}, {44.0/45.0, -56.0/15.0, 32.0/9.0}, {19372.0/6561.0, -25360.0/2187.0, 64448.0/6561.0, -212.0/729.0}, {9017.0/3168.0, -355.0/33.0, 46732.0/5247.0, 49.0/176.0, -5103.0/18656.0}, {35.0/384.0, 0, 500.0/ 1113.0, 125.0/1920., -2187.0/6784.0, 11.0/84.0}};

    public static class DormandPrince extends AdaptiveRungeKutta {
        public DormandPrince(BiFunction<Apfloat, Vector2D, Vector2D> func) {
            super(func, 7, 5, dormandprinceWeights, dormandprinceLWeights, dormandprinceNodes, dormandprinceCoeff);
        }
    }
}
