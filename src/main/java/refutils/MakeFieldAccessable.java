package refutils;

import java.lang.reflect.Field;

/**
 * @author bjorn
 * @since 2013-10-17
 */
class MakeFieldAccessable {
    private final Field field;
    private final boolean accessibleState;

    MakeFieldAccessable(Field field) {
        this.field = field;
        accessibleState = field.isAccessible();
        field.setAccessible(true);
    }

    void restoreAccessState() {
        field.setAccessible(accessibleState);
    }
}
