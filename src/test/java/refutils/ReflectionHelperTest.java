package refutils;

import org.junit.Test;
import refutils.testclasses.SubClass;
import refutils.testclasses.SuperClass;
import refutils.testclasses.SuperSuperClass;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
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
        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "longPrivate", Long.class, 42L);
    }

    @Test
    public void settingPrivateFieldShouldSetValue() {
        SuperClass instance = new SuperClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("Gurka");

        assertThat(instance.getStringPrivate(), is("Gurka"));

        reflectionHelper = new ReflectionHelper(instance);
        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "stringPrivate", String.class, "Gurka");
    }

    @Test
    public void settingInheritedFieldShouldSetValueInSuperClass() {
        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(34);

        assertThat(instance.getIntPackage(), is(34));
        reflectionHelper = new ReflectionHelper(instance);
        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "intPackage", Integer.class, 34);
    }

    @Test
    public void settingNamedFieldShouldSetValue() {
        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("intPackage", 34);

        assertThat(instance.getIntPackage(), is(34));

        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "intPackage", Integer.class, 34);
    }

    @Test
    public void privateFieldsInSuperClassShouldBeReachable() {
        SubClass instance = new SubClass();

        FileNotFoundException fileNotFoundException = new FileNotFoundException("Gurka");
        new ReflectionHelper(instance).setField(fileNotFoundException);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        FileNotFoundException field = reflectionHelper.getField(FileNotFoundException.class);
        assertThat(field.getMessage(), is("Gurka"));

        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "fnfex", FileNotFoundException.class, fileNotFoundException);
    }

    @Test
    public void privateNamedFieldsInSuperClassShouldBeReachable() {
        //When using named fields, traverse the superclasses
        SubClass instance = new SubClass();

        FileNotFoundException fileNotFoundException = new FileNotFoundException("Gurka");
        new ReflectionHelper(instance).setField("fnfex", fileNotFoundException);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        FileNotFoundException field = (FileNotFoundException) reflectionHelper.getField("fnfex");
        assertThat(field.getMessage(), is("Gurka"));

        assertGetFieldWithBothTypedAndNamed(new ReflectionHelper(instance, SuperClass.class), "fnfex", FileNotFoundException.class, fileNotFoundException);
    }

    @Test
    public void privateNamedFieldsInSuperSuperClassShouldBeReachable() {
        //When using named fields, traverse the superclasses
        SubClass instance = new SubClass();

        new ReflectionHelper(instance).setField("superSneakyField", "Find me!");

        assertThat(new ReflectionHelper(instance).getField("superSneakyField").toString(), is("Find me!"));
        assertThat(instance.getSuperSneakyField(), is("Find me!"));

        assertGetFieldWithBothTypedAndNamed(new ReflectionHelper(instance, SuperSuperClass.class), "superSneakyField", String.class, "Find me!");
    }

    @Test
    public void getAndSetFieldOnDifferentClassesShouldWork() {
        //When using named fields, traverse the superclasses
        SubClass instance = new SubClass();

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("superSneakyField", "Find me!");
        reflectionHelper.setField("stringPrivate2", "a String");
        reflectionHelper.setField("stringPrivate", "another String");

        reflectionHelper = new ReflectionHelper(instance);
        assertThat(reflectionHelper.getField("superSneakyField"), is("Find me!"));
        assertThat(reflectionHelper.getField("stringPrivate"), is("another String"));
        assertThat(reflectionHelper.getField("stringPrivate2"), is("a String"));
        assertThat(reflectionHelper.getField("stringPrivate"), is("another String"));
        assertThat(reflectionHelper.getField("superSneakyField"), is("Find me!"));
    }

    @Test
    public void settingFinalNonStaticFieldShouldWork() {
        SubClass instance = new SubClass();

        new ReflectionHelper(instance).setField("state", Thread.State.TIMED_WAITING);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "state", Thread.State.class, Thread.State.TIMED_WAITING);
    }

    @Test
    public void settingFinalNonStaticFieldShouldWork2() {
        SubClass instance = new SubClass();

        new ReflectionHelper(instance).setField(Thread.State.TIMED_WAITING);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "state", Thread.State.class, Thread.State.TIMED_WAITING);
    }

    @Test
    public void settingFinalStaticFieldShouldWork() {
        SubClass instance = new SubClass();

        new ReflectionHelper(instance).setField("FINAL_FIELD", TimeUnit.MINUTES);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "FINAL_FIELD", TimeUnit.class, TimeUnit.MINUTES);
    }

    @Test
    public void settingFinalStaticFieldShouldWork2() {
        SubClass instance = new SubClass();

        new ReflectionHelper(instance).setField(TimeUnit.MINUTES);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "FINAL_FIELD", TimeUnit.class, TimeUnit.MINUTES);
    }

    private void assertGetFieldWithBothTypedAndNamed(ReflectionHelper helper, String fieldName, Class<?> fieldType, Object expectedFieldValue) {
        assertThat(helper.getField(fieldName), is(expectedFieldValue));
        assertThat(helper.getField(fieldType), is(expectedFieldValue));
    }
}
