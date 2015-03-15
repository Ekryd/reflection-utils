package refutils.util;

import java.lang.reflect.Field;

/**
 * @author bjorn
 * @since 2013-10-17
 */
class MakeFieldAccessible {
    private final Field field;
    private final boolean accessibleState;

    MakeFieldAccessible(Field field) {
        this.field = field;
        accessibleState = field.isAccessible();
        field.setAccessible(true);
    }

    void restoreAccessState() {
        field.setAccessible(accessibleState);
    }
}
