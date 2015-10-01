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
    private Field staticFinalModifierField = null;

    MakeFieldAccessible(Field field) {
        this.field = field;
        accessibleState = field.isAccessible();
        field.setAccessible(true);

        if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
            try {
                staticFinalModifierField = Field.class.getDeclaredField("modifiers");
                staticFinalModifierField.setAccessible(true);
                staticFinalModifierField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            } catch (NoSuchFieldException ex) {
                throw new IllegalStateException("Internal error: Could not make " + field.toGenericString() + " accessable", ex);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Internal error: Could not make " + field.toGenericString() + " accessable", ex);
            }

        }
    }

    void restoreAccessState() throws IllegalAccessException {
        field.setAccessible(accessibleState);
        if (staticFinalModifierField != null) {
            staticFinalModifierField.setInt(field, field.getModifiers() | ~Modifier.FINAL);
        }
    }
}
