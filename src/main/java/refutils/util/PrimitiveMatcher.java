package refutils.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Matches primitive classes with their boxed class type. I.e int and Integer matches
 *
 * @author bjorn
 * @since 2013-10-17
 */
class PrimitiveMatcher {
    private final Class matchedPrimitiveType;

    private static final Map<Class, Class> PRIMITIVE_CLASSES_MAP = new LinkedHashMap<>();

    static {
        // Populate map with all java primitives and their corresponding autoboxing class.
        PRIMITIVE_CLASSES_MAP.put(Integer.class, int.class);
        PRIMITIVE_CLASSES_MAP.put(Long.class, long.class);
        PRIMITIVE_CLASSES_MAP.put(Double.class, double.class);
        PRIMITIVE_CLASSES_MAP.put(Float.class, float.class);
        PRIMITIVE_CLASSES_MAP.put(Byte.class, byte.class);
        PRIMITIVE_CLASSES_MAP.put(Character.class, char.class);
        PRIMITIVE_CLASSES_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_CLASSES_MAP.put(Short.class, short.class);
    }

    PrimitiveMatcher(Class valueClass) {
        this.matchedPrimitiveType = PRIMITIVE_CLASSES_MAP.get(valueClass);
    }

    boolean isMatchedPrimitive(Class<?> type) {
        return matchedPrimitiveType != null && type.equals(matchedPrimitiveType);
    }
}
