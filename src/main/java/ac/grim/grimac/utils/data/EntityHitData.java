package ac.grim.grimac.utils.data;

import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.util.Vector;

@Getter
@ToString
public class EntityHitData extends HitData {
    private final PacketEntity entity;

    public EntityHitData(PacketEntity packetEntity) {
        this(packetEntity, new Vector(packetEntity.trackedServerPosition.getPos().x,
                packetEntity.trackedServerPosition.getPos().y,
                packetEntity.trackedServerPosition.getPos().z));
    }

    public EntityHitData(PacketEntity packetEntity, Vector intersectionPoint) {
        super(intersectionPoint);  // Use actual intersection point
        this.entity = packetEntity;
    }
}
