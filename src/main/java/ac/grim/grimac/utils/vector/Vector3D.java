package ac.grim.grimac.utils.vector;

import org.jetbrains.annotations.NotNull;

public interface Vector3D {
    double getX();
    double getY();
    double getZ();

    Vector3D setX(double x);
    Vector3D setY(double y);
    Vector3D setZ(double z);

    double length();
    double lengthSquared();

    @NotNull Vector3D multiply(double m);
    @NotNull Vector3D normalize();
    @NotNull Vector3D crossProduct(@NotNull Vector3D o);
    @NotNull Vector3D add(@NotNull Vector3D o);
    @NotNull Vector3D subtract(@NotNull Vector3D o);
    @NotNull Vector3D multiply(@NotNull Vector3D o);
    double distance(@NotNull Vector3D o);
    double distanceSquared(@NotNull Vector3D o);
    double dot(@NotNull Vector3D o);

    @NotNull Vector3D clone();
}
