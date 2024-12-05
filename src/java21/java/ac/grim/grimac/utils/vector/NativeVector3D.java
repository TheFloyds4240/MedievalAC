package ac.grim.grimac.utils.vector;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class NativeVector3D implements Vector3D {

    private static final Arena GLOBAL_ARENA = Arena.global();
    private static final MemoryLayout VECTOR_LAYOUT = MemoryLayout.sequenceLayout(3, ValueLayout.JAVA_DOUBLE);
    private static final MethodHandle SIN;
    private static final MethodHandle COS;
    private static final MethodHandle SQRT;
    private static final FunctionDescriptor LENGTH_SQUARED_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
            ValueLayout.ADDRESS
    );
    private static final MethodHandle LENGTH_SQUARED_MH;
    private static final FunctionDescriptor NORMALIZE_DESCRIPTOR = FunctionDescriptor.ofVoid(
            ValueLayout.ADDRESS
    );
    private static final MethodHandle NORMALIZE_MH;
    private static final FunctionDescriptor MULTIPLY_SCALAR_DESCRIPTOR = FunctionDescriptor.ofVoid(
            ValueLayout.ADDRESS,
            ValueLayout.JAVA_DOUBLE
    );
    private static final MethodHandle MULTIPLY_SCALAR_MH;
    private static final FunctionDescriptor CROSS_PRODUCT_DESCRIPTOR = FunctionDescriptor.ofVoid(
            ValueLayout.ADDRESS,
            ValueLayout.ADDRESS
    );
    private static final MethodHandle CROSS_PRODUCT_MH;
    private static final FunctionDescriptor ADD_DESCRIPTOR = FunctionDescriptor.ofVoid(
            ValueLayout.ADDRESS,
            ValueLayout.ADDRESS
    );
    private static final MethodHandle ADD_MH;
    private static final FunctionDescriptor SUBTRACT_DESCRIPTOR = FunctionDescriptor.ofVoid(
            ValueLayout.ADDRESS,
            ValueLayout.ADDRESS
    );
    private static final MethodHandle SUBTRACT_MH;
    private static final FunctionDescriptor MULTIPLY_VECTOR_DESCRIPTOR = FunctionDescriptor.ofVoid(
            ValueLayout.ADDRESS,
            ValueLayout.ADDRESS
    );
    private static final MethodHandle MULTIPLY_VECTOR_MH;
    private static final FunctionDescriptor DISTANCE_SQUARED_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
            ValueLayout.ADDRESS,
            ValueLayout.ADDRESS
    );
    private static final MethodHandle DISTANCE_SQUARED_MH;
    private static final FunctionDescriptor DOT_DESCRIPTOR = FunctionDescriptor.of(ValueLayout.JAVA_DOUBLE,
            ValueLayout.ADDRESS,
            ValueLayout.ADDRESS
    );
    private static final MethodHandle DOT_MH;


    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup symbolLookup = SymbolLookup.loaderLookup(); // Or libraryLookup if needed
        try {
            SIN = MethodHandles.lookup().findStatic(Math.class, "sin", MethodType.methodType(double.class, double.class));
            COS = MethodHandles.lookup().findStatic(Math.class, "cos", MethodType.methodType(double.class, double.class));
            SQRT = MethodHandles.lookup().findStatic(Math.class, "sqrt", MethodType.methodType(double.class, double.class));
            // Load native library containing optimized vector operations
            System.loadLibrary("vector_ops"); // Or use linker.downcall to load from a specific path
            LENGTH_SQUARED_MH = linker.downcallHandle(symbolLookup.find("length_squared").orElseThrow(), LENGTH_SQUARED_DESCRIPTOR);
            NORMALIZE_MH = linker.downcallHandle(symbolLookup.find("normalize").orElseThrow(), NORMALIZE_DESCRIPTOR);
            MULTIPLY_SCALAR_MH = linker.downcallHandle(symbolLookup.find("multiply_scalar").orElseThrow(), MULTIPLY_SCALAR_DESCRIPTOR);
            CROSS_PRODUCT_MH = linker.downcallHandle(symbolLookup.find("cross_product").orElseThrow(), CROSS_PRODUCT_DESCRIPTOR);
            ADD_MH = linker.downcallHandle(symbolLookup.find("add").orElseThrow(), ADD_DESCRIPTOR);
            SUBTRACT_MH = linker.downcallHandle(symbolLookup.find("subtract").orElseThrow(), SUBTRACT_DESCRIPTOR);
            MULTIPLY_VECTOR_MH = linker.downcallHandle(symbolLookup.find("multiply_vector").orElseThrow(), MULTIPLY_VECTOR_DESCRIPTOR);
            DISTANCE_SQUARED_MH = linker.downcallHandle(symbolLookup.find("distance_squared").orElseThrow(), DISTANCE_SQUARED_DESCRIPTOR);
            DOT_MH = linker.downcallHandle(symbolLookup.find("dot").orElseThrow(), DOT_DESCRIPTOR);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final MemorySegment memorySegment;

    public NativeVector3D(double x, double y, double z) {
        this.memorySegment = GLOBAL_ARENA.allocate(VECTOR_LAYOUT);
        setX(x);
        setY(y);
        setZ(z);
    }

    public NativeVector3D(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
    }

    @Override
    public double getX() {
        return memorySegment.getAtIndex(ValueLayout.JAVA_DOUBLE, 0);
    }

    @Override
    public double getY() {
        return memorySegment.getAtIndex(ValueLayout.JAVA_DOUBLE, 1);
    }

    @Override
    public double getZ() {
        return memorySegment.getAtIndex(ValueLayout.JAVA_DOUBLE, 2);
    }

    @Override
    public NativeVector3D setX(double x) {
        memorySegment.setAtIndex(ValueLayout.JAVA_DOUBLE, 0, x);
        return this;
    }

    @Override
    public NativeVector3D setY(double y) {
        memorySegment.setAtIndex(ValueLayout.JAVA_DOUBLE, 1, y);
        return this;
    }

    @Override
    public NativeVector3D setZ(double z) {
        memorySegment.setAtIndex(ValueLayout.JAVA_DOUBLE, 2, z);
        return this;
    }

    @Override
    public double length() {
        try {
            return (double) SQRT.invokeExact(lengthSquared());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double lengthSquared() {
        try {
            return (double) LENGTH_SQUARED_MH.invokeExact(memorySegment);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public NativeVector3D multiply(double m) {
        try {
            MULTIPLY_SCALAR_MH.invokeExact(memorySegment, m);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public NativeVector3D normalize() {
        try {
            NORMALIZE_MH.invokeExact(memorySegment);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public NativeVector3D crossProduct(Vector3D o) {
        NativeVector3D other = (NativeVector3D) o;
        CROSS_PRODUCT_MH.invokeExact(memorySegment, other.memorySegment); // No try-catch
        return this;
    }

    @Override
    public NativeVector3D add(Vector3D o) {
        if (!(o instanceof NativeVector3D other)) {
            throw new IllegalArgumentException("Provided vector must be a NativeVector3D");
        }
        try {
            ADD_MH.invokeExact(memorySegment, other.memorySegment);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public NativeVector3D subtract(Vector3D o) {
        if (!(o instanceof NativeVector3D other)) {
            throw new IllegalArgumentException("Provided vector must be a NativeVector3D");
        }
        try {
            SUBTRACT_MH.invokeExact(memorySegment, other.memorySegment);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public NativeVector3D multiply(Vector3D o) {
        if (!(o instanceof NativeVector3D other)) {
            throw new IllegalArgumentException("Provided vector must be a NativeVector3D");
        }
        try {
            int result = (int) MULTIPLY_VECTOR_MH.invokeExact(this.memorySegment, other.memorySegment);
            if (result != 0) {
                throw new RuntimeException("Error in native multiply_vector function. Error code: " + result);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public double distance(Vector3D o) {
        if (!(o instanceof NativeVector3D)) {
            throw new IllegalArgumentException("Incompatible Vector3D type");
        }
        NativeVector3D other = (NativeVector3D) o;
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public double distanceSquared(Vector3D o) {
        if (!(o instanceof NativeVector3D)) {
            throw new IllegalArgumentException("Incompatible Vector3D type");
        }
        NativeVector3D other = (NativeVector3D) o;
        try {
            return (double) DISTANCE_SQUARED_MH.invokeExact(this.memorySegment, other.memorySegment);
        } catch (Throwable e) {
            throw new RuntimeException("Error invoking native distanceSquared", e);
        }
    }

    @Override
    public double dot(Vector3D o) {
        if (!(o instanceof NativeVector3D)) {
            throw new IllegalArgumentException("Incompatible Vector3D type");
        }
        NativeVector3D other = (NativeVector3D) o;
        try {
            return (double) DOT_MH.invokeExact(this.memorySegment, other.memorySegment);
        } catch (Throwable e) {
            throw new RuntimeException("Error invoking native dot product", e);
        }
    }

    @Override
    public Vector3D clone() {
        try {
            MemorySegment clonedSegment = GLOBAL_ARENA.allocate(VECTOR_LAYOUT);
            MemorySegment.copy(this.memorySegment, 0, clonedSegment, 0, VECTOR_LAYOUT.byteSize());
            return new NativeVector3D(clonedSegment);
        } catch (Exception e) {
            throw new RuntimeException("Error cloning NativeVector3D", e);
        }
    }
}