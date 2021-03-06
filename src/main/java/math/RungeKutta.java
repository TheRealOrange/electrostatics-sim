package math;

import java.util.function.BiFunction;

public class RungeKutta implements Cloneable {
    BiFunction<Double, Vector2D, Vector2D> func;
    int stages;
    double[] weights;
    double[] nodes;
    double[][] coefficients;
    Vector2D[] k;

    public RungeKutta(BiFunction<Double, Vector2D, Vector2D> func, int stages, double[] weights, double[] nodes, double[][] coefficients) {
        this.func = func;
        this.stages = stages;
        this.weights = new double[weights.length];
        this.nodes = new double[nodes.length];
        this.coefficients = new double[stages-1][1];
        for (int i = 0;i < this.stages; ++i) {
            this.weights[i] = weights[i];
            this.nodes[i] = nodes[i];

            if (i > 0) {
				int coeffcientnum = i - 1;
                this.coefficients[coeffcientnum] = new double[i];

                System.arraycopy(coefficients[coeffcientnum], 0, this.coefficients[coeffcientnum], 0, i);
            }
        }
        this.weights = weights;
        this.nodes = nodes;
        this.coefficients = coefficients;
        this.k = new Vector2D[stages];
    }

    public Vector2D step(Vector2D curr, double t, double h) {
        Vector2D sum = new Vector2D();

        for (int i = 0;i < this.stages; ++i) {
            Vector2D n = new Vector2D();
            for (int j = 0; j < i; ++j) n = n.add(k[j].mul(coefficients[i-1][j]));

            k[i] = this.func.apply(t + nodes[i] * h, curr.add(n.mul(h)));
            sum = sum.add(k[i].mul(weights[i]));
        }

        return curr.add(sum.mul(h));
    }

    public Vector2D step(Vector2D curr, double t, double[] h, double[] next_h, double tolerance) {
        return step(curr, t, h[0]);
    }

    public RungeKutta clone() {
        return new RungeKutta(func, stages, weights, nodes, coefficients);
    }

    public BiFunction<Double, Vector2D, Vector2D> getFunc() {
        return func;
    }

    static final double[] eulerWeights = new double[]{1};
    static final double[] eulerNodes = new double[]{0};

    public static class Euler extends RungeKutta{
        public Euler(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 1, eulerWeights, eulerNodes, null);
        }
    }

    static final double[] midpointWeights = new double[]{0, 1};
    static final double[] midpointNodes = new double[]{0, 1.0/2.0};
    static final double[][] midpointCoeff = new double[][]{{1.0/2.0}};

    public static class Midpoint extends RungeKutta{
        public Midpoint(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 2, midpointWeights, midpointNodes, midpointCoeff);
        }
    }

    static final double[] heunWeights = new double[]{1.0/2.0, 1.0/2.0};
    static final double[] heunNodes = new double[]{0, 1};
    static final double[][] heunCoeff = new double[][]{{1}};

    public static class Heun extends RungeKutta{
        public Heun(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 2, heunWeights, heunNodes, heunCoeff);
        }
    }

    static final double[] ralstonWeights = new double[]{1.0/4.0, 3.0/4.0};
    static final double[] ralstonNodes = new double[]{0, 2.0/3.0};
    static final double[][] ralstonCoeff = new double[][]{{2.0/3.0}};

    public static class Ralston extends RungeKutta{
        public Ralston(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 2, ralstonWeights, ralstonNodes, ralstonCoeff);
        }
    }

    static final double[] rk4Weights = new double[]{1.0/6.0, 1.0/3.0, 1.0/3.0, 1.0/6.0};
    static final double[] rk4Nodes = new double[]{0, 1.0/2.0, 1.0/2.0, 1};
    static final double[][] rk4Coeff = new double[][]{{1.0/2.0}, {0,1.0/2.0}, {0, 0, 1}};

    public static class RK4 extends RungeKutta{
        public RK4(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 4, rk4Weights, rk4Nodes, rk4Coeff);
        }
    }

    static final double[] ssprk3Weights = new double[]{1.0/6.0, 1.0/6.0, 2.0/3.0};
    static final double[] ssprk3Nodes = new double[]{0, 1, 1.0/2.0};
    static final double[][] ssprk3Coeff = new double[][]{{1}, {1.0/4.0, 1.0/4.0}};

    public static class SSPRK3 extends RungeKutta{
        public SSPRK3(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 3, ssprk3Weights, ssprk3Nodes, ssprk3Coeff);
        }
    }

    static final double[] ralston4Weights = new double[]{0.17476028, -0.55148066, 1.20553560, 0.17118478};
    static final double[] ralston4Nodes = new double[]{0, 0.4, 0.45573725, 1};
    static final double[][] ralston4Coeff = new double[][]{{0.4}, {0.29697761, 0.15875964}, {0.21810040, -3.05096516, 3.83286476}};

    public static class Ralston4 extends RungeKutta{
        public Ralston4(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 4, ralston4Weights, ralston4Nodes, ralston4Coeff);
        }
    }

    static final double[] rk3_8Weights = new double[]{1.0/8.0, 3.0/8.0, 3.0/8.0, 1.0/8.0};
    static final double[] rk3_8Nodes = new double[]{0, 1.0/3.0, 2.0/3.0, 1};
    static final double[][] rk3_8Coeff = new double[][]{{1.0/3.0}, {-1.0/3.0,1}, {1, -1, 1}};

    public static class RK3_8 extends RungeKutta{
        public RK3_8(BiFunction<Double, Vector2D, Vector2D> func) {
            super(func, 4, rk3_8Weights, rk3_8Nodes, rk3_8Coeff);
        }
    }
}
