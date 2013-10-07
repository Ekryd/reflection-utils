package refutils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

class FieldHelper {
    private final Class<?> instanceClass;
    private final Class<?> valueClass;

    private final Set<Field> allFields = new TreeSet<Field>(new FieldComparator());

    private final Field field;

    public FieldHelper(Class instanceClass, String fieldName) throws SecurityException,
            NoSuchFieldException {
        this.instanceClass = instanceClass;
        this.valueClass = null;

        scanForFields();
        field = getField(fieldName);
    }

    private void scanForFields() {
        allFields.addAll(Arrays.asList(instanceClass.getDeclaredFields()));
        scanForFieldsWithoutPrivate(instanceClass.getSuperclass());
    }

    private void scanForFieldsWithoutPrivate(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        String packageName = clazz.getPackage().getName();
        if (startsWith(packageName, "java.", "javax.", "sun.", "sunw.")) {
            return;
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!(Modifier.isPrivate(field.getModifiers()))) {
                allFields.add(field);
            }
        }
        scanForFieldsWithoutPrivate(clazz.getSuperclass());
    }

    private boolean startsWith(String string, String... matches) {
        for (String match : matches) {
            if (string.matches(match)) {
                return true;
            }
        }
        return false;
    }

    private Field getField(String fieldName) {
        for (Field field : allFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        throw new IllegalArgumentException(String.format("Cannot find field named %s", fieldName));
    }

    public FieldHelper(Class instanceClass, Class<?> valueClass) {
        this.valueClass = valueClass;
        this.instanceClass = instanceClass;
        checkForObjectValueClass();

        scanForFields();
        field = getMatchingField();
    }

    private void checkForObjectValueClass() {
        if (valueClass == Object.class) {
            throw new IllegalArgumentException("Cannot match Object.class type parameter, you must specify it by name");
        }
    }

    private Field getMatchingField() {
        Collection<Field> matchingFields = allFields;

        matchingFields = filterOnTypeMatches(matchingFields);
        if (matchingFields.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find field for %s", valueClass));
        }
        if (matchingFields.size() > 1) {
            throw new IllegalArgumentException(String.format("Found %s matches for field %s", matchingFields.size(),
                    valueClass));
        }

        return matchingFields.iterator().next();
    }

    private Collection<Field> getAllFields() {
        LinkedHashSet<Field> returnValue = new LinkedHashSet<Field>();
        return returnValue;
    }

    private Collection<Field> filterOnTypeMatches(Collection<Field> fieldMatches) {
        List<Field> returnValue = new ArrayList<Field>();
        for (Field field : fieldMatches) {
            if (field.getType() != Object.class && (field.getType().isAssignableFrom(valueClass) || isMatchedPrimitive(field.getType()))) {
                returnValue.add(field);
            }
        }
        return returnValue;
    }

    private boolean isMatchedPrimitive(Class<?> fieldType) {
        if (matchPrimitive(fieldType, int.class, Integer.class)) {
            return true;
        }
        if (matchPrimitive(fieldType, long.class, Long.class)) {
            return true;
        }
        if (matchPrimitive(fieldType, double.class, Double.class)) {
            return true;
        }
        if (matchPrimitive(fieldType, float.class, Float.class)) {
            return true;
        }
        if (matchPrimitive(fieldType, byte.class, Byte.class)) {
            return true;
        }
        if (matchPrimitive(fieldType, char.class, Character.class)) {
            return true;
        }
        if (matchPrimitive(fieldType, boolean.class, Boolean.class)) {
            return true;
        }
        if (matchPrimitive(fieldType, short.class, Short.class)) {
            return true;
        }
        return false;
    }

    private boolean matchPrimitive(Class<?> fieldType, Class<?> primitiveClass, Class<?> boxedPrimitiveClass) {
        return boxedPrimitiveClass == valueClass && primitiveClass == fieldType;
    }

    public Object getValue(Object instance) throws IllegalAccessException {
        boolean accessibleState = field.isAccessible();
        field.setAccessible(true);

        Object returnValue = field.get(instance);

        field.setAccessible(accessibleState);

        return returnValue;
    }

    public void setValue(Object instance, Object value) throws IllegalAccessException {
        boolean accessibleState = field.isAccessible();
        field.setAccessible(true);

        field.set(instance, value);

        field.setAccessible(accessibleState);
    }

}
