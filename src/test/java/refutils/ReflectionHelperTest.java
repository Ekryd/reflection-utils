package refutils;

import org.junit.Test;
import refutils.testclasses.SubClass;
import refutils.testclasses.SuperClass;

import java.io.FileNotFoundException;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionHelperTest {

    @Test
    public void instantiatePrivateNonArgConstructorShouldWork() {
        assertNotNull(ReflectionHelper.instantiatePrivateConstructor(SubClass.class));
    }

    @Test
    public void settingPrimitiveFieldShouldSetValue() {
        SuperClass instance = new SuperClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(42L);

        assertThat(instance.getLongPrivate(), is(42L));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((Long) reflectionHelper.getField("longPrivate"), is(42L));
        assertThat(reflectionHelper.getField(Long.class), is(42L));
    }

    @Test
    public void settingPrivateFieldShouldSetValue() {
        SuperClass instance = new SuperClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("Gurka");

        assertThat(instance.getStringPrivate(), is("Gurka"));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((String) reflectionHelper.getField("stringPrivate"), is("Gurka"));
        assertThat(reflectionHelper.getField(String.class), is("Gurka"));
    }

    @Test
    public void settingInheritedFieldShouldSetValueInSuperClass() {
        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(34);

        assertThat(instance.getIntPackage(), is(34));
    }

    @Test
    public void settingNamedFieldShouldSetValue() {
        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("intPackage", 34);

        assertThat(instance.getIntPackage(), is(34));
    }

    @Test
    public void privateFieldsInSuperClassShouldBeReachable() {
        SubClass instance = new SubClass();

        new ReflectionHelper(instance, SuperClass.class).setField(new FileNotFoundException("Gurka"));
        FileNotFoundException field = new ReflectionHelper(instance, SuperClass.class).getField(FileNotFoundException.class);

        assertThat(field.getMessage(), is("Gurka"));
    }

}
