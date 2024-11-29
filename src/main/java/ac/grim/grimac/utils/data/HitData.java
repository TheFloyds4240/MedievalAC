package ac.grim.grimac.utils.data;

import lombok.Getter;
import org.bukkit.util.Vector;

@Getter
public class HitData {
    Vector blockHitLocation;

    public HitData(Vector blockHitLocation) {
        this.blockHitLocation = blockHitLocation;
    }
}
