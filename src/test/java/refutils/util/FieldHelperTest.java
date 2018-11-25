package refutils.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.ReflectionHelper;
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
    public void objectInstanceShouldNotBeSetWithClassReference() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Cannot match Object.class type parameter, you must specify it by name"));

        new FieldHelper(new SubClass(), SubClass.class).setValueByType(new Object());
    }

    @Test
    public void objectInstanceShouldBeSetWithNameReference() throws Exception {
        SubClass instance = new SubClass();
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);

        fieldHelper.setValueByName("anObject", new Object());

        assertThat(fieldHelper.getValueByName("anObject"), not(nullValue()));
    }

    @Test
    public void primitiveAndClassFieldsShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("" +
                "Found too many (2) matches for field class java.lang.Float [aFloat, aFloat2], " +
                "specify the field by name instead"));

        new FieldHelper(new SubClass(), SubClass.class).getValueByType(Float.class);
    }

    @Test
    public void settingFieldClassThatDontExistShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field for class java.lang.StringBuffer"));

        new FieldHelper(new SubClass(), SubClass.class).setValueByType(new StringBuffer());
    }

    @Test
    public void settingFieldNameThatDontExistShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field named gurka"));

        new FieldHelper(new SubClass(), SubClass.class).setValueByName("gurka", "");
    }

    @Test
    public void settingFieldWithWrongClassShouldThrowException() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Can not set java.lang.Runnable field refutils.testclasses.SubClass.override to java.lang.Integer"));

        FieldHelper fieldHelper = new FieldHelper(new SubClass(), SubClass.class);
        fieldHelper.setValueByName("override", 43);
    }

    @Test
    public void settingNamedFieldWithSubClassShouldWork() throws Exception {
        FieldClass instance = new FieldClass();
        FieldHelper fieldHelper = new FieldHelper(instance, FieldClass.class);

        fieldHelper.setValueByName("superClass", new SubClass());

        assertThat(fieldHelper.getValueByName("superClass"), not(nullValue()));
    }

    @Test
    public void fieldsWithCommonSuperClassShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is(
                "Found too many (3) matches for field class refutils.testclasses.SubClass [anInterface, subClass, superClass], " +
                        "specify the field by name instead"));

        new FieldHelper(new FieldClass(), FieldClass.class).getValueByType(SubClass.class);
    }

    @Test
    public void fieldsWithCommonInterfaceShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is(
                "Found too many (2) matches for field class refutils.testclasses.SuperClass [anInterface, superClass], " +
                        "specify the field by name instead"));

        new FieldHelper(new FieldClass(), FieldClass.class).getValueByType(SuperClass.class);
    }

    @Test
    public void settingInterfaceFieldShouldWork() throws Exception {
        FieldClass instance = new FieldClass();
        FieldHelper fieldHelper = new FieldHelper(instance, FieldClass.class);

        fieldHelper.setValueByType((Interface) f -> 0);

        assertThat(fieldHelper.getValueByType(Interface.class), not(nullValue()));
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

        fieldHelper.setValueByType(testValue);

        fieldHelper = new FieldHelper(instance, SubClass.class);

        assertThat(fieldHelper.getValueByType(testValue.getClass()), is(testValue));
    }

    @Test
    public void settingStaticProtectedInheritedFieldShouldWork() throws Exception {
        SubClass instance = new SubClass();
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);
        File fieldValue = new File("gurka.txt");

        fieldHelper.setValueByType(fieldValue);

        assertThat(fieldHelper.getValueByType(fieldValue.getClass()).toString(), is("gurka.txt"));
    }
    
    @Test
    public void privateTypedFieldsInSuperClassShouldBeReachable() throws Exception {
        SubClass instance = new SubClass();

        new FieldHelper(instance, SubClass.class).getValueByType(FileNotFoundException.class);
        new FieldHelper(instance, SuperClass.class).getValueByType(FileNotFoundException.class);
    }

    @Test
    public void privateNamedFieldsInSuperClassShouldBeReachable() throws Exception {
        SubClass instance = new SubClass();

        new FieldHelper(instance, SubClass.class).getValueByName("fnfex");
        new FieldHelper(instance, SuperClass.class).getValueByName("fnfex");
    }

    @Test
    public void privateFieldsInSuperClassShouldBeModifiable() throws Exception {
        SubClass instance = new SubClass();
        FileNotFoundException ex = new FileNotFoundException("Gurka");
        
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);
        fieldHelper.setValueByType(ex);
        
        assertThat(getExceptionMessage(ex), is("Gurka"));
        assertThat(getExceptionMessage(instance.getFnfex()), is("Gurka"));
        assertThat(getExceptionMessage(fieldHelper.getValueByName("fnfex")), is("Gurka"));
        assertThat(getExceptionMessage(fieldHelper.getValueByType(FileNotFoundException.class)), is("Gurka"));

        ex = new FileNotFoundException("Tomat");
        fieldHelper.setValueByName("fnfex", ex);

        assertThat(getExceptionMessage(instance.getFnfex()), is("Tomat"));
    }
    
    private String getExceptionMessage(Object ex) {
        return ((Exception) ex).getMessage();
    }

    @Test
    public void checkThatSubclassIsCorrect() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Instance of SubClass is not a subclass of SneakyConstructor");
        
        SubClass instance = new SubClass();
        new FieldHelper(instance, SneakyConstructor.class);
    }

    @Test
    public void checkThatSubclassIsCorrect2() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Instance of SubClass is not a subclass of String");
        
        SubClass instance = new SubClass();
        new FieldHelper(instance, String.class);
    }

    @Test
    public void checkThatSubclassIsCorrect3() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Instance of SuperClass is not a subclass of SubClass");
        
        SuperClass instance = new SuperClass((byte) 0);
        new FieldHelper(instance, SubClass.class);
    }

    @Test
    public void specifyingClassShouldBeOkButUnnecessary() throws Exception {
        SubClass instance = new SubClass();
        FieldHelper fieldHelper = new FieldHelper(instance, SubClass.class);

        fieldHelper.setValueByName("anObject", new Object());

        assertThat(fieldHelper.getValueByName("anObject"), not(nullValue()));
    }
    
    @Test
    public void settingOverridenVariableInSuperClassShouldNotAffectSubClass() {
        SubClass instance = new SubClass();
        Runnable gurka = new Runnable() {
            public void run() {}
            
            public String toString() { return "gurka"; }
        };
        Runnable tomat = new Runnable() {
            public void run() {}
            
            public String toString() { return "tomat"; }
        };

        assertThat(instance.getOverride(), nullValue());
        
        new ReflectionHelper(instance, SuperClass.class).setField(gurka);
        assertThat(instance.getOverride(), nullValue());
        assertThat(instance.getSuperOverride().toString(), is("gurka"));
        
        new ReflectionHelper(instance, SuperClass.class).setField("override", tomat);
        assertThat(instance.getOverride(), nullValue());
        assertThat(instance.getSuperOverride().toString(), is("tomat"));
        
        new ReflectionHelper(instance).setField(gurka);
        assertThat(instance.getOverride().toString(), is("gurka"));
        assertThat(instance.getSuperOverride().toString(), is("tomat"));
    }
}
