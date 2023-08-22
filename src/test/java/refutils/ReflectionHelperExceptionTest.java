package refutils;

import static org.hamcrest.Matchers.is;

import java.util.Calendar;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.*;

public class ReflectionHelperExceptionTest {

  @Rule public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void instantiateWithNoDefaultConstructorShouldThrowException() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is("Cannot instantiate Class: refutils.testclasses.SubClassToThread constructor"));

    ReflectionHelper.instantiatePrivateConstructor(SubClassToThread.class);
  }

  @Test
  public void instantiateAbstractShouldThrowException() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is("Cannot instantiate Class: refutils.testclasses.AbstractClass constructor"));

    ReflectionHelper.instantiatePrivateConstructor(AbstractClass.class);
  }

  @Test
  public void instantiateEnumShouldThrowException() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is("Cannot instantiate Class: refutils.testclasses.EnumType constructor"));

    ReflectionHelper.instantiatePrivateConstructor(EnumType.class);
  }

  @Test
  public void instantiateFaultyConstructorShouldThrowException() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is("Cannot instantiate Class: refutils.testclasses.SneakyConstructor constructor"));

    ReflectionHelper.instantiatePrivateConstructor(SneakyConstructor.class);
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  public void nullInstanceShouldThrowException() {
    expectedException.expect(NullPointerException.class);
    expectedException.expectMessage(is("The instance in the ReflectionHelper cannot be null"));

    new ReflectionHelper(null);
  }

  @Test
  public void settingNonExistingFieldShouldThrowException() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is("java.lang.NoSuchFieldException: Cannot find visible field named gurka"));

    SubClass instance = new SubClass();
    ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
    reflectionHelper.setField("gurka", 42L);
  }

  @Test
  public void settingNonExistingFieldShouldThrowException2() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is(
            "java.lang.NoSuchFieldException: Cannot find visible field for class java.util.GregorianCalendar"));

    SubClass instance = new SubClass();
    ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
    reflectionHelper.setField(Calendar.getInstance());
  }

  @Test
  public void gettingNonExistingFieldShouldThrowException() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is("java.lang.NoSuchFieldException: Cannot find visible field named gurka"));

    SubClass instance = new SubClass();
    ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
    reflectionHelper.getField("gurka");
  }

  @Test
  public void gettingNonExistingFieldShouldThrowException2() {
    expectedException.expect(ReflectionHelperException.class);
    expectedException.expectMessage(
        is(
            "java.lang.NoSuchFieldException: Cannot find visible field for class java.util.Calendar"));

    SubClass instance = new SubClass();
    ReflectionHelper reflectionHelper = new ReflectionHelper(instance);
    reflectionHelper.getField(Calendar.class);
  }
}
