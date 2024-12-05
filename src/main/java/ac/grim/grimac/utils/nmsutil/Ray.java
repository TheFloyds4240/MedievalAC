package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.vector.Vector3D;
import ac.grim.grimac.utils.vector.Vector3D;

import static ac.grim.grimac.utils.vector.VectorFactory.newVector3D;
import static java.lang.Math.toRadians;

// Copied directly from Hawk
public class Ray implements Cloneable {

    private Vector3D origin;
    private Vector3D direction;

    public Ray(Vector3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Ray(GrimPlayer player, double x, double y, double z, float xRot, float yRot) {
        this.origin = newVector3D(x, y, z);
        this.direction = calculateDirection(player, xRot, yRot);
    }

    // Account for FastMath by using player's trig handler
    // Copied from hawk which probably copied it from NMS
    public static Vector3D calculateDirection(GrimPlayer player, float xRot, float yRot) {
        Vector3D vector = newVector3D();
        float rotX = (float) toRadians(xRot);
        float rotY = (float) toRadians(yRot);
        vector.setY(-player.trigHandler.sin(rotY));
        double xz = player.trigHandler.cos(rotY);
        vector.setX(-xz * player.trigHandler.sin(rotX));
        vector.setZ(xz * player.trigHandler.cos(rotX));
        return vector;
    }

    public Ray clone() {
        Ray clone;
        try {
            clone = (Ray) super.clone();
            clone.origin = this.origin.clone();
            clone.direction = this.direction.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toString() {
        return "origin: " + origin + " direction: " + direction;
    }

    public Vector3D getPointAtDistance(double distance) {
        Vector3D dir = newVector3D(direction.getX(), direction.getY(), direction.getZ());
        Vector3D orig = newVector3D(origin.getX(), origin.getY(), origin.getZ());
        return orig.add(dir.multiply(distance));
    }

    //https://en.wikipedia.org/wiki/Skew_lines#Nearest_Points
    public Pair<Vector3D, Vector3D> closestPointsBetweenLines(Ray other) {
        Vector3D n1 = direction.clone().crossProduct(other.direction.clone().crossProduct(direction));
        Vector3D n2 = other.direction.clone().crossProduct(direction.clone().crossProduct(other.direction));

        Vector3D c1 = origin.clone().add(direction.clone().multiply(other.origin.clone().subtract(origin).dot(n2) / direction.dot(n2)));
        Vector3D c2 = other.origin.clone().add(other.direction.clone().multiply(origin.clone().subtract(other.origin).dot(n1) / other.direction.dot(n1)));

        return new Pair<>(c1, c2);
    }

    public Vector3D getOrigin() {
        return origin;
    }

    public Vector3D calculateDirection() {
        return direction;
    }
}
