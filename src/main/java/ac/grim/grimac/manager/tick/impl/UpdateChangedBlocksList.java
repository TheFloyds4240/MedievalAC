package ac.grim.grimac.manager.tick.impl;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.impl.scaffolding.LineOfSightPlace;
import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.viaversion.viaversion.util.Triple;

import java.util.Iterator;
import java.util.Set;

public class UpdateChangedBlocksList implements Tickable {
    @Override
    public void tick() {
        for (GrimPlayer player : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            Set<Triple<Vector3i, WrappedBlockState, Byte>> blocksChangedList = player.checkManager.getBlockPlaceCheck(LineOfSightPlace.class).blocksChangedList;

            // Create an iterator to iterate over the set
            Iterator<Triple<Vector3i, WrappedBlockState, Byte>> iterator = blocksChangedList.iterator();

            // Iterate over the set
            while (iterator.hasNext()) {
                Triple<Vector3i, WrappedBlockState, Byte> entry = iterator.next();

                // Decrement the byte value
                byte newValue = (byte) (entry.third() - 1);

                // If the new value is less than or equal to 1, remove the entry
                if (newValue == 0) {
                    iterator.remove();
                } else {
                    // Otherwise, update the entry with the new value
                    iterator.remove();
                    blocksChangedList.add(new Triple<>(entry.first(), entry.second(), newValue));
                }
            }
        }
    }
}
