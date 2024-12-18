package ac.grim.grimac.utils.change;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerBlockHistory {
    public final Deque<BlockModification> modificationQueue = new ArrayDeque<>();

    // Add a new block modification to the history.
    public void add(BlockModification modification) {
        modificationQueue.add(modification);
    }

    // Get all recent modifications (optionally filtered by a condition).
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

    // Remove old modifications older than maxTick
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
