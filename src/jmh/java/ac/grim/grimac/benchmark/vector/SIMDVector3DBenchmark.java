package ac.grim.grimac.benchmark.vector;

import ac.grim.grimac.utils.vector.SIMDVector3D;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class SIMDVector3DBenchmark {

    private SIMDVector3D vector1;
    private SIMDVector3D vector2;

    @Setup
    public void setup() {
        vector1 = new SIMDVector3D(1.0, 2.0, 3.0);
        vector2 = new SIMDVector3D(4.0, 5.0, 6.0);
    }

    @Benchmark
    public double benchmarkGetX() {
        return vector1.getX();
    }

    @Benchmark
    public double benchmarkGetY() {
        return vector1.getY();
    }

    @Benchmark
    public double benchmarkGetZ() {
        return vector1.getZ();
    }

    @Benchmark
    public SIMDVector3D benchmarkSetX() {
        return (SIMDVector3D) vector1.setX(10.0);
    }

    @Benchmark
    public SIMDVector3D benchmarkSetY() {
        return (SIMDVector3D) vector1.setY(20.0);
    }

    @Benchmark
    public SIMDVector3D benchmarkSetZ() {
        return (SIMDVector3D) vector1.setZ(30.0);
    }

    @Benchmark
    public double benchmarkLength() {
        return vector1.length();
    }

    @Benchmark
    public double benchmarkLengthSquared() {
        return vector1.lengthSquared();
    }

    @Benchmark
    public SIMDVector3D benchmarkMultiplyScalar() {
        return (SIMDVector3D) vector1.multiply(2.0);
    }

    @Benchmark
    public SIMDVector3D benchmarkNormalize() {
        return (SIMDVector3D) vector1.normalize();
    }

    @Benchmark
    public SIMDVector3D benchmarkCrossProduct() {
        return (SIMDVector3D) vector1.crossProduct(vector2);
    }

    @Benchmark
    public SIMDVector3D benchmarkAdd() {
        return (SIMDVector3D) vector1.add(vector2);
    }

    @Benchmark
    public SIMDVector3D benchmarkSubtract() {
        return (SIMDVector3D) vector1.subtract(vector2);
    }

    @Benchmark
    public SIMDVector3D benchmarkMultiplyVector() {
        return (SIMDVector3D) vector1.multiply(vector2);
    }

    @Benchmark
    public double benchmarkDistance() {
        return vector1.distance(vector2);
    }

    @Benchmark
    public double benchmarkDistanceSquared() {
        return vector1.distanceSquared(vector2);
    }

    @Benchmark
    public double benchmarkDotProduct() {
        return vector1.dot(vector2);
    }

    @Benchmark
    public SIMDVector3D benchmarkClone() {
        return (SIMDVector3D) vector1.clone();
    }
}
