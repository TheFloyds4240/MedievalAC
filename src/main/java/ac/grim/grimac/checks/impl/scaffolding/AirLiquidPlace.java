package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.change.BlockModification;
import ac.grim.grimac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3i;

import java.util.Iterator;


@CheckData(name = "AirLiquidPlace")
public class AirLiquidPlace extends BlockPlaceCheck {

    public AirLiquidPlace(GrimPlayer player) {
        super(player);
    }

    /* This check has been plagued by falses for ages, and I've finally figured it out.
    When breaking and placing on the same tick in the same tick, I believe the vanilla client always sends DIGGING ACTION packets first
    This check's falses all seem to stem from processing DiggingAction.START_DIGGING before PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT in the same tick
    Since we process the break first, when we go to process the place it looks like the player placed against air in the async world

    We will often see:
        Async world updated: short_grass -> air at X: -32, Y: 69, Z: -240, tick 0, cause/source: DiggingAction.START_DIGGING
        AirLiquidPlace Check: Block state at X: -32, Y: 69, Z: -240 is air (valid: false), tick +0, cause/source: PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT <---- previously falsed here
        Async world updated: air -> short_grass at X: -32, Y: 69, Z: -240, tick +3-4, cause: realtime task in applyBlockChanges(List<Vector3i> toApplyBlocks) source: PacketType.Play.Client.PONG
        Async world updated: short_grass -> air at X: -32, Y: 69, Z: -240, tick +0-1, cause: handleNettySyncTransaction(LatencyUtils.java:56) source: PacketType.Play.Client.PONG

        Which previously would've caused a false.
        To solve this we store recently changed blocks caused by DiggingAction.START_DIGGING (instant breaking) and check against the old block.
        Lots of other checks have similar issues, and with the new player.blockHistory we can patch those.
    */

    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.gamemode == GameMode.CREATIVE) return;

        Vector3i blockPos = place.getPlacedAgainstBlockLocation();
        StateType placeAgainst = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        int currentTick = GrimAPI.INSTANCE.getTickManager().currentTick;
        Iterable<BlockModification> blockModifications = player.blockHistory.getRecentModifications((blockModification) -> currentTick - blockModification.getTick() == 0
                && blockPos.equals(blockModification.getLocation())
                && blockModification.getCause() == BlockModification.Cause.START_DIGGING);


        // Check if old block from instant breaking in same tick as the current placement was valid
        // There should only be one block here for legit clients
        Iterator<BlockModification> iterator =  blockModifications.iterator();
        if (iterator.hasNext()) {
            StateType stateType = iterator.next().getOldBlockContents().getType();
            if (!(stateType.isAir() || Materials.isNoPlaceLiquid(stateType))) {
                return;
            }
        }

        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst)) { // fail
            if (flagAndAlert("tick: " + GrimAPI.INSTANCE.getTickManager().currentTick) && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }

    @Override
    public void onReload(ConfigManager config) {
        this.cancelVL = config.getIntElse(getConfigName() + ".cancelVL", 0);
    }
}
