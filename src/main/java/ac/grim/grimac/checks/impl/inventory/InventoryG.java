package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.InventoryCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(name = "InventoryG", setback = 3, experimental = true)
public class InventoryG extends InventoryCheck {

    public InventoryG(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (player.packetStateData.lastPacketWasTeleport) return;
        super.onPacketReceive(event);

        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            if (player.hasInventoryOpen) {
                if (flagAndAlert("Sent a entity action packet while inventory is open")) {
                    closeInventory();
                }
            } else {
                reward();
            }
        }
    }
}
