package ac.grim.grimac.utils.vector;

import jdk.incubator.vector.*;

public final class SIMDVector3D implements Vector3D {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;
    private static final VectorMask<Double> LENGTH_3_ARRAY_MASK = SPECIES.indexInRange(0, 3);
    private static final VectorShuffle<Double> CROSS_PRODUCT_SHUFFLE1 = VectorShuffle.fromArray(SPECIES, new int[]{1, 2, 0, 3}, 0);
    private static final VectorShuffle<Double> CROSS_PRODUCT_SHUFFLE2 = VectorShuffle.fromArray(SPECIES, new int[]{2, 0, 1, 3}, 0);
    private static final DoubleVector FOURTH_COMPONENT_ZERO = DoubleVector.zero(SPECIES).withLane(3, 1.0);

    private DoubleVector vector;

    public SIMDVector3D(double x, double y, double z) {
        this.vector = DoubleVector.fromArray(SPECIES, new double[]{x, y, z, 0}, 0);
    }

    @Override
    public double getX() {
        return this.vector.lane(0);
    }

    @Override
    public double getY() {
        return this.vector.lane(1);
    }

    @Override
    public double getZ() {
        return this.vector.lane(2);
    }

    @Override
    public Vector3D setX(double x) {
        this.vector = this.vector.withLane(0, x);
        return this;
    }

    @Override
    public Vector3D setY(double y) {
        this.vector = this.vector.withLane(1, y);
        return this;
    }

    @Override
    public Vector3D setZ(double z) {
        this.vector = this.vector.withLane(2, z);
        return this;
    }

    @Override
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override
    public double lengthSquared() {
        return this.vector.mul(this.vector).reduceLanes(VectorOperators.ADD);
    }

    @Override
    public Vector3D multiply(double m) {
        this.vector = this.vector.mul(m);
        return this;
    }

    @Override
    public Vector3D normalize() {
//        DoubleVector squared = this.vector.mul(this.vector);
//        double sum = squared.reduceLanes(VectorOperators.ADD);
//        double length = Math.sqrt(sum);
//        this.vector = this.vector.div(length);
//        return this;
        double length = length();
        this.vector = this.vector.div(length).blend(FOURTH_COMPONENT_ZERO, LENGTH_3_ARRAY_MASK.not());
        return this;
    }

    @Override
    public Vector3D crossProduct(Vector3D other) {
        SIMDVector3D o = (SIMDVector3D) other;

        DoubleVector tmp0 = this.vector.rearrange(CROSS_PRODUCT_SHUFFLE1);
        DoubleVector tmp1 = o.vector.rearrange(CROSS_PRODUCT_SHUFFLE2);

        DoubleVector tmp2 = tmp0.mul(o.vector);
        DoubleVector tmp3 = tmp0.mul(tmp1);

        DoubleVector tmp4 = tmp2.rearrange(CROSS_PRODUCT_SHUFFLE1);

        this.vector = tmp3.sub(tmp4).blend(FOURTH_COMPONENT_ZERO, LENGTH_3_ARRAY_MASK.not());
        return this;
    }

    @Override
    public Vector3D add(Vector3D o) {
        this.vector = this.vector.add(((SIMDVector3D) o).vector);
        return this;
    }

    @Override
    public Vector3D subtract(Vector3D o) {
        this.vector = this.vector.sub(((SIMDVector3D) o).vector);
        return this;
    }

    @Override
    public Vector3D multiply(Vector3D o) {
        this.vector = this.vector.mul(((SIMDVector3D) o).vector);
        return this;
    }

    @Override
    public double distance(Vector3D o) {
        return Math.sqrt(distanceSquared(o));
    }

    @Override
    public double distanceSquared(Vector3D o) {
        DoubleVector diff = this.vector.sub(((SIMDVector3D) o).vector);
        return diff.mul(diff).reduceLanes(VectorOperators.ADD);
    }

    @Override
    public double dot(Vector3D o) {
        return this.vector.mul(((SIMDVector3D) o).vector).reduceLanes(VectorOperators.ADD);
    }


    @Override
    public Vector3D clone() {
        // I think this makes a shallow clone if I do it like this?
//        try {
//            return (Vector3D) super.clone();
//        } catch (CloneNotSupportedException e) {
//            throw new Error(e);
//        }
        try {
            SIMDVector3D cloned = (SIMDVector3D) super.clone();
            // Create a new DoubleVector with the same values
            cloned.vector = DoubleVector.fromArray(SPECIES,
                    new double[]{this.getX(), this.getY(), this.getZ(), 0}, 0);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
