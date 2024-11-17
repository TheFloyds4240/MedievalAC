package ac.grim.grimac.utils.collisions.datatypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComplexCollisionBox implements CollisionBox {

    private final List<SimpleCollisionBox> boxes = new ArrayList<>();

    public ComplexCollisionBox(SimpleCollisionBox... boxes) {
        Collections.addAll(this.boxes, boxes);
    }

    public boolean add(SimpleCollisionBox collisionBox) {
        return boxes.add(collisionBox);
    }

    @Override
    public CollisionBox union(SimpleCollisionBox other) {
        add(other);
        return this;
    }

    @Override
    public boolean isCollided(SimpleCollisionBox other) {
        for (CollisionBox box : boxes) {
            if (box.isCollided(other)) return true;
        }
        return false;
    }

    @Override
    public boolean isIntersected(SimpleCollisionBox other) {
        for (CollisionBox box : boxes) {
            if (box.isIntersected(other))
                return true;
        }
        return false;
    }

    @Override
    public CollisionBox copy() {
        ComplexCollisionBox cc = new ComplexCollisionBox();
        for (SimpleCollisionBox b : boxes)
            cc.boxes.add(b.copy());
        return cc;
    }

    @Override
    public CollisionBox offset(double x, double y, double z) {
        for (CollisionBox b : boxes)
            b.offset(x, y, z);
        return this;
    }

    @Override
    public void downCast(List<SimpleCollisionBox> list) {
        for (CollisionBox box : boxes)
            box.downCast(list);
    }

    @Override
    public int downCast(SimpleCollisionBox[] list) {
        final int size = boxes.size();
        for (int i = 0; i < size; i++) {
            list[i] = boxes.get(i);
        }
        return size;
    }

    @Override
    public boolean isNull() {
        for (CollisionBox box : boxes)
            if (!box.isNull())
                return false;
        return true;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }
}
