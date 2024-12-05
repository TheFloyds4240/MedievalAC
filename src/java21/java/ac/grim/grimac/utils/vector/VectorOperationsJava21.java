package ac.grim.grimac.utils.vector;


public class VectorOperationsJava21 implements VectorOperations {

    @Override
    public Vector3D newVector(double x, double y, double z) {
        return new Java21SIMDVector3D(x, y, z);
    }
}
