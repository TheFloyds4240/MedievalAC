package ac.grim.grimac.checks.impl.scaffolding;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockPlaceCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.update.BlockPlace;
import ac.grim.grimac.utils.change.BlockModification;
import ac.grim.grimac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.Vector3i;


@CheckData(name = "AirLiquidPlace")
public class AirLiquidPlace extends BlockPlaceCheck {

    private int lastTick;
    private boolean didLastFlag;
    private Vector3i lastPlaceLoc;
    private StateType lastBlockType;

    public AirLiquidPlace(GrimPlayer player) {
        super(player);
    }

    /* On 1.9-1.21.1 clients we see:
        Async world updated: short_grass -> air at X: -32, Y: 69, Z: -240, tick 0, cause/source: DiggingAction.START_DIGGING
        AirLiquidPlace Check: Block state at X: -32, Y: 69, Z: -240 is air (valid: false), tick +0, cause/source: PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT <---- previously falsed here
        Async world updated: air -> short_grass at X: -32, Y: 69, Z: -240, tick +3-4, cause: realtime task in applyBlockChanges(List<Vector3i> toApplyBlocks) source: PacketType.Play.Client.PONG
        Async world updated: short_grass -> air at X: -32, Y: 69, Z: -240, tick +0-1, cause: handleNettySyncTransaction(LatencyUtils.java:56) source: PacketType.Play.Client.PONG
     */

    // This check has been plagued by falses for ages and I"ve finally figured it out.
    // When breaking and placing on the same tick in the same tick, I believe the vanilla client always sends DIGGING ACTION packets first
    // We could make a new BadPackets check based on this information, I wonder what we'd catch?, but I'm not 100% certain this is the cause,
    // This check's falses all seem to stem from processing DiggingAction.START_DIGGING before PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT in the same tick
    @Override
    public void onBlockPlace(final BlockPlace place) {
        if (player.gamemode == GameMode.CREATIVE) return;

        Vector3i blockPos = place.getPlacedAgainstBlockLocation();
        StateType placeAgainst = player.compensatedWorld.getStateTypeAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        int currentTick = GrimAPI.INSTANCE.getTickManager().currentTick;
        Iterable<BlockModification> blockModifications = player.blockHistory.getRecentModifications((blockModification) -> currentTick - blockModification.getTick() == 0
                && blockPos.equals(blockModification.getLocation())
                && blockModification.getCause() == BlockModification.Cause.START_DIGGING);

        // being debug
        boolean isValid = !(placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst));

        // Format the log message
        String logMessage = String.format(
                "AirLiquidPlace Check: Block state at %s is %s (valid: %b), tick %d",
                blockPos,
                placeAgainst,
                isValid,
                currentTick
        );

        LogUtil.info(logMessage);
        // end debug

        int its = 0;
        for (BlockModification blockModification : blockModifications) {
            StateType stateType = blockModification.getOldBlockContents().getType();
            if (!(stateType.isAir() || Materials.isNoPlaceLiquid(stateType))) {
                player.blockHistory.cleanup(currentTick - 2);
                return;
            }
            its++;
        }

        if (placeAgainst.isAir() || Materials.isNoPlaceLiquid(placeAgainst)) { // fail
            if (flagAndAlert("tick: " + GrimAPI.INSTANCE.getTickManager().currentTick) && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
        player.blockHistory.cleanup(currentTick - 2);
    }

    @Override
    public void onReload(ConfigManager config) {
        this.cancelVL = config.getIntElse(getConfigName() + ".cancelVL", 0);
    }
}
