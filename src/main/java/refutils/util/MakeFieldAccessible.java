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

  /**
   * Creates entity and makes the field supplied accessible to set values.
   *
   * @param field the field to modify
   */
  MakeFieldAccessible(Field field) {
    this.field = field;
    boolean fieldIsStaticField = Modifier.isStatic(field.getModifiers());

    accessibleState = field.isAccessible();
    field.setAccessible(true);

    if (fieldIsStaticField && Modifier.isFinal(field.getModifiers())) {
      throw new IllegalArgumentException("Cannot make final static field accessible");
    }
  }

  void restoreAccessState() throws IllegalAccessException {
    field.setAccessible(accessibleState);
    if (staticFinalModifierField != null) {
      staticFinalModifierField.setInt(field, field.getModifiers() | ~Modifier.FINAL);
    }
  }
}
