package refutils;

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
     * Sets a value for a field in the instance object. The method uses
     * type-matching to set the field. This method can only be used if a class
     * has one field of the specified type.
     *
     * @param fieldValue The value that the field should be set to.
     * @throws IllegalAccessException Thrown if the field is final or otherwise inaccessable
     */
    public void setField(final Object fieldValue) throws IllegalAccessException, NoSuchFieldException {
        FieldHelper fieldHelper = new FieldHelper(instance);
        fieldHelper.setValue(fieldValue);
    }

    /**
     * Sets a value for a field in the instance object. This method can be used if a class
     * has more than one field of the specified type.
     *
     * @param fieldName  The name of the field
     * @param fieldValue The value that the field should be set to.
     * @throws NoSuchFieldException   Thrown if the field name is incorrect
     * @throws IllegalAccessException Thrown if the field is final or otherwise inaccessable
     */
    public void setField(final String fieldName, final Object fieldValue) throws NoSuchFieldException,
            IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance);
        fieldHelper.setValue(fieldName, fieldValue);
    }

    /**
     * Gets the value of a field in the instance object. This method can be used if a class
     * has more than one field of the specified type. The returned value must be casted to the field class.
     *
     * @param fieldName The name of the field
     * @return the value of the field
     * @throws NoSuchFieldException   Thrown if the field name is incorrect
     * @throws IllegalAccessException Thrown if the field is final or otherwise inaccessable
     */
    public Object getField(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        FieldHelper fieldHelper = new FieldHelper(instance);
        return fieldHelper.getValue(fieldName);
    }

    /**
     * Gets the value of a field in the instance object. The method uses
     * type-matching to set the field. This method can only be used if a class
     * has one field of the specified type.
     *
     * @param fieldClass the class of the field
     * @param <T>        field class
     * @return the value of the field with the right type
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public <T> T getField(Class<T> fieldClass) throws IllegalAccessException, NoSuchFieldException {
        FieldHelper fieldHelper = new FieldHelper(instance);
        return fieldHelper.getValue(fieldClass);
    }

}
