package ac.grim.grimac.utils.vector;

import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.DoubleVector;

public class VectorOperationsJava18 implements VectorOperations {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    @Override
    public double[] add(double[] a, double[] b) {
        var v1 = DoubleVector.fromArray(SPECIES, a, 0);
        var v2 = DoubleVector.fromArray(SPECIES, b, 0);
        var result = v1.add(v2);
        return result.toArray();
    }
}
