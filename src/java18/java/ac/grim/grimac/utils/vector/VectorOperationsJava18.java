package ac.grim.grimac.utils.vector;

import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.DoubleVector;

public class VectorOperationsJava18 implements VectorOperations {

    @Override
    public Vector3D newVector(double x, double y, double z) {
        return new SIMDVector3D(x, y, z);
    }
}
