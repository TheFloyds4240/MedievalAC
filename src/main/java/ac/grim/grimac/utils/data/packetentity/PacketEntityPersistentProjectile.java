package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;

import java.util.UUID;

public class PacketEntityPersistentProjectile extends PacketEntityProjectile {

    // currently only spectral arrows, arrows (same entity as tipped arrows) and tridents extend this in vanilla
    public PacketEntityPersistentProjectile(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
        super(player, uuid, type, x, y, z);
    }

    // in vanilla this returns !onGround; We don't have a nice way to check if a packetEntity is on the ground
    // but we exempt when canHit == false anyways
    @Override
    public boolean canHit() {
        return false;
    }
}
