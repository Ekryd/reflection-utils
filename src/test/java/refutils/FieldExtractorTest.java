package refutils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import refutils.testclasses.SubClass;
import refutils.testclasses.SubClassToThread;
import refutils.testclasses.SuperClass;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author bjorn
 * @since 2013-10-08
 */
public class FieldExtractorTest {

    @Test
    public void classWithoutSuperClassShouldNotCrash() throws Exception {
        FieldExtractor fieldExtractor = new FieldExtractor(Object.class);
        assertThat(fieldExtractor.getAllFields(), not(nullValue()));
    }

    @Test
    public void privateFieldsInSuperClassShouldNotBeVisible() throws Exception {
        FieldExtractor fieldExtractor = new FieldExtractor(SubClass.class);
        assertThat(fieldExtractor.getAllFields(), hasItem(SubClass.class.getDeclaredField("stringPrivate2")));
        assertThat(fieldExtractor.getAllFields(), not(hasItem(SuperClass.class.getDeclaredField("stringPrivate"))));
    }

    @Test
    public void protectedInheritedFieldShouldBeAvailable() throws Exception {
        FieldExtractor fieldExtractor = new FieldExtractor(SubClass.class);
        assertThat(fieldExtractor.getAllFields(), hasItem(SuperClass.class.getDeclaredField("intPackage")));
    }

    @Test
    public void overriddenFieldShouldShowFieldInSubClass() throws Exception {
        FieldExtractor fieldExtractor = new FieldExtractor(SubClass.class);
        assertThat(fieldExtractor.getAllFields(), hasItem(SubClass.class.getDeclaredField("override")));
        assertThat(fieldExtractor.getAllFields(), not(hasItem(SuperClass.class.getDeclaredField("override"))));
    }

    @Test
    public void inheritedFieldsFromStandardLibraryShouldNotBeVisible() throws Exception {
        FieldExtractor fieldExtractor = new FieldExtractor(SubClassToThread.class);
        assertThat(fieldExtractor.getAllFields(), hasItem(SubClassToThread.class.getDeclaredField("something")));
        assertThat(fieldExtractor.getAllFields(), hasItem(SubClassToThread.class.getDeclaredField("rex")));
        assertThat(fieldExtractor.getAllFields(), everyItem(new IsNotFieldFromJdk()));
    }

    private class IsNotFieldFromJdk extends TypeSafeMatcher<Field> {


        /**
         * Subclasses should implement this. The item will already have been checked for
         * the specific type and will never be null.
         */
        @Override
        protected boolean matchesSafely(Field item) {
            String name = item.getDeclaringClass().getName();
            System.out.println(name);
            return !name.startsWith("java");
        }

        /**
         * Generates a description of the object.  The description may be part of a
         * a description of a larger object of which this is just a component, so it
         * should be worded appropriately.
         *
         * @param description The description to be built or appended to.
         */
        @Override
        public void describeTo(Description description) {
            description.appendText("not a field from a jdk class");
        }
    }
}
