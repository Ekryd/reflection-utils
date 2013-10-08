package refutils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.FieldClass;
import refutils.testclasses.Interface;
import refutils.testclasses.SubClass;
import refutils.testclasses.SuperClass;

import java.io.FileNotFoundException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author bjorn
 * @since 2013-10-08
 */
public class FieldHelperTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void settingInheritedPrivateFieldShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field for class java.io.FileNotFoundException"));
        
        new FieldHelper(SubClass.class,FileNotFoundException.class);
    }

    @Test
    public void objectInstanceShouldNotBeSetWithClassReference() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Cannot match Object.class type parameter, you must specify it by name"));

        new FieldHelper(SubClass.class, Object.class);
    }

    @Test
    public void objectInstanceShouldBeSetWithNameReference() throws Exception {
        FieldHelper fieldHelper = new FieldHelper(SubClass.class, "anObject");
        SubClass instance = new SubClass((byte) 0);

        fieldHelper.setValue(instance, new Object());

        assertThat(fieldHelper.getValue(instance), not(nullValue()));
    }

    @Test
    public void primitiveAndClassFieldsShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Found 2 matches for field class java.lang.Float [aFloat, aFloat2]"));

        new FieldHelper(SubClass.class, Float.class);
    }

    @Test
    public void settingFieldClassThatDontExistShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field for class java.lang.StringBuffer"));
        
        new FieldHelper(SubClass.class, StringBuffer.class);
    }

    @Test
    public void settingFieldNameThatDontExistShouldThrowException() throws Exception {
        expectedException.expect(NoSuchFieldException.class);
        expectedException.expectMessage(is("Cannot find visible field named gurka"));
        
        new FieldHelper(SubClass.class, "gurka");
    }

    @Test
    public void settingFieldWithWrongClassShouldThrowException() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Can not set java.lang.Runnable field refutils.testclasses.SubClass.override to java.lang.Integer"));

        FieldHelper fieldHelper = new FieldHelper(SubClass.class, "override");
        fieldHelper.setValue(new SubClass((byte)0), 43);
    }

    @Test
    public void settingNamedFieldWithSubClassShouldWork() throws Exception {
        FieldHelper fieldHelper = new FieldHelper(FieldClass.class, "superClass");
        FieldClass instance = new FieldClass();
        
        fieldHelper.setValue(instance, new SubClass((byte)0));

        assertThat(fieldHelper.getValue(instance), not(nullValue()));
    }

    @Test
    public void fieldsWithCommonSuperClassShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Found 3 matches for field class refutils.testclasses.SubClass [anInterface, subClass, superClass]"));

        new FieldHelper(FieldClass.class, SubClass.class);
    }

    @Test
    public void fieldsWithCommonInterfaceShouldBeCountedAsSame() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Found 2 matches for field class refutils.testclasses.SuperClass [anInterface, superClass]"));

        new FieldHelper(FieldClass.class, SuperClass.class);
    }

    @Test
    public void settingInterfaceFieldShouldWork() throws Exception {
        FieldHelper fieldHelper =  new FieldHelper(FieldClass.class, Interface.class);
        FieldClass instance = new FieldClass();
        
        fieldHelper.setValue(instance, new Interface() {
            @Override
            public int interfaceMethod(long f) {
                return 0;
            }
        });

        assertThat(fieldHelper.getValue(instance), not(nullValue()));
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
        FieldHelper fieldHelper = new FieldHelper(SubClass.class, testValue.getClass());
        SubClass instance = new SubClass((byte)0);

        fieldHelper.setValue(instance, testValue);

        fieldHelper = new FieldHelper(SubClass.class, testValue.getClass());

        assertThat(fieldHelper.getValue(instance), is(testValue));
    }

}
