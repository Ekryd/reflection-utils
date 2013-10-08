package refutils;

import org.junit.Test;
import refutils.testclasses.SubClass;
import refutils.testclasses.SubClassToThread;
import refutils.testclasses.SuperClass;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
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
        assertThat(fieldExtractor.getAllFields().size(), is(1));
        assertThat(fieldExtractor.getAllFields(), hasItem(SubClassToThread.class.getDeclaredField("something")));
    }


}
