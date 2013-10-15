package refutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains utility methods to get and set field value from an instance of a class. The field can be located by class
 * type or field name.
 */
class FieldHelper {
    private final Class<?> valueClass;
    private final Collection<Field> allFields;

    private final Field field;

    /**
     * Locates matching field in instance by looking at field name.
     *
     * @param instanceClass the instance where the field is located
     * @param fieldName     the name of the field
     * @throws NoSuchFieldException thrown if field cannot be located
     */
    public FieldHelper(Class instanceClass, String fieldName) throws NoSuchFieldException {
        this.valueClass = null;
        this.allFields = new FieldExtractor(instanceClass).getAllFields();

        field = getField(fieldName);
    }

    private Field getField(String fieldName) throws NoSuchFieldException {
        for (Field potentialField : allFields) {
            if (potentialField.getName().equals(fieldName)) {
                return potentialField;
            }
        }
        throw new NoSuchFieldException(String.format("Cannot find visible field named %s", fieldName));
    }

    /**
     * Locates matching field in an instance by looking at field class type.
     *
     * @param instanceClass the instance where the field is located
     * @param valueClass    the class type of the desired field
     * @throws NoSuchFieldException thrown if field cannot be located
     */
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
            throw new IllegalArgumentException(String.format("Found too many (%s) matches for field %s %s, specify the field by name instead", matchingFields.size(),
                    valueClass, extractFieldNames(matchingFields)));
        }

        return matchingFields.iterator().next();
    }

    private Collection<Field> filterOnTypeMatches(Collection<Field> matchingFields) {
        List<Field> returnValue = new ArrayList<Field>();
        for (Field matchingField : matchingFields) {
            if (matchingField.getType() != Object.class && (matchingField.getType().isAssignableFrom(valueClass) || isMatchedPrimitive(matchingField.getType()))) {
                returnValue.add(matchingField);
            }
        }
        return returnValue;
    }

    private String extractFieldNames(Collection<Field> matchingFields) {
        List<String> fieldNames = new ArrayList<String>();
        for (Field matchingField : matchingFields) {
            fieldNames.add(matchingField.getName());
        }
        return fieldNames.toString();
    }

    @SuppressWarnings("RedundantIfStatement")
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
        return boxedPrimitiveClass.equals(valueClass) && primitiveClass.equals(fieldType);
    }

    /**
     * Get the value for the located field
     *
     * @param instance the instance of the class containing the field
     * @return the field value
     * @throws IllegalAccessException if the underlying field is inaccessible.
     */
    public Object getValue(Object instance) throws IllegalAccessException {
        boolean accessibleState = field.isAccessible();
        field.setAccessible(true);

        Object returnValue = field.get(instance);

        field.setAccessible(accessibleState);

        return returnValue;
    }

    /**
     * Inserts a value for the located field
     *
     * @param instance the instance of the class containing the field
     * @return the field value
     * @throws IllegalAccessException if the underlying field is inaccessible.
     */
    public void setValue(Object instance, Object value) throws IllegalAccessException {
        boolean accessibleState = field.isAccessible();
        field.setAccessible(true);

        field.set(instance, value);

        field.setAccessible(accessibleState);
    }

}
