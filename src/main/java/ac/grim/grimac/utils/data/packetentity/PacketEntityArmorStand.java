package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;

import java.util.UUID;

public class PacketEntityArmorStand extends PacketEntity {

    public static final int MARKER_FLAG = 16;

    boolean isMarker;

    public PacketEntityArmorStand(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z, int extraData) {
        super(player, uuid, type, x, y, z);
        isMarker = (extraData & MARKER_FLAG) != 0;
    }

    @Override
    public boolean canHit() {
        return !isMarker;
    }
}
