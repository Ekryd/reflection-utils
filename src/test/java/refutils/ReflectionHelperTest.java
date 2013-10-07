package refutils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.SubClass;
import refutils.testclasses.SuperClass;

import java.io.FileNotFoundException;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ReflectionHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void instantiateWithNoDefaultConstructorShouldThrowException() {
        try {
            assertNotNull(ReflectionHelper.instantiatePrivateConstructor(SuperClass.class));
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Cannot instantiate Class: refutils.testclasses.SuperClass constructor"));
        }
    }

    @Test
    public void settingPrivateNumericFieldShouldWork() throws Exception {
        SuperClass instance = new SuperClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(42L);

        assertThat(instance.getLongPrivate(), is(42L));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((Long) reflectionHelper.getField("longPrivate"), is(42L));
        assertThat(reflectionHelper.getField(Long.class), is(42L));
    }

    @Test
    public void settingPrivateStringFieldShouldWork() throws Exception {
        SuperClass instance = new SuperClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("Gurka");

        assertThat(instance.getStringPrivate(), is("Gurka"));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((String) reflectionHelper.getField("stringPrivate"), is("Gurka"));
        assertThat(reflectionHelper.getField(String.class), is("Gurka"));
    }

    @Test
    public void settingPrivateFieldInSubclassShouldWork() throws Exception {
        SubClass instance = new SubClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("Gurka");

        assertThat(instance.getStringPrivate2(), is("Gurka"));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((String) reflectionHelper.getField("stringPrivate2"), is("Gurka"));
        assertThat(reflectionHelper.getField(String.class), is("Gurka"));
    }

    @Test
    public void settingProtectedInheritedFieldShouldWork() throws Exception {
        SubClass instance = new SubClass((byte) 0);
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(34);

        assertThat(instance.getIntPackage(), is(34));

        reflectionHelper = new ReflectionHelper(instance);
        assertThat((Integer) reflectionHelper.getField("intPackage"), is(34));
        assertThat(reflectionHelper.getField(Integer.class), is(34));
    }

    @Test
    public void settingOverrideFieldShouldSetLowestLevel() throws Exception {
        SubClass instance = new SubClass((byte) 0);
        assertThat(instance.getOverride(), nullValue());
        assertThat(instance.getSuperOverride(), nullValue());

        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(new Runnable() {
            @Override
            public void run() {

            }
        });

        assertThat(instance.getOverride(), not(nullValue()));
        assertThat(instance.getSuperOverride(), nullValue());

        reflectionHelper = new ReflectionHelper(instance);
        assertThat(reflectionHelper.getField("override"), not(nullValue()));
        assertThat(reflectionHelper.getField(Runnable.class), not(nullValue()));
    }

    @Test
    public void settingInheritedPrivateFieldShouldNotWork() throws Exception {
        SubClass instance = new SubClass((byte) 0);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Cannot find field for class java.io.FileNotFoundException"));
        
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(new FileNotFoundException());
    }

    @Test
    public void testSetNumberField() throws Exception {
        Instance1 instance = new Instance1();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(42);
        assertEquals(42, instance.getNumber());
    }

    @Test
    public void testSetField2() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance0 instance = new Instance0();
        try {
            ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
            reflectionHelper.setField(class1);
            fail();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testSetField3() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance2 instance = new Instance2();
        try {
            ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
            reflectionHelper.setField(class1);
            fail();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testSetNamedField() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        FieldClass class1 = new FieldClass();
        Instance1 instance = new Instance1();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("fieldInterface", class1);
        assertEquals(class1, instance.getFieldInterface());
    }

    @Test
    public void testSetNamedField2() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance0 instance = new Instance0();
        try {
            ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
            reflectionHelper.setField("fieldInterface", class1);
            fail();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testSetNamedField3() throws Exception {
        FieldClass class1 = new FieldClass();
        Instance2 instance = new Instance2();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("fieldInterface2", class1);
        assertEquals(class1, instance.getFieldInterface2());
    }

    private class FieldClass implements FieldInterface {

    }

    private interface FieldInterface {

    }

    private class Instance0 {
    }

    private class Instance1 {
        private FieldInterface fieldInterface;
        private int number;

        public FieldInterface getFieldInterface() {
            return fieldInterface;
        }

        public int getNumber() {
            return number;
        }
    }

    private class Instance2 {
        private FieldInterface fieldInterface1;
        private FieldInterface fieldInterface2;

        public FieldInterface getFieldInterface1() {
            return fieldInterface1;
        }

        public FieldInterface getFieldInterface2() {
            return fieldInterface2;
        }

        public void setFieldInterface1(final FieldInterface fieldInterface1) {
            this.fieldInterface1 = fieldInterface1;
        }

        public void setFieldInterface2(final FieldInterface fieldInterface2) {
            this.fieldInterface2 = fieldInterface2;
        }

    }

    private static class Instance3 {
        private Instance3() {

        }
    }

}
