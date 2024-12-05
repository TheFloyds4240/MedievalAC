package ac.grim.grimac.utils.nmsutil;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.vector.Vector3D;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.utils.vector.Vector3D;

import static ac.grim.grimac.utils.vector.VectorFactory.newVector3D;
import static com.github.retrooper.packetevents.PacketEvents.getAPI;
import static com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes.RIPTIDE;
import static com.github.retrooper.packetevents.protocol.item.type.ItemTypes.TRIDENT;
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

public class Riptide {
    public static Vector3D getRiptideVelocity(GrimPlayer player) {
        ItemStack main = player.getInventory().getHeldItem();
        ItemStack off = player.getInventory().getOffHand();

        int j;
        if (main.getType() == TRIDENT) {
            j = main.getEnchantmentLevel(RIPTIDE, getAPI().getServerManager().getVersion().toClientVersion());
        } else if (off.getType() == TRIDENT) {
            j = off.getEnchantmentLevel(RIPTIDE, getAPI().getServerManager().getVersion().toClientVersion());
        } else {
            return newVector3D(); // Can't riptide
        }

        float f7 = player.xRot;
        float f = player.yRot;
        float f1 = -player.trigHandler.sin(f7 * ((float) PI / 180F)) * player.trigHandler.cos(f * ((float) PI / 180F));
        float f2 = -player.trigHandler.sin(f * ((float) PI / 180F));
        float f3 = player.trigHandler.cos(f7 * ((float) PI / 180F)) * player.trigHandler.cos(f * ((float) PI / 180F));
        float f4 = (float) sqrt(f1 * f1 + f2 * f2 + f3 * f3);
        float f5 = 3.0F * ((1.0F + j) / 4.0F);
        f1 = f1 * (f5 / f4);
        f2 = f2 * (f5 / f4);
        f3 = f3 * (f5 / f4);

        // If the player collided vertically with the 1.199999F pushing movement, then the Y additional movement was added
        // (We switched the order around as our prediction engine isn't designed for the proper implementation)
        if (player.verticalCollision) return newVector3D(f1, 0, f3);

        return newVector3D(f1, f2, f3);
    }
}
