//package ac.grim.grimac.benchmark.vector;
//
//import ac.grim.grimac.utils.vector.NativeVector3D;
//import org.openjdk.jmh.annotations.*;
//
//import java.util.concurrent.TimeUnit;
//
//@BenchmarkMode(Mode.AverageTime)
//@OutputTimeUnit(TimeUnit.NANOSECONDS)
//@State(Scope.Thread)
//@Warmup(iterations = 2, time = 1)
//@Measurement(iterations = 3, time = 1)
//@Fork(1)
//public class NativeVector3DBenchmark {
//
//    private NativeVector3D vector1;
//    private NativeVector3D vector2;
//
//    @Setup
//    public void setup() {
//        vector1 = new NativeVector3D(1.0, 2.0, 3.0);
//        vector2 = new NativeVector3D(4.0, 5.0, 6.0);
//    }
//
//    @Benchmark
//    public double benchmarkGetX() {
//        return vector1.getX();
//    }
//
//    @Benchmark
//    public double benchmarkGetY() {
//        return vector1.getY();
//    }
//
//    @Benchmark
//    public double benchmarkGetZ() {
//        return vector1.getZ();
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkSetX() {
//        return (NativeVector3D) vector1.setX(10.0);
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkSetY() {
//        return (NativeVector3D) vector1.setY(20.0);
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkSetZ() {
//        return (NativeVector3D) vector1.setZ(30.0);
//    }
//
//    @Benchmark
//    public double benchmarkLength() {
//        return vector1.length();
//    }
//
//    @Benchmark
//    public double benchmarkLengthSquared() {
//        return vector1.lengthSquared();
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkMultiplyScalar() {
//        return (NativeVector3D) vector1.multiply(2.0);
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkNormalize() {
//        return (NativeVector3D) vector1.normalize();
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkCrossProduct() {
//        return (NativeVector3D) vector1.crossProduct(vector2);
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkAdd() {
//        return (NativeVector3D) vector1.add(vector2);
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkSubtract() {
//        return (NativeVector3D) vector1.subtract(vector2);
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkMultiplyVector() {
//        return (NativeVector3D) vector1.multiply(vector2);
//    }
//
//    @Benchmark
//    public double benchmarkDistance() {
//        return vector1.distance(vector2);
//    }
//
//    @Benchmark
//    public double benchmarkDistanceSquared() {
//        return vector1.distanceSquared(vector2);
//    }
//
//    @Benchmark
//    public double benchmarkDotProduct() {
//        return vector1.dot(vector2);
//    }
//
//    @Benchmark
//    public NativeVector3D benchmarkClone() {
//        return (NativeVector3D) vector1.clone();
//    }
//}
