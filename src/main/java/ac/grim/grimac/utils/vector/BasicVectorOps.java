package ac.grim.grimac.utils.vector;

public class BasicVectorOps implements VectorOperations {

    @Override
    public Vector3D newVector(double x, double y, double z) {
        return new ScalarVector3D(x, y, z);
    }
}
