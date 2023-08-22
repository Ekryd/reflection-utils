package refutils.testclasses;

/**
 * @author bjorn
 * @since 2013-10-07
 */
@SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
public class SubClassToThread extends Thread {
  private int something;
  private final RuntimeException rex;

  public SubClassToThread(RuntimeException rex) {
    this.rex = rex;
  }
}
