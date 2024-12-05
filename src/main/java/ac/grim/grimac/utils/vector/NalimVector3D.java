package ac.grim.grimac.utils.vector;

import one.nalim.Library;
import one.nalim.Link;
import one.nalim.Linker;
import org.jetbrains.annotations.NotNull;

@Library("vector_ops")
public class NalimVector3D implements Vector3D {

    static {
        Linker.linkClass(NalimVector3D.class);
    }

    double[] loc = new double[3];

    public NalimVector3D(double x, double y, double z) {
        loc[0] = x;
        loc[1] = y;
        loc[2] = z;
    }

    @Override
    public double getX() {
        return loc[0];
    }

    @Override
    public double getY() {
        return loc[1];
    }

    @Override
    public double getZ() {
        return loc[2];
    }

    @Override
    public Vector3D setX(double x) {
        loc[0] = x;
        return this;
    }

    @NotNull
    public Vector3D setY(double y) {
        loc[1] =y;
        return this;
    }

    @Override
    public Vector3D setZ(double z) {
        loc[2] = z;
        return this;
    }

    @Override
    public double length() {
        return Math.sqrt(loc[0] * loc[0] + loc[1] * loc[1] + loc[2] * loc[2]);
    }

    @Override
    public double lengthSquared() {
        return loc[0] * loc[0] + loc[1] * loc[1] + loc[2] * loc[2];
    }

    @Override
    public @NotNull Vector3D multiply(double m) {
        loc[0] *= m;
        loc[1] *= m;
        loc[2] *= m;
        return this;
    }

    @Override
    public @NotNull Vector3D normalize() {
        double length = this.length();
        loc[0] /= length;
        loc[1] /= length;
        loc[2] /= length;
        return this;
    }

    @Link(name = "cross_product")
    private static native void crossProduct(double[] a, double[] b);

    @Override
    public @NotNull Vector3D crossProduct(@NotNull Vector3D o) {
        crossProduct(this.loc, ((NalimVector3D) o).loc);
        return this;
    }

    @NotNull
    public Vector3D add(@NotNull Vector3D vec) {
        loc[0] += vec.getX();
        loc[1] += vec.getY();
        loc[2] += vec.getZ();
        return this;
    }

    @NotNull
    public Vector3D subtract(@NotNull Vector3D vec) {
        loc[0] -= vec.getX();
        loc[1] -= vec.getY();
        loc[2] -= vec.getZ();
        return this;
    }

    @NotNull
    public Vector3D multiply(@NotNull Vector3D vec) {
        loc[0] *= vec.getX();
        loc[1] *= vec.getY();
        loc[2] *= vec.getZ();
        return this;
    }

    @Override
    public double distance(@NotNull Vector3D o) {
        return Math.sqrt(distanceSquared(o));
    }

    @Override
    public double distanceSquared(@NotNull Vector3D o) {
        return (loc[0] - o.getX()) * (loc[0] - o.getX())
                + (loc[1] - o.getY()) * (loc[1] - o.getY())
                + (loc[2] - o.getZ()) * (loc[2] - o.getZ());
    }

    @Override
    public double dot(@NotNull Vector3D other) {
        return loc[0] * other.getX() + loc[1] * other.getY() + loc[2] * other.getZ();
    }

    @NotNull
    public Vector3D clone() {
        try {
            return (Vector3D) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

}