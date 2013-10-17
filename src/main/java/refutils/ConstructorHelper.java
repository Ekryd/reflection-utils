package refutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class to handle constructors.
 *
 * @param <T>
 * @author exbjek
 */
final class ConstructorHelper<T> {
    private final Class<T> clazz;

    ConstructorHelper(final Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Instantiate private empty constructor.
     *
     * @return the instance
     */
    T instantiatePrivate() {
        try {
            return tryToInstantiatePrivate();
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Cannot instantiate Class: %s constructor", clazz.getName()), ex);
        }
    }

    private T tryToInstantiatePrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

}
