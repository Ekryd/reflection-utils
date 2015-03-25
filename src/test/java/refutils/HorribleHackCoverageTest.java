package refutils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.Test;
import refutils.testclasses.SubClass;

/**
 * @author bjorn
 * @since 15-03-19
 */
public class HorribleHackCoverageTest {

    @Test
    public void simulateImpossibleException() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("refutils.ReflectionHelper");
        CtClass clazz = cp.get("java.lang.Class");
        CtMethod m = cc.getDeclaredMethod("getField", new CtClass[]{clazz});

        //System.out.println(m.getMethodInfo().getLineNumber(60));
        m.insertAt(116, "{ throw new java.lang.RuntimeException(); }");
        //throw new IllegalAccessException();
        //m.insertAt(116, "{ System.out.println(\"Bah!\"); }");

        Class c = cc.toClass();
        SubClass instance = new SubClass();
        ReflectionHelper h = (ReflectionHelper) c.getConstructor(Object.class).newInstance(instance);
        String s = h.getField(String.class);
    }

}
