package refutils.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.SuperClass;

@SuppressWarnings("unchecked")
public class ConstructorHelperTest {

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void instantiatePrivateNonArgConstructorShouldWork() throws Exception {
    assertNotNull(new ConstructorHelper(SuperClass.class).instantiatePrivate());
  }
}
