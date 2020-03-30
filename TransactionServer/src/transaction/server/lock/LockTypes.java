package transaction.server.lock;

// LockTypes interface
public interface LockTypes
{
  public final int NO_LOCK = 0;
  public final int WRITE_LOCK = 1;
  public final int READ_LOCK = 2;
  public final int EMPTY_LOCK = 3;
}
