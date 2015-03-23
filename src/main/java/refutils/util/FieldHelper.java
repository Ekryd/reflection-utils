package refutils.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains utility methods to get and set field value from an instance of a class. The field can be located by class
 * type or field name.
 */
public class FieldHelper {
    private final Object instance;
    private final Collection<Field> allFields;

    /**
     * Instantiates a FieldHelper.
     *
     * @param instance the instance where the fields are located
     */
    public FieldHelper(Object instance) {
        this.instance = instance;
        this.allFields = new FieldExtractor(instance.getClass()).getAllFields();
    }

    /**
     * Instantiates a FieldHelper.
     *
     * @param instance the instance where the fields are located
     */
    public FieldHelper(Class<?> fieldsBaseClass, Object instance) {
        this.instance = instance;
        this.allFields = new FieldExtractor(fieldsBaseClass).getAllFields();
    }

    /**
     * Get the value for the named field
     *
     * @param fieldName the name of the field
     * @return the field value
     * @throws IllegalAccessException if the underlying field is inaccessible.
     * @throws NoSuchFieldException   thrown if field cannot be located
     */
    public Object getValue(String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = getFieldByName(fieldName);
        MakeFieldAccessible makeFieldAccessible = new MakeFieldAccessible(field);

        Object returnValue = field.get(instance);

        makeFieldAccessible.restoreAccessState();

        return returnValue;
    }

    /**
     * Get the value for the matching field by looking at class type.
     *
     * @param valueClass the class type of field
     * @param <T>        the type of the field
     * @return the field value
     * @throws IllegalAccessException This should never happen, since the field is always made accessible.
     * @throws NoSuchFieldException   thrown if field cannot be located
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> valueClass) throws IllegalAccessException, NoSuchFieldException {
        checkForObjectValueClass(valueClass);

        Field field = getFieldByType(valueClass);
        MakeFieldAccessible makeFieldAccessible = new MakeFieldAccessible(field);

        Object returnValue = field.get(instance);

        makeFieldAccessible.restoreAccessState();

        return (T) returnValue;
    }

    /**
     * Inserts a value for the named field
     *
     * @param fieldName the name of the field
     * @param value     the field value to set
     * @throws IllegalAccessException This should never happen, since the field is always made accessible.
     * @throws NoSuchFieldException   thrown if field cannot be located
     */
    public void setValue(String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field field = getFieldByName(fieldName);
        MakeFieldAccessible makeFieldAccessible = new MakeFieldAccessible(field);

        field.set(instance, value);

        makeFieldAccessible.restoreAccessState();
    }

    /**
     * Inserts a value for the matching field by looking at the value class type.
     *
     * @param value the field value to set
     * @throws IllegalAccessException if the underlying field is inaccessible.
     * @throws NoSuchFieldException   thrown if field cannot be located
     */
    public void setValue(Object value) throws IllegalAccessException, NoSuchFieldException {
        checkForObjectValueClass(value.getClass());

        Field field = getFieldByType(value.getClass());
        MakeFieldAccessible makeFieldAccessible = new MakeFieldAccessible(field);

        field.set(instance, value);

        makeFieldAccessible.restoreAccessState();
    }

    private Field getFieldByName(String fieldName) throws NoSuchFieldException {
        for (Field potentialField : allFields) {
            if (potentialField.getName().equals(fieldName)) {
                return potentialField;
            }
        }
        throw new NoSuchFieldException(String.format("Cannot find visible field named %s", fieldName));
    }

    private void checkForObjectValueClass(Class valueClass) {
        if (valueClass == Object.class) {
            throw new IllegalArgumentException("Cannot match Object.class type parameter, you must specify it by name");
        }
    }

    private Field getFieldByType(Class valueClass) throws NoSuchFieldException {
        Collection<Field> matchingFields = allFields;

        matchingFields = filterOnTypeMatches(matchingFields, valueClass);
        if (matchingFields.size() == 0) {
            throw new NoSuchFieldException(String.format("Cannot find visible field for %s", valueClass));
        }
        if (matchingFields.size() > 1) {
            throw new IllegalArgumentException(String.format("Found too many (%s) matches for field %s %s, specify the field by name instead", matchingFields.size(),
                    valueClass, extractFieldNames(matchingFields)));
        }

        return matchingFields.iterator().next();
    }

    private Collection<Field> filterOnTypeMatches(Collection<Field> matchingFields, Class valueClass) {
        List<Field> returnValue = new ArrayList<Field>();
        PrimitiveMatcher primitiveMatcher = new PrimitiveMatcher(valueClass);

        for (Field matchingField : matchingFields) {
            if (matchingField.getType() != Object.class && (primitiveMatcher.isMatchedPrimitive(matchingField.getType()) || matchingField.getType().isAssignableFrom(valueClass))) {
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

}
