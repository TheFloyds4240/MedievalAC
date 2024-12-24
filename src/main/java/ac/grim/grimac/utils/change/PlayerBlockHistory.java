package ac.grim.grimac.utils.change;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tracks block modifications made by a player over time.
 */
public class PlayerBlockHistory {
    // TODO, figure out how its possible for this to CME!
    public final Deque<BlockModification> modificationQueue = new ConcurrentLinkedDeque<>();

    /**
     * Adds a new block modification to the history.
     * @param modification The block modification to add
     */
    public void add(BlockModification modification) {
        modificationQueue.add(modification);
    }

    /**
     * Retrieves recent modifications that match the given filter.
     * @param filter Predicate to filter modifications
     * @return Filtered list of block modifications
     */
    public List<BlockModification> getRecentModifications(Predicate<BlockModification> filter) {
        return modificationQueue.stream().filter(filter).collect(Collectors.toList()); // Java 8+ compatible
    }

    public List<WrappedBlockState> getBlockStates(Predicate<BlockModification> filter) {
        return modificationQueue.stream()
                .filter(filter)
                .flatMap(mod -> Stream.of(mod.getOldBlockContents(), mod.getNewBlockContents()))
                .collect(Collectors.toList());// Java 8+ compatible
    }

    public List<WrappedBlockState> getPreviousBlockStates(Predicate<BlockModification> filter) {
        return modificationQueue.stream()
                .filter(filter)
                .map(BlockModification::getOldBlockContents)
                .collect(Collectors.toList());
    }

    public List<WrappedBlockState> getResultingBlockStates(Predicate<BlockModification> filter) {
        return modificationQueue.stream()
                .filter(filter)
                .map(BlockModification::getNewBlockContents)
                .collect(Collectors.toList());
    }

    /**
     * Removes modifications older than the specified tick.
     * @param maxTick The maximum tick age to keep
     */
    public void cleanup(int maxTick) {
        while (!modificationQueue.isEmpty() && maxTick - modificationQueue.peekFirst().getTick() > 0) {
            modificationQueue.removeFirst();
        }
    }

    // Get the size of the block history
    public int size() {
        return modificationQueue.size();
    }

    // Clear all block modifications
    public void clear() {
        modificationQueue.clear();
    }
}
