package refutils;

import refutils.util.ConstructorHelper;
import refutils.util.FieldHelper;

import java.lang.reflect.InvocationTargetException;

/**
 * This class is used to set protected fields in classes and access private
 * constructors.
 */
public final class ReflectionHelper {
    /** The object that contains the field. */
    private final Object instance;

    /**
     * Instantiates a new reflection helper with the instance where the reflection operations are performed.
     *
     * @param instance the instance
     */
    public ReflectionHelper(final Object instance) {
        if (instance == null) {
            throw new NullPointerException("The instance in the ReflectionHelper cannot be null");
        }
        this.instance = instance;
    }

    /**
     * Instantiate private empty constructor.
     *
     * @param clazz the class to instantiate
     * @param <T>   type of the class to instantiate
     * @return the new instance
     */
    public static <T> T instantiatePrivateConstructor(final Class<T> clazz) {
        try {
            return new ConstructorHelper<T>(clazz).instantiatePrivate();
        } catch (NoSuchMethodException ex) {
            throw ReflectionHelperException.createCannotInstantiateClass(clazz, ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("This should never happen, since the constructor is always made accessible");
        } catch (InvocationTargetException ex) {
            throw ReflectionHelperException.createCannotInstantiateClass(clazz, ex);
        } catch (InstantiationException ex) {
            throw ReflectionHelperException.createCannotInstantiateClass(clazz, ex);
        }
    }

    /**
     * Sets a value for a field in the instance object. The method uses
     * type-matching to set the field. This method can only be used if a class
     * has one field of the specified type.
     *
     * @param fieldValue The value that the field should be set to.
     */
    public void setField(final Object fieldValue) {
        try {
            FieldHelper fieldHelper = new FieldHelper(instance);
            fieldHelper.setValue(fieldValue);
        } catch (IllegalAccessException ex) {
            throw new ReflectionHelperException(ex);
        } catch (NoSuchFieldException ex) {
            throw new ReflectionHelperException(ex);
        }
    }

    /**
     * Sets a value for a field in the instance object. This method can be used if a class
     * has more than one field of the specified type.
     *
     * @param fieldName  The name of the field
     * @param fieldValue The value that the field should be set to.
     */
    public void setField(final String fieldName, final Object fieldValue) {
        try {
            FieldHelper fieldHelper = new FieldHelper(instance);
            fieldHelper.setValue(fieldName, fieldValue);
        } catch (IllegalAccessException ex) {
            throw new ReflectionHelperException(ex);
        } catch (NoSuchFieldException ex) {
            throw new ReflectionHelperException(ex);
        }
    }

    /**
     * Gets the value of a field in the instance object. This method can be used if a class
     * has more than one field of the specified type. The returned value must be casted to the field class.
     *
     * @param fieldName The name of the field
     * @return the value of the field
     */
    public Object getField(String fieldName) {
        try {
            FieldHelper fieldHelper = new FieldHelper(instance);
            return fieldHelper.getValue(fieldName);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("This should never happen, since the field is always made accessible");
        } catch (NoSuchFieldException ex) {
            throw new ReflectionHelperException(ex);
        }
    }

    /**
     * Gets the value of a field in the instance object. The method uses
     * type-matching to set the field. This method can only be used if a class
     * has one field of the specified type.
     *
     * @param fieldClass the class of the field
     * @param <T>        field class
     * @return the value of the field with the right type
     */
    public <T> T getField(Class<T> fieldClass) {
        try {
            FieldHelper fieldHelper = new FieldHelper(instance);
            return fieldHelper.getValue(fieldClass);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("This should never happen, since the field is always made accessible");
        } catch (NoSuchFieldException ex) {
            throw new ReflectionHelperException(ex);
        }
    }

}
