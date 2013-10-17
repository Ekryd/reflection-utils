package refutils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.testclasses.AbstractClass;
import refutils.testclasses.SubClassToThread;
import refutils.testclasses.SuperClass;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;

@SuppressWarnings("unchecked")
public class ConstructorHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void instantiatePrivateNonArgConstructorShouldWork() {
        assertNotNull(new ConstructorHelper(SuperClass.class).instantiatePrivate());
    }

    @Test
    public void instantiateWithNoDefaultConstructorShouldThrowException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Cannot instantiate Class: refutils.testclasses.SubClassToThread constructor"));

        assertNotNull(new ConstructorHelper(SubClassToThread.class).instantiatePrivate());
    }

    @Test
    public void instantiateAbstractShouldThrowException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Cannot instantiate Class: refutils.testclasses.AbstractClass constructor"));

        assertNotNull(new ConstructorHelper(AbstractClass.class).instantiatePrivate());
    }
}
