package ac.grim.grimac.utils.math;

import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.vector.Vector3D;
import com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.vector.Vector3D;

import static ac.grim.grimac.utils.math.GrimMath.clamp;
import static ac.grim.grimac.utils.vector.VectorFactory.newVector3D;

public class VectorUtils {
    public static Vector3D cutBoxToVector(Vector3D vectorToCutTo, Vector3D min, Vector3D max) {
        SimpleCollisionBox box = new SimpleCollisionBox(min, max).sort();
        return cutBoxToVector(vectorToCutTo, box);
    }

    public static Vector3D cutBoxToVector(Vector3D vectorCutTo, SimpleCollisionBox box) {
        return newVector3D(clamp(vectorCutTo.getX(), box.minX, box.maxX), clamp(vectorCutTo.getY(), box.minY, box.maxY), clamp(vectorCutTo.getZ(), box.minZ, box.maxZ));
    }

    public static Vector3D fromVec3d(Vector3d vector3d) {
        return newVector3D(vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }

    // Clamping stops the player from causing an integer overflow and crashing the netty thread
    public static Vector3d clampVector(Vector3d toClamp) {
        double x = GrimMath.clamp(toClamp.getX(), -3.0E7D, 3.0E7D);
        double y = GrimMath.clamp(toClamp.getY(), -2.0E7D, 2.0E7D);
        double z = GrimMath.clamp(toClamp.getZ(), -3.0E7D, 3.0E7D);

        return new Vector3d(x, y, z);
    }
}
