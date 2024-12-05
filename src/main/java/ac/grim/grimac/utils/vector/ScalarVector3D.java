package ac.grim.grimac.utils.vector;

import org.jetbrains.annotations.NotNull;

public class ScalarVector3D implements Vector3D {

    double x, y ,z;

    public ScalarVector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public Vector3D setX(double x) {
        this.x = x;
        return this;
    }

    @NotNull
    public Vector3D setY(double y) {
        this.y =y;
        return this;
    }

    @Override
    public Vector3D setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    @Override
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    @Override
    public @NotNull Vector3D multiply(double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    @Override
    public @NotNull Vector3D normalize() {
        double length = this.length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    @Override
    public @NotNull Vector3D crossProduct(@NotNull Vector3D o) {
        double newX = this.y * o.getZ() - o.getY() * this.z;
        double newY = this.z * o.getX() - o.getZ() * this.x;
        double newZ = this.x * o.getY() - o.getX() * this.y;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        return this;
    }

    @NotNull
    public Vector3D add(@NotNull Vector3D vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    @NotNull
    public Vector3D subtract(@NotNull Vector3D vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }

    @NotNull
    public Vector3D multiply(@NotNull Vector3D vec) {
        this.x *= vec.getX();
        this.y *= vec.getY();
        this.z *= vec.getZ();
        return this;
    }

    @Override
    public double distance(@NotNull Vector3D o) {
        double dSquared = this.distanceSquared(o);
        return dSquared * dSquared;
    }

    @Override
    public double distanceSquared(@NotNull Vector3D o) {
        return (this.x - o.getX()) + (this.y - o.getY()) + (this.z - o.getZ());
    }

    @Override
    public double dot(@NotNull Vector3D other) {
        return this.x * other.getX() + this.y * other.getY() + this.z * other.getZ();
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
