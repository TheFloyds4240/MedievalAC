package ac.grim.grimac.utils.vector;

import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.DoubleVector;

public class VectorOperationsJava18 implements VectorOperations {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;
    private static final VectorMask<Double> LENGTH_3_ARRAY_MASK = SPECIES.indexInRange(0, 3);

    @Override
    public double[] add(double[] dest, double[] src) {
        // Process the first 3 elements using a mask to handle the partial vector
        var v1 = DoubleVector.fromArray(SPECIES, dest, 0, LENGTH_3_ARRAY_MASK);
        var v2 = DoubleVector.fromArray(SPECIES, src, 0, LENGTH_3_ARRAY_MASK);
        var result = v1.add(v2);
        result.intoArray(dest, 0, LENGTH_3_ARRAY_MASK);
        return dest; // Return the modified destination array
    }
}
