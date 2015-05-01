package refutils.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.*;

import java.io.File;
import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author bjorn
 * @since 2013-10-08
 */
public class FieldHelperTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void gettingInheritedPrivateFieldShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field for class java.io.FileNotFoundException"));

        new FieldHelper(new SubClass(), SubClass.class).getValue(FileNotFoundException.class);
    }

    @Test
    public void objectInstanceShouldNotBeSetWithClassReference() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Cannot match Object.class type parameter, you must specify it by name"));

        new FieldHelper(new SubClass(), SubClass.class).setValue(new Object());
    }

    @Test
    public void objectInstanceShouldBeSetWithNameReference() throws Exception {
        SubClass instance = new SubClass();
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);

        fieldHelper.setValue("anObject", new Object());

        assertThat(fieldHelper.getValue("anObject"), not(nullValue()));
    }

    @Test
    public void primitiveAndClassFieldsShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("" +
                "Found too many (2) matches for field class java.lang.Float [aFloat, aFloat2], " +
                "specify the field by name instead"));

        new FieldHelper(new SubClass(), SubClass.class).getValue(Float.class);
    }

    @Test
    public void settingFieldClassThatDontExistShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field for class java.lang.StringBuffer"));

        new FieldHelper(new SubClass(), SubClass.class).setValue(new StringBuffer());
    }

    @Test
    public void settingFieldNameThatDontExistShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field named gurka"));

        new FieldHelper(new SubClass(), SubClass.class).setValue("gurka", "");
    }

    @Test
    public void settingFieldWithWrongClassShouldThrowException() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Can not set java.lang.Runnable field refutils.testclasses.SubClass.override to java.lang.Integer"));

        FieldHelper fieldHelper = new FieldHelper(new SubClass(), SubClass.class);
        fieldHelper.setValue("override", 43);
    }

    @Test
    public void settingNamedFieldWithSubClassShouldWork() throws Exception {
        FieldClass instance = new FieldClass();
        FieldHelper fieldHelper = new FieldHelper(instance, FieldClass.class);

        fieldHelper.setValue("superClass", new SubClass());

        assertThat(fieldHelper.getValue("superClass"), not(nullValue()));
    }

    @Test
    public void fieldsWithCommonSuperClassShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is(
                "Found too many (3) matches for field class refutils.testclasses.SubClass [anInterface, subClass, superClass], " +
                        "specify the field by name instead"));

        new FieldHelper(new FieldClass(), FieldClass.class).getValue(SubClass.class);
    }

    @Test
    public void fieldsWithCommonInterfaceShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is(
                "Found too many (2) matches for field class refutils.testclasses.SuperClass [anInterface, superClass], " +
                        "specify the field by name instead"));

        new FieldHelper(new FieldClass(), FieldClass.class).getValue(SuperClass.class);
    }

    @Test
    public void settingInterfaceFieldShouldWork() throws Exception {
        FieldClass instance = new FieldClass();
        FieldHelper fieldHelper = new FieldHelper(instance, FieldClass.class);

        fieldHelper.setValue(new Interface() {
            @Override
            public int interfaceMethod(long f) {
                return 0;
            }
        });

        assertThat(fieldHelper.getValue(Interface.class), not(nullValue()));
    }

    @Test
    public void settingPrimitiveFieldsShouldWork() throws Exception {
        testPrimitive(true);
        testPrimitive(42);
        testPrimitive((byte) 42);
        testPrimitive((short) 42);
        testPrimitive((char) 42);
        testPrimitive((double) 42);
    }

    private void testPrimitive(Object testValue) throws NoSuchFieldException, IllegalAccessException {
        SubClass instance = new SubClass();
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);

        fieldHelper.setValue(testValue);

        fieldHelper = new FieldHelper(instance, SubClass.class);

        assertThat(fieldHelper.getValue(testValue.getClass()), is(testValue));
    }

    @Test
    public void settingStaticProtectedInheritedFieldShouldWork() throws Exception {
        SubClass instance = new SubClass();
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);
        File fieldValue = new File("gurka.txt");

        fieldHelper.setValue(fieldValue);

        assertThat(fieldHelper.getValue(fieldValue.getClass()).toString(), is("gurka.txt"));
    }
    
    @Test
    public void privateFieldsInSuperClassShouldBeReachable() throws Exception {
        SubClass instance = new SubClass();
        
        FieldHelper fieldHelper = new FieldHelper(instance, SuperClass.class);
        fieldHelper.getValue("fnfex");
    }

    @Test
    public void privateFieldsInSuperClassShouldBeModifiable() throws Exception {
        SubClass instance = new SubClass();
        FileNotFoundException ex = new FileNotFoundException("Gurka");
        
        FieldHelper fieldHelper = new FieldHelper(instance, SuperClass.class);
        fieldHelper.setValue(ex);
        
        assertThat(getExceptionMessage(ex), is("Gurka"));
        assertThat(getExceptionMessage(instance.getFnfex()), is("Gurka"));
        assertThat(getExceptionMessage(fieldHelper.getValue("fnfex")), is("Gurka"));
        assertThat(getExceptionMessage(fieldHelper.getValue(FileNotFoundException.class)), is("Gurka"));

        ex = new FileNotFoundException("Tomat");
        fieldHelper.setValue("fnfex", ex);

        assertThat(getExceptionMessage(instance.getFnfex()), is("Tomat"));
    }
    
    private String getExceptionMessage(Object ex) {
        return ((Exception) ex).getMessage();
    }

    @Test
    public void checkThatSubclassIsCorrect() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Instance of SubClass is not a subclass of SneakyConstructor");
        
        SubClass instance = new SubClass();
        new FieldHelper(instance, SneakyConstructor.class);
    }

    @Test
    public void checkThatSubclassIsCorrect2() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Instance of SubClass is not a subclass of String");
        
        SubClass instance = new SubClass();
        new FieldHelper(instance, String.class);
    }

    @Test
    public void checkThatSubclassIsCorrect3() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Instance of SuperClass is not a subclass of SubClass");
        
        SuperClass instance = new SuperClass((byte) 0);
        new FieldHelper(instance, SubClass.class);
    }

    @Test
    public void specifyingClassShouldBeOkButUnnecessary() throws Exception {
        SubClass instance = new SubClass();
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);

        fieldHelper.setValue("anObject", new Object());

        assertThat(fieldHelper.getValue("anObject"), not(nullValue()));
    }
    
}
