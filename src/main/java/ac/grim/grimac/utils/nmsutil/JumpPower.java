package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.vector.Vector3D;

import java.util.OptionalInt;

import static ac.grim.grimac.utils.vector.VectorFactory.newVector3D;

public class JumpPower {
    public static void jumpFromGround(GrimPlayer player, Vector3D vector) {
        float jumpPower = getJumpPower(player);

        final OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
        if (jumpBoost.isPresent()) {
            jumpPower += 0.1f * (jumpBoost.getAsInt() + 1);
        }

        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_5) && jumpPower <= 1.0E-5F) return;

        vector.setY(jumpPower);

        if (player.isSprinting) {
            float radRotation = player.xRot * ((float) Math.PI / 180F);
            vector.add(newVector3D(-player.trigHandler.sin(radRotation) * 0.2f, 0.0, player.trigHandler.cos(radRotation) * 0.2f));
        }
    }

    public static float getJumpPower(GrimPlayer player) {
        return (float) player.compensatedEntities.getSelf().getAttributeValue(Attributes.GENERIC_JUMP_STRENGTH) * getPlayerJumpFactor(player);
    }

    public static float getPlayerJumpFactor(GrimPlayer player) {
        return BlockProperties.onHoneyBlock(player, player.mainSupportingBlockData, new Vector3d(player.lastX, player.lastY, player.lastZ)) ? 0.5f : 1f;
    }
}
