package ac.grim.grimac.checks.impl.breaking;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;

@CheckData(name = "PositionBreakA")
public class PositionBreakA extends Check implements BlockBreakCheck {
    public PositionBreakA(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        SimpleCollisionBox combined = blockBreak.getCombinedBox();

        final double[] possibleEyeHeights = player.getPossibleEyeHeights();
        double minEyeHeight = Double.MAX_VALUE;
        double maxEyeHeight = Double.MIN_VALUE;
        for (double height : possibleEyeHeights) {
            minEyeHeight = Math.min(minEyeHeight, height);
            maxEyeHeight = Math.max(maxEyeHeight, height);
        }

        SimpleCollisionBox eyePositions = new SimpleCollisionBox(player.x, player.y + minEyeHeight, player.z, player.x, player.y + maxEyeHeight, player.z);
        if (!player.packetStateData.didLastMovementIncludePosition || player.canSkipTicks()) {
            eyePositions.expand(player.getMovementThreshold());
        }

        // If the player is inside a block, then they can ray trace through the block and hit the other side of the block
        if (eyePositions.isIntersected(combined)) {
            return;
        }

        // So now we have the player's possible eye positions
        // So then look at the face that the player has clicked
        boolean flag;
        switch (blockBreak.face) {
            case NORTH:
                flag = eyePositions.minZ > combined.minZ; // Z- face
                break;
            case SOUTH:
                flag = eyePositions.maxZ < combined.maxZ; // Z+ face
                break;
            case EAST:
                flag = eyePositions.maxX < combined.maxX; // X+ face
                break;
            case WEST:
                flag = eyePositions.minX > combined.minX; // X- face
                break;
            case UP:
                flag = eyePositions.maxY < combined.maxY; // Y+ face
                break;
            case DOWN:
                flag = eyePositions.minY > combined.minY; // Y- face
                break;
            default:
                flag = false;
                break;
        }

        if (flag && flagAndAlert("action=" + blockBreak.action + ", face=" + blockBreak.face) && shouldModifyPackets()) {
            blockBreak.cancel();
        }
    }
}
