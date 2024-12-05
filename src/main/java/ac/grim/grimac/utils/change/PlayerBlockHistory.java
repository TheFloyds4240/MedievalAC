package ac.grim.grimac.utils.change;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerBlockHistory {
    private final Deque<BlockModification> blockHistory = new ArrayDeque<>();

    // Add a new block modification to the history.
    public void add(BlockModification modification) {
        blockHistory.add(modification);
    }

    // Get all recent modifications (optionally filtered by a condition).
    public Iterable<BlockModification> getRecentModifications(Predicate<BlockModification> filter) {
        return blockHistory.stream().filter(filter).collect(Collectors.toList()); // Java 8+ compatible
    }

    // Remove old modifications older than maxTick
    public void cleanup(int maxTick) {
        while (!blockHistory.isEmpty() && maxTick - blockHistory.peekFirst().getTick() > 0) {
            blockHistory.removeFirst();
        }
    }

    // Get the size of the block history
    public int size() {
        return blockHistory.size();
    }

    // Clear all block modifications
    public void clear() {
        blockHistory.clear();
    }
}
