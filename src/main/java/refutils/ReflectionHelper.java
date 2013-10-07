package refutils;

import java.lang.reflect.InvocationTargetException;

/**
 * This class is used to set protected fields in classes and access private
 * constructors.
 */
public final class ReflectionHelper {
    /** The object that contains the field. */
    private final Object instance;

    /**
     * Instantiates a new reflection helper.
     *
     * @param instance the instance
     */
    public ReflectionHelper(final Object instance) {
        this.instance = instance;
    }

    /**
     * Instantiate private empty constructor.
     *
     * @param clazz the clazz to instantiate
     * @return the new instance
     */
    public static <T> T instantiatePrivateConstructor(final Class<T> clazz) {
        return new ConstructorHelper<T>(clazz).instantiatePrivate();
    }

    /**
     * Sets a private or protected field for an object. This method uses
     * type-matchning to set the field. This method can be used if a class only
     * has one field of the specified type.
     *
     * @param fieldValue The value that the field should be set to.
     * @throws IllegalAccessException Thrown if the field is final
     */
    public void setField(final Object fieldValue) throws IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldValue.getClass());
        fieldHelper.setValue(instance, fieldValue);
    }

    /**
     * Sets a named private or protected field for an object. Use the
     * type-matching setter if possible. This method can be used if a class only
     * has more than one field of the specified type.
     *
     * @param fieldName  The name of the field
     * @param fieldValue The value that the field should be set to.
     * @throws NoSuchFieldException   Thrown if the fieldname is incorrect
     * @throws IllegalAccessException Thrown if the field is final
     */
    public void setField(final String fieldName, final Object fieldValue) throws NoSuchFieldException,
            IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldName);
        fieldHelper.setValue(instance, fieldValue);
    }

    public Object getField(String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldName);
        Object returnValue = fieldHelper.getValue(instance);
        return returnValue;
    }

    @SuppressWarnings("unchecked")
    public <T> T getField(Class<T> fieldClass) throws IllegalArgumentException, IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance.getClass(), fieldClass);
        Object returnValue = fieldHelper.getValue(instance);
        return (T) returnValue;
    }

    public Object executeMethod(String methodName, Object... invocationValues) throws InvocationTargetException, IllegalAccessException {
        MethodHelper methodHelper = new MethodHelper(instance, methodName, invocationValues);
        return methodHelper.invoke();
    }

}
