package refutils.util;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.SuperClass;

@SuppressWarnings("unchecked")
public class ConstructorHelperTest {

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void instantiatePrivateNonArgConstructorShouldWork()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException {
    assertNotNull(new ConstructorHelper(SuperClass.class).instantiatePrivate());
  }
}
