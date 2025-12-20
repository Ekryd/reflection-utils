package refutils.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class to handle constructors.
 *
 * @param <T> The type to instantiate
 * @author exbjek
 */
public final class ConstructorHelper<T> {
  private final Class<T> clazz;

  public ConstructorHelper(final Class<T> clazz) {
    this.clazz = clazz;
  }

  /**
   * Instantiate private empty constructor.
   *
   * @return the instance
   * @throws NoSuchMethodException if a matching method is not found.
   * @throws IllegalAccessException should never be thrown since the constructor is made accessible
   * @throws InvocationTargetException if the underlying constructor throws an exception.
   * @throws InstantiationException if the class that declares the underlying constructor represents
   *     an abstract class.
   */
  public T instantiatePrivate()
      throws NoSuchMethodException,
          IllegalAccessException,
          InvocationTargetException,
          InstantiationException {
    Constructor<T> constructor = clazz.getDeclaredConstructor();
    constructor.setAccessible(true);
    return constructor.newInstance();
  }
}
