package ac.grim.grimac.utils.vector;

public class SIMDVector3D implements Vector3D {

    private double[] coords = new double[3];

    public SIMDVector3D(double x, double y, double z) {
        coords[0] = x;
        coords[1] = y;
        coords[2] = z;
    }

    @Override
    public double getX() {
        return coords[0];
    }

    @Override
    public double getY() {
        return coords[1];
    }

    @Override
    public double getZ() {
        return coords[2];
    }
}
