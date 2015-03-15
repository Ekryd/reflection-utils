package refutils.testclasses;

/**
 * @author bjorn
 * @since 15-03-15
 */
public class SneakyConstructor {
    SneakyConstructor() {
        throw new NullPointerException("Nah! Don't wanna");
    }
}
