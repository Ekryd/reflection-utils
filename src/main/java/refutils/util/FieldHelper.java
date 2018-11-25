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
    private final Class<?> classContainingField;

    /**
     * Instantiates a FieldHelper. By specifying a superclass, the FieldHelper can be used to reach hidden private
     * variables in the super class
     *
     * @param instance             the instance that should be manipulated
     * @param classContainingField the super class of the instance where the fields are defined
     */
    public FieldHelper(Object instance, Class<?> classContainingField) {
        if (!classContainingField.isInstance(instance)) {
            throw new IllegalArgumentException(String.format("Instance of %s is not a subclass of %s",
                    instance.getClass().getSimpleName(),
                    classContainingField.getSimpleName()));
        }
        this.instance = instance;
        this.allFields = new FieldExtractor(classContainingField).getAllFields();
        this.classContainingField = classContainingField;
    }

    /**
     * Get the value for the named field
     *
     * @param fieldName the name of the field
     * @return the field value
     * @throws IllegalAccessException if the underlying field is inaccessible.
     * @throws NoSuchFieldException   thrown if field cannot be located
     */
    public Object getValueByName(String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field = getFieldByName(fieldName, classContainingField, allFields);
        MakeFieldAccessible makeFieldAccessible = new MakeFieldAccessible(field);

        Object returnValue = field.get(instance);

        makeFieldAccessible.restoreAccessState();

        return returnValue;
    }

    /**
     * Get the value for the matching field by looking at class type.
     *
     * @param <T>        the type of the field
     * @param valueClass the class type of field
     * @return the field value
     * @throws IllegalAccessException This should never happen, since the field is always made accessible.
     * @throws NoSuchFieldException   thrown if field cannot be located
     */
    @SuppressWarnings("unchecked")
    public <T> T getValueByType(Class<T> valueClass) throws IllegalAccessException, NoSuchFieldException {
        checkForObjectValueClass(valueClass);

        Field field = getFieldByType(valueClass, classContainingField, allFields);
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
    public void setValueByName(String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field field = getFieldByName(fieldName, classContainingField, allFields);
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
    public void setValueByType(Object value) throws IllegalAccessException, NoSuchFieldException {
        checkForObjectValueClass(value.getClass());

        Field field = getFieldByType(value.getClass(), classContainingField, allFields);
        MakeFieldAccessible makeFieldAccessible = new MakeFieldAccessible(field);

        field.set(instance, value);

        makeFieldAccessible.restoreAccessState();
    }

    private void checkForObjectValueClass(Class valueClass) {
        if (valueClass == Object.class) {
            throw new IllegalArgumentException("Cannot match Object.class type parameter, you must specify it by name");
        }
    }

    private Field getFieldByName(String fieldName, Class<?> classContainingField, Collection<Field> classFields) throws NoSuchFieldException {
        for (Field potentialField : classFields) {
            if (potentialField.getName().equals(fieldName)) {
                return potentialField;
            }
        }

        Class<?> superclass = classContainingField.getSuperclass();
        if (superclass == null) {
            throw new NoSuchFieldException(String.format("Cannot find visible field named %s", fieldName));
        }

        return getFieldByName(fieldName, superclass, new FieldExtractor(superclass).getAllFields());
    }

    private Field getFieldByType(Class valueClass, Class<?> classContainingField, Collection<Field> classFields) throws NoSuchFieldException {
        Collection<Field> matchingFields = filterOnTypeMatches(classFields, valueClass);

        if (matchingFields.size() > 1) {
            throw new IllegalArgumentException(String.format("Found too many (%s) matches for field %s %s, specify the field by name instead", matchingFields.size(),
                    valueClass, extractFieldNames(matchingFields)));
        }

        if (matchingFields.size() == 1) {
            return matchingFields.iterator().next();
        }

        Class<?> superclass = classContainingField.getSuperclass();
        if (superclass == null) {
            throw new NoSuchFieldException(String.format("Cannot find visible field for %s", valueClass));
        }

        return getFieldByType(valueClass, superclass, new FieldExtractor(superclass).getAllFields());
    }

    private Collection<Field> filterOnTypeMatches(Collection<Field> matchingFields, Class valueClass) {
        List<Field> returnValue = new ArrayList<>();
        PrimitiveMatcher primitiveMatcher = new PrimitiveMatcher(valueClass);

        for (Field matchingField : matchingFields) {
            if (matchingField.getType() != Object.class && (primitiveMatcher.isMatchedPrimitive(matchingField.getType()) || matchingField.getType().isAssignableFrom(valueClass))) {
                returnValue.add(matchingField);
            }
        }
        return returnValue;
    }

    private String extractFieldNames(Collection<Field> matchingFields) {
        List<String> fieldNames = new ArrayList<>();
        for (Field matchingField : matchingFields) {
            fieldNames.add(matchingField.getName());
        }
        return fieldNames.toString();
    }

}
