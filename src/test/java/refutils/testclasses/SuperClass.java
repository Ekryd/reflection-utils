package refutils.testclasses;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author bjorn
 * @since 2013-10-01
 */
@SuppressWarnings("UnusedDeclaration")
public class SuperClass implements Interface {
    private long longPrivate;
    private final byte bytePrivateFinal;
    int intPackage;
    protected boolean boolProtected;
    private String stringPrivate;
    protected Runnable override;
    private FileNotFoundException fnfex;
    public Object anObject;
    protected Float aFloat;
    protected float aFloat2;
    protected static File file;

    private SuperClass() {
        bytePrivateFinal = 0;
    }

    public SuperClass(byte bytePrivateFinal) {
        this.bytePrivateFinal = bytePrivateFinal;
    }

    @Override
    public int interfaceMethod(long f) {
        return 0;
    }

    public boolean isBoolProtected() {
        return boolProtected;
    }

    public int getIntPackage() {
        return intPackage;
    }

    public long getLongPrivate() {
        return longPrivate;
    }

    public String getStringPrivate() {
        return stringPrivate;
    }

    public byte getBytePrivateFinal() {
        return bytePrivateFinal;
    }

    public Runnable getSuperOverride() {
        return override;
    }


}
