package refutils;

/**
 * Common Runtime Exception for all reflection methods
 */
public class ReflectionHelperException extends RuntimeException {
    public ReflectionHelperException(Exception e) {
        super(e);
    }

    public ReflectionHelperException(String msg, Exception ex) {
        super(msg, ex);
    }

    public static ReflectionHelperException createCannotInstantiateClass(Class<?> clazz, Exception ex) {
        return new ReflectionHelperException(String.format("Cannot instantiate Class: %s constructor", clazz.getName()), ex);
    }
}
