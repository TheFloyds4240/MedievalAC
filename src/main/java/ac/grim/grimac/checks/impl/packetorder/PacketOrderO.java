package ac.grim.grimac.checks.impl.packetorder;

import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;

@CheckData(name = "PacketOrderO", experimental = true)
public class PacketOrderO extends BlockPlaceCheck {
    public PacketOrderO(final GrimPlayer player) {
        super(player);
    }

    private boolean sentMainhand;

    @Override
    public void onBlockPlace(BlockPlace place) {
        if (!isSupported()) {
            return;
        }

        if (place.getHand() == InteractionHand.OFF_HAND) {
            if (!sentMainhand) {
                if (flagAndAlert("Skipped Mainhand") && shouldModifyPackets() && shouldCancel()) {
                    place.resync();
                }
            }

            sentMainhand = false;
        } else {
            if (sentMainhand) {
                if (flagAndAlert("Skipped Offhand") && shouldModifyPackets() && shouldCancel()) {
                    place.resync();
                }
            }

            sentMainhand = !place.isBlock();
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && !player.packetStateData.lastPacketWasTeleport && !player.packetStateData.lastPacketWasOnePointSeventeenDuplicate) {
            if (sentMainhand) {
                sentMainhand = false;
                flagAndAlert("Skipped Offhand (Tick)");
            }
        }
    }

    private boolean isSupported() {
        return player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13) && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9);
    }
}
