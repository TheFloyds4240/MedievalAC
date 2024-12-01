package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

import java.util.UUID;

public class PacketEntityProjectile extends PacketEntity {
    public PacketEntityProjectile(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
        super(player, uuid, type, x, y, z);
    }

    @Override
    public boolean canHit() {
        // only fireballs, breeze wind charge, and wind charge can be hit by default
        EntityType type = this.getType();
        return type == EntityTypes.FIREBALL || type == EntityTypes.WIND_CHARGE || type == EntityTypes.BREEZE_WIND_CHARGE;
    }

    @Override
    public float getTargetingMargin() {
        return this.canHit() ? 1.0F : 0.0F;
    }
}
