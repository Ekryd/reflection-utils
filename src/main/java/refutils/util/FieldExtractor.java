package refutils.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * Extracts all visible fields for a class. Visible fields are private, package protected, protected and public fields
 * of the class and package protected, protected and public fields of existing superclasses.
 * Fields that are hidden (same name and type) by a subclass are not available.
 * Internal fields in classes from Sun are not available
 */
class FieldExtractor {
    private final Set<Field> allFields = new TreeSet<Field>(new FieldComparator());
    private final Class instanceClass;

    /**
     * Extracts all fields for a class
     *
     * @param instanceClass the instance containing the fields
     */
    FieldExtractor(Class instanceClass) {
        this.instanceClass = instanceClass;
        scanForFields();
    }

    private void scanForFields() {
        allFields.addAll(Arrays.asList(instanceClass.getDeclaredFields()));
        scanForFieldsWithoutPrivate(instanceClass.getSuperclass());
    }

    private void scanForFieldsWithoutPrivate(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        String packageName = clazz.getPackage().getName();
        if (startsWith(packageName, "java.", "javax.", "sun.", "sunw.")) {
            return;
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!(Modifier.isPrivate(field.getModifiers()))) {
                allFields.add(field);
            }
        }
        scanForFieldsWithoutPrivate(clazz.getSuperclass());
    }

    private boolean startsWith(String string, String... matches) {
        for (String match : matches) {
            if (string.startsWith(match)) {
                return true;
            }
        }
        return false;
    }

    /** Returns all visible fields. */
    Set<Field> getAllFields() {
        return allFields;
    }
}