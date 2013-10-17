package refutils.testclasses;

/**
 * @author bjorn
 * @since 2013-10-07
 */
@SuppressWarnings("UnusedDeclaration")
public class SubClassToThread extends Thread {
    private int something;
    private RuntimeException rex;

    public SubClassToThread(RuntimeException rex) {
        this.rex = rex;
    }
}
