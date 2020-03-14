
// Transaction Client class
public class TransactionClient extends Thread
{
  public void run()
  {
    // Spawns off proxy threads
    TServerProxy worker = new TServerProxy();
    Thread proxyWorker = new Thread( worker );
    proxyWorker.start();
  }
}
