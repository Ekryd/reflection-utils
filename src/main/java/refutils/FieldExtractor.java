package refutils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class FieldExtractor {
    private final Set<Field> allFields = new TreeSet<Field>(new FieldComparator());
    private final Class instanceClass;

    public FieldExtractor(Class instanceClass) {
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

    boolean startsWith(String string, String... matches) {
        for (String match : matches) {
            if (string.startsWith(match)) {
                return true;
            }
        }
        return false;
    }

    public Set<Field> getAllFields() {
        return allFields;
    }
}