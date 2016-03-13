package refutils;

import org.junit.Test;
import refutils.testclasses.SubClass;
import refutils.testclasses.SuperClass;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

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
        new ReflectionHelper(instance, SuperClass.class).setField(fileNotFoundException);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance, SuperClass.class);
        FileNotFoundException field = reflectionHelper.getField(FileNotFoundException.class);
        assertThat(field.getMessage(), is("Gurka"));

        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "fnfex", FileNotFoundException.class, fileNotFoundException);
    }

    @Test
    public void privateNamedFieldsInSuperClassShouldBeReachable() {
        SubClass instance = new SubClass();

        FileNotFoundException fileNotFoundException = new FileNotFoundException("Gurka");
        new ReflectionHelper(instance, SuperClass.class).setField("fnfex", fileNotFoundException);

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance, SuperClass.class);
        FileNotFoundException field = (FileNotFoundException) reflectionHelper.getField("fnfex");
        assertThat(field.getMessage(), is("Gurka"));

        assertGetFieldWithBothTypedAndNamed(reflectionHelper, "fnfex", FileNotFoundException.class, fileNotFoundException);
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

    private void assertGetFieldWithBothTypedAndNamed(ReflectionHelper helper, String fieldName, Class fieldType, Object expectedFieldValue) {
        assertThat(helper.getField(fieldName), is(expectedFieldValue));
        assertThat(helper.getField(fieldType), is(expectedFieldValue));
    }
}
