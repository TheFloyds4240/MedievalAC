package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.vector.Vector3D;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.vector.Vector3D;

import static ac.grim.grimac.utils.vector.VectorFactory.newVector3D;
import static com.github.retrooper.packetevents.protocol.player.ClientVersion.V_1_14;
import static java.lang.Math.abs;

public class FluidFallingAdjustedMovement {
    public static Vector3D getFluidFallingAdjustedMovement(GrimPlayer player, double d, boolean bl, Vector3D vec3) {
        if (player.hasGravity && !player.isSprinting) {
            boolean falling = player.getClientVersion().isNewerThanOrEquals(V_1_14) ? bl : vec3.getY() < 0;
            double d2 = falling && abs(vec3.getY() - 0.005) >= 0.003 && abs(vec3.getY() - d / 16.0) < 0.003 ? -0.003 : vec3.getY() - d / 16.0;
            return newVector3D(vec3.getX(), d2, vec3.getZ());
        }
        return vec3;
    }
}
