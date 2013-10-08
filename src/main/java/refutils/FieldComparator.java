package refutils;

import java.lang.reflect.Field;
import java.util.Comparator;

    /**
 * @author bjorn
 * @since 2013-10-07
 */
    class FieldComparator implements Comparator<Field> {
        @Override
        public int compare(Field field, Field field2) {
            int compare = field.getName().compareTo(field2.getName());
            if (compare != 0)
                return compare;
            return field.getType().getName().compareTo(field.getType().getName());
        }
    }
