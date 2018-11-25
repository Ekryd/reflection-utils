package refutils.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author bjorn
 * @since 2013-10-17
 */
class MakeFieldAccessible {
    private final Field field;
    private final boolean accessibleState;

    /**
     * Creates entity and makes the field supplied accessible to set values.
     *
     * @param field the field to modify
     * @param instance the instance containing the field
     */
    MakeFieldAccessible(Field field, Object instance) {
        this.field = field;
        boolean fieldIsStaticField = Modifier.isStatic(field.getModifiers());

        accessibleState = field.canAccess(fieldIsStaticField ? null : instance);
        field.setAccessible(true);

        if (fieldIsStaticField && Modifier.isFinal(field.getModifiers())) {
            throw new IllegalStateException("Since JDK9 it is no longer possible to modify static final fields. Could not make " + field.toGenericString() + " accessable");
        }
    }

    void restoreAccessState() {
        field.setAccessible(accessibleState);
    }
}
