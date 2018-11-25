package refutils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.*;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;

public class ReflectionHelperExceptionTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void instantiateWithNoDefaultConstructorShouldThrowException() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("Cannot instantiate Class: refutils.testclasses.SubClassToThread constructor"));

        ReflectionHelper.instantiatePrivateConstructor(SubClassToThread.class);
    }

    @Test
    public void instantiateAbstractShouldThrowException() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("Cannot instantiate Class: refutils.testclasses.AbstractClass constructor"));

        ReflectionHelper.instantiatePrivateConstructor(AbstractClass.class);
    }

    @Test
    public void instantiateEnumShouldThrowException() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("Cannot instantiate Class: refutils.testclasses.EnumType constructor"));

        ReflectionHelper.instantiatePrivateConstructor(EnumType.class);
    }

    @Test
    public void instantiateFaultyConstructorShouldThrowException() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("Cannot instantiate Class: refutils.testclasses.SneakyConstructor constructor"));

        ReflectionHelper.instantiatePrivateConstructor(SneakyConstructor.class);
    }

    @Test
    public void nullInstanceShouldThrowException() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(is("The instance in the ReflectionHelper cannot be null"));

        new ReflectionHelper(null);
    }

    @Test
    public void settingNonExistingFieldShouldThrowException() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("java.lang.NoSuchFieldException: Cannot find visible field named gurka"));

        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField("gurka", 42L);
    }

    @Test
    public void settingNonExistingFieldShouldThrowException2() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("java.lang.NoSuchFieldException: Cannot find visible field for class java.util.GregorianCalendar"));

        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.setField(Calendar.getInstance());
    }

    @Test
    public void gettingNonExistingFieldShouldThrowException() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("java.lang.NoSuchFieldException: Cannot find visible field named gurka"));

        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.getField("gurka");
    }

    @Test
    public void gettingNonExistingFieldShouldThrowException2() {
        expectedException.expect(ReflectionHelperException.class);
        expectedException.expectMessage(is("java.lang.NoSuchFieldException: Cannot find visible field for class java.util.Calendar"));

        SubClass instance = new SubClass();
        ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
        reflectionHelper.getField(Calendar.class);
    }

    @Test
    public void settingFinalStaticFieldDoesNotWork() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(is("Since JDK9 it is no longer possible to modify static final fields. " +
                "Could not make private static final java.util.concurrent.TimeUnit refutils.testclasses.SubClass.FINAL_FIELD " +
                "accessable"));

        SubClass instance = new SubClass();
        new ReflectionHelper(instance).setField("FINAL_FIELD", TimeUnit.MINUTES);
    }
}
