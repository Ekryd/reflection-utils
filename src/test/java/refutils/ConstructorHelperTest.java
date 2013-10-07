package refutils;

import org.junit.Test;
import refutils.testclasses.AbstractClass;
import refutils.testclasses.SubClass;
import refutils.testclasses.SuperClass;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ConstructorHelperTest {

    @Test
    public void instantiatePrivateNonArgConstructorShouldWork() {
        assertNotNull(new ConstructorHelper(SubClass.class).instantiatePrivate());
    }

    @Test
    public void instantiateWithNoDefaultConstructorShouldThrowException() {
        try {
            assertNotNull(new ConstructorHelper(SuperClass.class).instantiatePrivate());
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Cannot instantiate Class: refutils.testclasses.SuperClass constructor"));
        }
    }

    @Test
    public void instantiateAbstractShouldThrowException() {
        try {
            assertNotNull(new ConstructorHelper(AbstractClass.class).instantiatePrivate());
            fail();
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Cannot instantiate Class: refutils.testclasses.AbstractClass constructor"));
        }
    }
}
