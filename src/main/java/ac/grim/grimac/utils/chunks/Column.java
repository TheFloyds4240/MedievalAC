package ac.grim.grimac.utils.chunks;


import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;

public class Column {
    private final int x;
    private final int z;
    private final BaseChunk[] chunks;
    private final int transaction;

    // Constructor
    public Column(int x, int z, BaseChunk[] chunks, int transaction) {
        this.x = x;
        this.z = z;
        this.chunks = chunks;
        this.transaction = transaction;
    }

    // Getters
    public int x() {
        return x;
    }

    public int z() {
        return z;
    }

    public BaseChunk[] chunks() {
        return chunks;
    }

    public int transaction() {
        return transaction;
    }

    // Method to merge chunks
    public void mergeChunks(BaseChunk[] toMerge) {
        for (int i = 0; i < 16; i++) {
            if (toMerge[i] != null) {
                chunks[i] = toMerge[i];
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Column{x=%d, z=%d, chunks=%s, transaction=%d}",
                x, z, java.util.Arrays.toString(chunks), transaction
        );
    }
}
