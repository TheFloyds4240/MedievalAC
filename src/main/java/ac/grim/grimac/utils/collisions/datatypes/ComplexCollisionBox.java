package ac.grim.grimac.utils.collisions.datatypes;

import java.util.Arrays;
import java.util.List;

public class ComplexCollisionBox implements CollisionBox {

    // Most complex shape is the Modern MC Cauldron which is made up of 15 boxes
    public static int DEFAULT_MAX_COLLISION_BOX_SIZE = 15; // increase if we somehow have a shape made of more than 15 parts.
    private final SimpleCollisionBox[] boxes;
    int maxLength = 0;

    public ComplexCollisionBox(SimpleCollisionBox... boxes) {
        this(DEFAULT_MAX_COLLISION_BOX_SIZE, boxes);
    }

    public ComplexCollisionBox(int maxIndex) {
        this.boxes = new SimpleCollisionBox[maxIndex];
    }

    public ComplexCollisionBox(int maxIndex, SimpleCollisionBox... boxes) {
        this.boxes = new SimpleCollisionBox[maxIndex];
        System.arraycopy(boxes, 0, this.boxes, 0, Math.min(maxIndex, boxes.length));
        maxLength = boxes.length;
    }

    public boolean add(SimpleCollisionBox collisionBox) {
        boxes[maxLength] = collisionBox;
        maxLength++;
        return maxLength <= boxes.length;
    }

    @Override
    public CollisionBox union(SimpleCollisionBox other) {
        add(other);
        return this;
    }

    @Override
    public boolean isCollided(SimpleCollisionBox other) {
        for (int i = 0; i < maxLength; i++) {
            if (boxes[i].isCollided(other)) return true;
        }
        return false;
    }

    @Override
    public boolean isIntersected(SimpleCollisionBox other) {
        for (int i = 0; i < maxLength; i++) {
            if (boxes[i].isIntersected(other)) return true;
        }
        return false;
    }

    @Override
    public CollisionBox copy() {
        return new ComplexCollisionBox(boxes.length, boxes);
    }

    @Override
    public CollisionBox offset(double x, double y, double z) {
        for (int i = 0; i < maxLength; i++) {
            boxes[i].offset(x, y ,z);
        }
        return this;
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        list.addAll(Arrays.asList(boxes).subList(0, maxLength));
    }

    @Override
    public int downCast(SimpleCollisionBox[] list) {
        System.arraycopy(boxes, 0, list, 0, maxLength);
        return maxLength;
    }

    @Override
    public boolean isNull() {
        for (int i = 0; i < maxLength; i++) {
            if (!boxes[i].isNull()) return false;
        }
        return true;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }
}
