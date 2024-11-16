package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.InventoryCheck;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity.InteractAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import io.github.retrooper.packetevents.util.FoliaCompatUtil;

@CheckData(name = "InventoryB", setback = 3)
public class InventoryB extends InventoryCheck {
    public InventoryB(GrimPlayer player) {
        super(player);
    }

    public void handle(PacketReceiveEvent event, WrapperPlayClientPlayerDigging wrapper) {
        if (wrapper.getAction() != DiggingAction.START_DIGGING) return;

        // Is not possible to start digging a block while the inventory is open.
        if (player.hasInventoryOpen) {
            if (flagAndAlert("Started digging blocks while inventory is open")) {
                // Cancel the packet
                if (shouldModifyPackets()) {
                    event.setCancelled(true);
                    player.onPacketCancel();
                }
                closeInventory();
            }
        } else {
            reward();
        }
    }
}
