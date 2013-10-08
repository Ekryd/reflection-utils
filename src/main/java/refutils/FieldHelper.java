package refutils;

import java.lang.reflect.Field;
import java.util.*;

class FieldHelper {
    private final Class<?> valueClass;
    private final Collection<Field> allFields;

    private final Field field;
    
    public FieldHelper(Class instanceClass, String fieldName) throws NoSuchFieldException {
        this.valueClass = null;
        this.allFields = new FieldExtractor(instanceClass).getAllFields();

        field = getField(fieldName);
    }

   private Field getField(String fieldName) throws NoSuchFieldException {
        for (Field field : allFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        throw new NoSuchFieldException(String.format("Cannot find visible field named %s", fieldName));
    }

    public FieldHelper(Class instanceClass, Class<?> valueClass) throws NoSuchFieldException {
        this.valueClass = valueClass;
        checkForObjectValueClass();

        this.allFields = new FieldExtractor(instanceClass).getAllFields();
        field = getMatchingField();
    }
    
    private void checkForObjectValueClass() {
        if (valueClass == Object.class) {
            throw new IllegalArgumentException("Cannot match Object.class type parameter, you must specify it by name");
        }
    }

    private Field getMatchingField() throws NoSuchFieldException {
        Collection<Field> matchingFields = allFields;

        matchingFields = filterOnTypeMatches(matchingFields);
        if (matchingFields.size() == 0) {
            throw new NoSuchFieldException(String.format("Cannot find visible field for %s", valueClass));
        }
        if (matchingFields.size() > 1) {
            throw new IllegalArgumentException(String.format("Found %s matches for field %s %s", matchingFields.size(),
                    valueClass, extractFieldNames(matchingFields)));
        }

        return matchingFields.iterator().next();
    }

    private String extractFieldNames(Collection<Field> matchingFields) {
        List<String> fieldNames = new ArrayList<String>();
        for (Field matchingField : matchingFields) {
            fieldNames.add(matchingField.getName());
        }
        return fieldNames.toString();
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
