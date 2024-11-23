package ac.grim.grimac.utils.vector;

public class BasicVectorOps implements VectorOperations {
    @Override
    public double[] add(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }
}
