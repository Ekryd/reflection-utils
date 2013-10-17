package refutils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.SubClass;
import refutils.testclasses.SubClassToThread;
import refutils.testclasses.SuperClass;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void instantiatePrivateNonArgConstructorShouldWork() {
        assertNotNull(ReflectionHelper.instantiatePrivateConstructor(SubClass.class));
    }

    @Test
    public void instantiateWithNoDefaultConstructorShouldThrowException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Cannot instantiate Class: refutils.testclasses.SubClassToThread constructor"));

        ReflectionHelper.instantiatePrivateConstructor(SubClassToThread.class);
    }

    @Test
    public void settingPrimitiveFieldShouldSetValue() throws Exception {
        SuperClass instance = new SuperClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(42L);

        assertThat(instance.getLongPrivate(), is(42L));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((Long) reflectionHelper.getField("longPrivate"), is(42L));
        assertThat(reflectionHelper.getField(Long.class), is(42L));
    }

    @Test
    public void settingPrivateFieldShouldSetValue() throws Exception {
        SuperClass instance = new SuperClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("Gurka");

        assertThat(instance.getStringPrivate(), is("Gurka"));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((String) reflectionHelper.getField("stringPrivate"), is("Gurka"));
        assertThat(reflectionHelper.getField(String.class), is("Gurka"));
    }

    @Test
    public void settingInheritedFieldShouldSetValueInSuperClass() throws Exception {
        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(34);

        assertThat(instance.getIntPackage(), is(34));
    }

    @Test
    public void settingNamedFieldShouldSetValue() throws Exception {
        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("intPackage", 34);

        assertThat(instance.getIntPackage(), is(34));
    }


}
