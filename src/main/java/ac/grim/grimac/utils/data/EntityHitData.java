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
        super(new Vector(packetEntity.trackedServerPosition.getPos().x, packetEntity.trackedServerPosition.getPos().y, packetEntity.trackedServerPosition.getPos().z));
        this.entity = packetEntity;
    }
}
