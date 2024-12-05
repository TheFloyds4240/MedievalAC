package ac.grim.grimac.benchmark.vector;

import ac.grim.grimac.utils.vector.ScalarVector3D;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class ScalarVector3DBenchmark {

    private ScalarVector3D vector1;
    private ScalarVector3D vector2;

    @Setup
    public void setup() {
        vector1 = new ScalarVector3D(1.0, 2.0, 3.0);
        vector2 = new ScalarVector3D(4.0, 5.0, 6.0);
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
    public ScalarVector3D benchmarkSetX() {
        return (ScalarVector3D) vector1.setX(10.0);
    }

    @Benchmark
    public ScalarVector3D benchmarkSetY() {
        return (ScalarVector3D) vector1.setY(20.0);
    }

    @Benchmark
    public ScalarVector3D benchmarkSetZ() {
        return (ScalarVector3D) vector1.setZ(30.0);
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
    public ScalarVector3D benchmarkMultiplyScalar() {
        return (ScalarVector3D) vector1.multiply(2.0);
    }

    @Benchmark
    public ScalarVector3D benchmarkNormalize() {
        return (ScalarVector3D) vector1.normalize();
    }

    @Benchmark
    public ScalarVector3D benchmarkCrossProduct() {
        return (ScalarVector3D) vector1.crossProduct(vector2);
    }

    @Benchmark
    public ScalarVector3D benchmarkAdd() {
        return (ScalarVector3D) vector1.add(vector2);
    }

    @Benchmark
    public ScalarVector3D benchmarkSubtract() {
        return (ScalarVector3D) vector1.subtract(vector2);
    }

    @Benchmark
    public ScalarVector3D benchmarkMultiplyVector() {
        return (ScalarVector3D) vector1.multiply(vector2);
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
    public ScalarVector3D benchmarkClone() {
        return (ScalarVector3D) vector1.clone();
    }
}
