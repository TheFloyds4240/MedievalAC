package ac.grim.grimac.utils.vector;

import ac.grim.grimac.utils.anticheat.LogUtil;

import java.lang.reflect.InvocationTargetException;

// Factory that loads once at startup
public final class VectorFactory {
    private static final VectorOperations INSTANCE;

    static {
        // This happens once during class initialization
        VectorOperations ops;
        try {
            if (isSimdApiAvailable()) {
                ops = (VectorOperations) Class
                        .forName("ac.grim.grimac.utils.vector.VectorOperationsJava18")
                        .getDeclaredConstructor()
                        .newInstance();
            } else {
                LogUtil.info("java.lang.vector.VectorSpecies not found. SIMD is unavailable falling back to Scalar Operations.");
                ops = new BasicVectorOps();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            LogUtil.error("SIMD is unavailable falling back to Scalar Operations: " + e.getMessage());
            ops = new BasicVectorOps();
        }
        INSTANCE = ops;
    }

    public static Vector3D newVector3D() {
        return INSTANCE.newVector(0 ,0, 0);
    }

    public static Vector3D newVector3D(int x, int y, int z) {
        return INSTANCE.newVector(x, y, z);
    }

    public static Vector3D newVector3D(float x, float y, float z) {
        return INSTANCE.newVector(x, y, z);
    }

    public static Vector3D newVector3D(double x, double y, double z) {
        return INSTANCE.newVector(x, y, z);
    }


    public static VectorOperations get() {
        return INSTANCE;
    }

    private static boolean isSimdApiAvailable() {
        int javaVersion = getJavaVersion();
        if (javaVersion >= 18) {
            try {
                // Attempt to load a class that uses the Vector API
                Class.forName("java.lang.vector.VectorSpecies");
                return true;
            } catch (ClassNotFoundException e) {
                // Vector API not available
                return false;
            }
        }
        return false;
    }

    private static int getJavaVersion() {
        String versionString = System.getProperty("java.version");
        if (versionString.startsWith("1.")) {
            versionString = versionString.substring(2, 3);
        } else {
            int dotIndex = versionString.indexOf(".");
            if (dotIndex != -1) {
                versionString = versionString.substring(0, dotIndex);
            }
        }
        try {
            return Integer.parseInt(versionString);
        } catch (NumberFormatException e) {
            // Fallback to assuming Java 8 if parsing fails
            return 8;
        }
    }
}
