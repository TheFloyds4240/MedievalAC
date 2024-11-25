package ac.grim.grimac.utils.change;


import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Predicate;

public class PlayerBlockHistory {
    private final ConcurrentLinkedDeque<BlockModification> blockHistory = new ConcurrentLinkedDeque<>();

    // Add a new block modification to the history.
    public void add(BlockModification modification) {
        blockHistory.add(modification);
    }

    // Get all recent modifications (optionally filtered by a condition).
    public Iterable<BlockModification> getRecentModifications(Predicate<BlockModification> filter) {
        return blockHistory.stream().filter(filter).toList(); // Java 8+ compatible
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
