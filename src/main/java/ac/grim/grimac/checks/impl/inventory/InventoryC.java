package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.InventoryCheck;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.util.FoliaCompatUtil;

@CheckData(name = "InventoryC", setback = 3)
public class InventoryC extends InventoryCheck {

    public InventoryC(GrimPlayer player) {
        super(player);
    }

    public void onBlockPlace(final BlockPlace place) {
        // It is not possible to place a block while the inventory is open
        if (player.hasInventoryOpen) {
            if (flagAndAlert("Placed a block while inventory is open")) {
                if (shouldModifyPackets()) {
                    place.resync();
                }
                closeInventory();
            }
        } else {
            reward();
        }
    }
}
